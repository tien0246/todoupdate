package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String filePath = "data.txt";
    private static final File file = new File(filePath);
    private static todolistGUI gui;

    public static void main(String[] args) {
        gui = new todolistGUI();
    }

    public static void checkAppUpdate() {
        String result = null;
        File dataFile = new File(filePath);
        if (!dataFile.exists()) {
            return;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(dataFile);
            if (!scanner.hasNextLine()) {
                return;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        while (true) {
            assert scanner != null;
            if (!scanner.hasNextLine()) break;
            String[] data = scanner.nextLine().split("\\|");
            if (data.length < 2) {
                continue;
            }
            if (data.length == 2) {
                data = new String[]{data[0], data[1], "US"};
            }
            try {
                result = HttpsRequest.getDataBundleID(data[0], data[2].split(","));
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (result == null) {
                gui.addInfoPanel("Not found", data[0], data[1], "Not found", null);
                continue;
            }
            String newVersion = regex("\"version\":\"(.*?)\"", result);
            String dateString = regex("\"currentVersionReleaseDate\":\"(.*?)\"", result);
            assert dateString != null;
            LocalDateTime specifiedDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(currentDateTime, specifiedDateTime.plusDays(7));
            if (newVersion != null && !newVersion.equals(data[1])) {
                newVersion += " (" + duration.toDays() + " days left)";
                String appName = regex("\"trackName\":\"(.*?)\"", result);
                Image image;
                try {
                    image = HttpsRequest.getImage(regex("\"artworkUrl60\":\"(.*?)\"", result));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gui.addInfoPanel(appName, data[0], data[1], newVersion, image);
            }
        }
        gui.addBlankPanel();
        gui.reloadGUI();
    }

    public static String regex(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (regex.equals("\\d+")) {
                StringBuilder resultBuilder = new StringBuilder(matcher.group());
                while (matcher.find()) {
                    resultBuilder.append(matcher.group());
                }
                return resultBuilder.toString();
            }
            return matcher.group(1);
        }
        return null;
    }

    public static void addApp(String bundleID, String version, String countryCodesSelected) {
        if (bundleID.isEmpty() || version.isEmpty()) {
            return;
        }
        System.out.println("Adding app: " + bundleID + " " + version + " " + countryCodesSelected);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    writeToFile(bundleID + "|" + version + "|" + countryCodesSelected);
                }
            } else if (!isBundleIDExist(bundleID)) {
                writeToFile("\n" + bundleID + "|" + version + "|" + countryCodesSelected);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static boolean isBundleIDExist(String bundleID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && parts[0].equals(bundleID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static String[] getCountryCodesSelected(String bundleID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && parts[0].equals(bundleID)) {
                    return parts[2].split(",");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new String[0];
    }

    public static void doneApp(String bundleID, String oldVersion, String newVersion) {
        try {
            Path path = Paths.get(filePath);
            String fileContent = new String(Files.readAllBytes(path));
            String updatedContent = fileContent.replace(bundleID + "|" + oldVersion, bundleID + "|" + newVersion);
            Files.write(path, updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteApp(String bundleID, String version) {
        String fileContent;
        try {
            Path path = Paths.get(filePath);
            fileContent = new String(Files.readAllBytes(path));
            String updatedContent = fileContent.replaceFirst(bundleID + "\\|" + version + "\\|.*", "");
            Files.write(path, updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void editApp(String bundleID, String version, String newBundleID, String newVersion, String countryCodesSelected) {
        try {
            Path path = Paths.get(filePath);
            String fileContent = new String(Files.readAllBytes(path));
            String updatedContent = fileContent.replaceFirst(bundleID + "\\|" + version + "\\|.*", newBundleID + "|" + newVersion + "|" + countryCodesSelected);
            Files.write(path, updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void writeToFile(String content) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

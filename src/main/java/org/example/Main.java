package org.example;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
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
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split("\\|");
            if (data.length != 2) {
                continue;
            }
            try {
                result = HttpsRequest.getDataBundleID(data[0]);
                System.out.println("Response: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (regex("\"resultCount\":(.*?),", result).equals("0")) {
                gui.addInfoPanel("Not found", data[0], data[1], "Not found", null);
                continue;
            }
            String newVersion = regex("\"version\":\"(.*?)\"", result);
            String dateString = regex("\"currentVersionReleaseDate\":\"(.*?)\"", result);
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
    }

    public static String regex(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find() ) {
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

    public static void addApp(String bundleID, String version) {
        if (bundleID.isEmpty() || version.isEmpty()) {
            return;
        }
        try {
            String currentDirectory = System.getProperty("user.dir");
            File file = new File(currentDirectory, filePath);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    writeToFile(file, bundleID + "|" + version);
                }
            } else if (!isBundleIDExist(file, bundleID)) {
                writeToFile(file, "\n" + bundleID + "|" + version);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isBundleIDExist(File file, String bundleID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0 && parts[0].equals(bundleID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void doneApp(String bundlID, String oldVersion, String newVersion) {
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            String updatedContent = fileContent.replace(bundlID + "|" + oldVersion, bundlID + "|" + newVersion);
            Files.write(Paths.get(filePath), updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteApp(String bundlID, String version) {
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            String updatedContent = fileContent.replace(bundlID + "|" + version, "");
            Files.write(Paths.get(filePath), updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editApp(String bundlID, String version, String newBundleID, String newVersion) {
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            String updatedContent = fileContent.replace(bundlID + "|" + version, newBundleID + "|" + newVersion);
            Files.write(Paths.get(filePath), updatedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

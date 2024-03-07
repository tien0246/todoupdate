package org.example;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

import static org.example.Main.regex;

public class HttpsRequest {

    public static String getDataBundleID(String bundleID, String[] stores) throws IOException {
        String maxResult = null;
        String maxVersion = "0.0.0.0";
        for (String store : stores) {
            String result = getDataBundleID(bundleID, store);
            System.out.println("Response: " + result);
            if (Objects.equals(regex("\"resultCount\":(.*?),", result), "0")) {
                continue;
            }
            String versionString = regex("\"version\":\"(.*?)\"", result);
            if (compareVersions(versionString, maxVersion)) {
                maxVersion = versionString;
                maxResult = result;
            }
        }
        return maxResult;
    }

    private static boolean compareVersions(String version1, String version2) {
        version1 = version1.replaceAll("\\(.*?\\)", "");
        version2 = version2.replaceAll("\\(.*?\\)", "");
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int minLength = Math.min(parts1.length, parts2.length);

        for (int i = 0; i < minLength; i++) {
            if (Integer.parseInt(parts1[i]) > Integer.parseInt(parts2[i])) {
                return true;
            } else if (Integer.parseInt(parts1[i]) < Integer.parseInt(parts2[i])) {
                return false;
            }
        }
        return parts1.length > parts2.length;
    }

    private static String getDataBundleID(String bundleID, String country) throws IOException {
        HttpsURLConnection urlConnection = getHttpsURLConnection("https://itunes.apple.com/lookup?bundleId=" + bundleID + "&country=" + country);
        String responseBody = null;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            responseBody = response.toString();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            urlConnection.disconnect();
        }
        return responseBody;
    }

    public static Image getImage(String url) throws IOException {
        HttpsURLConnection urlConnection = getHttpsURLConnection(url);
        Image image = null;
        try (InputStream in = urlConnection.getInputStream()) {
            image = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            urlConnection.disconnect();
        }
        return image;
    }

    private static HttpsURLConnection getHttpsURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setConnectTimeout(5000);
        urlConnection.connect();
        return urlConnection;
    }
}

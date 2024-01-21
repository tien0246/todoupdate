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

    public static String getDataBundleID(String bundleID) throws IOException {
        String[] stores = {"us", "vn", "ph", "jp", "kr", "tw"};
        String maxResult = null;
        int maxVersion = Integer.MIN_VALUE;
        int count = 0;
        for (String store : stores) {
            if (count == 2) {
                break;
            }
            String result = getDataBundleID(bundleID, store);
            if (Objects.equals(regex("\"resultCount\":(.*?),", result), "0")) {
                continue;
            }
            String versionString = regex("\"version\":\"(.*?)\"", result);
            System.out.println("Version: " + versionString);
            String versionDigits = regex("\\d+", versionString);
            System.out.println("Version: " + versionDigits);
            int version = Integer.parseInt(Objects.requireNonNull(versionDigits));
            if (version == maxVersion) {
                count++;
            }

            if (version > maxVersion) {
                maxVersion = version;
                maxResult = result;
                count = 0;
            }
        }
        return maxResult;
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
            e.printStackTrace();
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
            e.printStackTrace();
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

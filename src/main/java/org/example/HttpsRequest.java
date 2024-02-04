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
//        int maxVersion = Integer.MIN_VALUE;
        String maxVersion = "0.0.0.0";
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
//            String versionDigits = regex("\\d+", versionString);
//            int version = Integer.parseInt(Objects.requireNonNull(versionDigits));
//            if (version == maxVersion) {
//                count++;
//            }
//
//            if (version > maxVersion) {
//                maxVersion = version;
//                maxResult = result;
//                count = 0;
//            }
            if (compareVersions(versionString, maxVersion)) {
                maxVersion = versionString;
                maxResult = result;
                count = 0;
            } else {
                count++;
            }
        }
        return maxResult;
    }

    private static boolean compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int minLength = Math.min(parts1.length, parts2.length);

        for (int i = 0; i < minLength; i++) {
            int num1 = Integer.parseInt(parts1[i]);
            int num2 = Integer.parseInt(parts2[i]);

            if (num1 > num2) {
                return true;
            } else if (num1 < num2) {
                return false;
            }
        }

        // Nếu các phần số giống nhau, phiên bản có nhiều phần số sẽ lớn hơn
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

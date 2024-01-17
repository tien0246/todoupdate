package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequest {

    public static String urlRequest(String urlString) throws IOException {
        HttpsURLConnection urlConnection = getHttpsURLConnection(urlString);
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
        }

        return responseBody;
    }

    private static HttpsURLConnection getHttpsURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // write out form parameters (if needed)
        try (PrintWriter out = new PrintWriter(urlConnection.getOutputStream())) {
            // out.println("param1=value1&param2=value2");
        }

        // set connection timeout
        urlConnection.setConnectTimeout(5000); // 5 seconds timeout

        // connect
        urlConnection.connect();

        return urlConnection;
    }

    public static void main(String[] args) {
        try {
            String result = urlRequest("https://example.com");
            System.out.println("Response: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

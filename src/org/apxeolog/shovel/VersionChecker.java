package org.apxeolog.shovel;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by APXEOLOG on 02.09.2015.
 * Check github releases for new version
 */
public class VersionChecker {
    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/apxeolog/hafen-client-shovel/releases/latest";

    public static String check() {
        String response = getUrlResponse(LATEST_RELEASE_URL);
        if (response != null && !response.isEmpty()) {
            HashMap<String, String> responseMap = new HashMap<>();
            Gson gson = new Gson();
            responseMap = gson.fromJson(response, responseMap.getClass());
            // Check version
            String version = responseMap.get("tag_name");
            String information = responseMap.get("body");
            if (getVersionNum(version) > getVersionNum(Shovel.getVersion()))
                return "Version " + version + "\n\n" + information;
        }
        return null;
    }

    private static int getVersionNum(String version) {
        try {
            return Integer.parseInt(version.replaceAll("\\.", ""));
        } catch (Exception ex) {
            return 0;
        }
    }

    private static String getUrlResponse(String url) {
        HttpURLConnection connection = null;
        String result = "";
        try {
            URL final_url = new URL(url);
            // Create connection
            connection = (HttpURLConnection) final_url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(5000); // 5 sec timeout
            // Connect
            connection.connect();
            int responseCode = connection.getResponseCode();
            // Read the result
            InputStream inputStream = null;
            try {
                inputStream = connection.getInputStream();
                result = readStream(inputStream);
                inputStream.close();
            } catch (Exception ex) {
                // UPS
            }
        } catch (Exception ex) {
            // Read error stream to allow reusing this socket
            if (connection.getErrorStream() != null) {
                result = readStream(connection.getErrorStream());
                try {
                    connection.getErrorStream().close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    private static String readStream(InputStream stream) {
        BufferedReader reader = null;
        try {
            reader  = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            return null;
        }
    }
}

package org.apxeolog.shovel.highlight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import haven.MainFrame;
import org.apxeolog.shovel.ALS;
import org.apxeolog.shovel.Shovel;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by APXEOLOG on 08/09/2015.
 */
public class HighlightManager {
    private static final Pattern COLOR_PATTERN = Pattern.compile("rgba\\((\\d+), (\\d+), (\\d+), (\\d+)\\)", Pattern.CASE_INSENSITIVE);
    private static HighlightGroup[] highlightGroupsCache = null;
    private static ArrayList<HighlightOption> highlightDataCache = null;

    /**
     * Copy base file from jar to folder, if not exists
     * @param destination
     */
    private static void copyHighlightFileFromJar(File destination) {
        try {
            try (InputStream inputStream = MainFrame.class.getResourceAsStream("highlight.json");
                 OutputStream outputStream = new FileOutputStream(destination)) {
                int read = 0;
                byte buffer[] = new byte[1024];
                while ((read = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, read);
                }
            }
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot restore highlight.json from jar");
        }
    }

    /**
     * Rebuild cache for fast check
     */
    private static void rebuildHighlightDataCache() {
        if (highlightDataCache == null) {
            highlightDataCache = new ArrayList<>();
        }
        highlightDataCache.clear();
        HighlightGroup[] groups = getHighlightGroups();
        for (int i = 0; i < groups.length; i++) {
            for (int j = 0; j < groups[i].options.length; j++) {
                if (groups[i].options[j].enabled) {
                    highlightDataCache.add(groups[i].options[j]);
                }
            }
        }
    }

    /**
     * Get array with enabled highlights
     * @return
     */
    public static ArrayList<HighlightOption> getFinalHighlightData() {
        if (highlightDataCache == null) {
            highlightDataCache = new ArrayList<>();
            rebuildHighlightDataCache();
        }
        return highlightDataCache;
    }

    /**
     * Get highlights group
     * @return
     */
    public static HighlightGroup[] getHighlightGroups() {
        if (highlightGroupsCache == null) {
            highlightGroupsCache = loadHighlightData();
        }
        return highlightGroupsCache;
    }

    /**
     * Save highlights group
     */
    public static void saveHighlightGroups() {
        File highlightFile = new File(Shovel.getWorkingDirectory(), "highlight.json");
        try {
            rebuildHighlightDataCache();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Color.class, new ColorClassAdapter())
                    .setPrettyPrinting().create();
            Files.write(highlightFile.toPath(), gson.toJson(highlightGroupsCache).getBytes(Charset.forName("utf-8")), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot save highlights file:", ex.getMessage());
        }
    }

    /**
     * Load data from file
     * @return
     */
    public static HighlightGroup[] loadHighlightData() {
        HighlightGroup[] highlightGroups = new HighlightGroup[0];
        File highlightFile = new File(Shovel.getWorkingDirectory(), "highlight.json");
        try {
            if (!highlightFile.exists()) {
                copyHighlightFileFromJar(highlightFile);
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Color.class, new ColorClassAdapter())
                    .create();
            highlightGroups = gson.fromJson(new FileReader(highlightFile), highlightGroups.getClass());
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot load highlight data:", ex.getMessage());
        }
        return highlightGroups;
    }

    /**
     * Format color as string
     * @param color
     * @return
     */
    public static String formatColor(Color color) {
        try {
            return String.format("rgba(%d, %d, %d, %d)", color.getRed(),
                    color.getGreen(), color.getBlue(), color.getAlpha());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Parse color from string
     * @param string
     * @return
     */
    public static Color parseColor(String string) {
        Matcher matcher = COLOR_PATTERN.matcher(string);
        if (matcher.find() && matcher.groupCount() == 4) {
            return new Color(
                    Integer.valueOf(matcher.group(1)),
                    Integer.valueOf(matcher.group(2)),
                    Integer.valueOf(matcher.group(3)),
                    Integer.valueOf(matcher.group(4))
            );
        }
        return null;
    }

    /**
     * Adapter for proper serializations of Color class
     */
    protected static class ColorClassAdapter extends TypeAdapter<Color> {
        @Override
        public void write(JsonWriter jsonWriter, Color color) throws IOException {
            if (color == null)
                jsonWriter.value((String) null);
            else
                jsonWriter.value(HighlightManager.formatColor(color));
        }

        @Override
        public Color read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return HighlightManager.parseColor(jsonReader.nextString());
        }
    }
}

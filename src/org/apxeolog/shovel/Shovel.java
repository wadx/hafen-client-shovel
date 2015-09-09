package org.apxeolog.shovel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Created by APXEOLOG on 31/08/2015.
 * Main class to init startup data like configs etc
 */
public class Shovel {
    private static String version = "1.2.2";
    private static Settings settings;
    private static File workingDirectory;
    private static File userDirectory;

    /**
     * Load settings from settings.json
     */
    private static void loadSettings() {
        File settingsFile = new File(workingDirectory, "settings.json");
        try {
            if (settingsFile.exists()) {
                Gson gson = new Gson();
                settings = gson.fromJson(new FileReader(settingsFile), Settings.class);
            } else {
                settings = new Settings();
                saveSettings();
            }
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot load settings:", ex.getMessage());
            settings = new Settings();
            saveSettings();
        }
    }

    public static String checkForNewReleases() {
        if (!getSettings().checkedForNewReleases) {
            getSettings().checkedForNewReleases = true;
            return VersionChecker.check();
        }
        return null;
    }

    /**
     * Get Shovel settings
     * @return
     */
    public static Settings getSettings() {
        return settings;
    }

    /**
     * Save settings to file
     */
    public static void saveSettings() {
        File settingsFile = new File(workingDirectory, "settings.json");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.write(settingsFile.toPath(), gson.toJson(settings).getBytes(Charset.forName("utf-8")), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot save settings file:", ex.getMessage());
        }
    }

    /**
     * Get Shovel client version
     * @return
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Init method
     */
    public static void init() {
        try {
            // Detect working directory, in case jar not in WORKING_DIR
            workingDirectory = new File(Shovel.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot detect jar path, using working directory instead");
            workingDirectory = new File("");
        }
        try {
            // Get user directory
            userDirectory = new File(new File(System.getProperty("user.home", ""), ".haven"), "hafen");
            if (!userDirectory.exists()) userDirectory.mkdirs();
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot create user directory");
            userDirectory = new File("");
        }
        loadSettings();
    }

    /**
     * Log errors to file
     * @param throwable
     * @param additional
     */
    public static void logErrorToFile(Throwable throwable, String... additional) {
        File errorLog = new File(workingDirectory, "error.log");
        try {
            StringWriter stringWriter = new StringWriter();
            stringWriter.append("=====================================\r\n");
            throwable.printStackTrace(new PrintWriter(stringWriter));
            stringWriter.append("\r\n");
            if (additional.length > 0) {
                stringWriter.append("======== ADDITIONAL\r\n");
                for (int i = 0; i < additional.length; i++) {
                    stringWriter.append(additional[i]);
                    stringWriter.append("\r\n");
                }
                stringWriter.append("=====================================\r\n");
            }
            Files.write(errorLog.toPath(), stringWriter.toString().getBytes(Charset.forName("utf-8")), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot save settings file:", ex.getMessage());
        }
    }

    /**
     * Get working directory
     * @return
     */
    public static File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * User directory
     * @return
     */
    public static File getUserDirectory() {
        return userDirectory;
    }

    /**
     * Directory for custom resources
     * @return
     */
    public static File getCustomResourceDir() {
        File customResDir = new File(workingDirectory, "res");
        if (!customResDir.exists()) customResDir.mkdir();
        return customResDir;
    }

    public static void main(String[] args) {

    }
}

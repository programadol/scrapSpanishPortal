package com.scrap.lib.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String LOG_FILE_PATH = "log.txt";
    private static final File outputDir;
    private static final File outputFile;

    static{
        outputDir = new File(System.getenv("APPDATA") + "/scraper/");
        outputDir.mkdirs();
        outputFile = new File(outputDir, LOG_FILE_PATH);
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            String timestamp = dateFormat.format(new Date());
            writer.write(timestamp + " - " + message);
            System.out.println(timestamp + " - " + message);
            writer.newLine();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
        }
    }

    public static void logError(String message) {
        log("[ERROR] " + message);
    }

    public static void logInfo(String message) {
        log("[INFO] " + message);
    }

    public static void logWarning(String message) {
        log("[WARNING] " + message);
    }
}

package com.scrap.lib.cache;

import com.scrap.lib.log.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IDCache {
    private final String FILENAME;

    private final Set<String> cachedIDs;

    private File outputDir;
    private File archivo;


    public IDCache(String idFilename) {
        cachedIDs = new HashSet<>();
        FILENAME = "cached_ads_" + idFilename + ".txt";
        outputDir = new File(System.getenv("APPDATA") + "/scraper/");
        outputDir.mkdirs();
        archivo = new File(outputDir, FILENAME);
        loadCacheFromFile();
    }

    public boolean isIDSeenBefore(String id) {
        return cachedIDs.contains(id);
    }

    public Set<String> getCachedIDs() {
        return cachedIDs;
    }

    public void addID(String id) {
        cachedIDs.add(id);
        saveCacheToFile();
    }

    private void loadCacheFromFile() {
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                Logger.log("Archivo caché creado correctamente con el nombre " + FILENAME);
            } catch (Exception e) {
                Logger.log("Error creando archivo de caché " + FILENAME + " , errores: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                cachedIDs.add(line.trim());
            }
        } catch (Exception e) {
            Logger.log("Lectura del archivo de caché " + FILENAME + " , errores: " + e.getMessage());
        }
    }

    private void saveCacheToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String id : cachedIDs) {
                bw.write(id);
                bw.newLine();
            }
        } catch (Exception e) {
            Logger.log("Error guardando en archivo de caché " + FILENAME + " , errores: " + e.getMessage());
        }
    }
}

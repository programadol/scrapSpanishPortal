package com.scrap.lib.cache;


import com.scrap.Main;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class IDCache {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private final String FILENAME;

    private final Set<String> cachedIDs;

    private File outputDir;

    private File archivo;

    public IDCache(String idFilename, String folderPath) {
        cachedIDs = new HashSet<>();
        FILENAME = "cached_ads_" + idFilename + ".txt";
        outputDir = new File(folderPath);
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
                logger.log(Level.DEBUG,"Archivo caché creado correctamente con el nombre " + FILENAME);
            } catch (Exception e) {
                logger.log(Level.DEBUG,"Error creando archivo de caché " + FILENAME + " , errores: " + e.getMessage());
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                cachedIDs.add(line.trim());
            }
        } catch (Exception e) {
            logger.log(Level.DEBUG,"Lectura del archivo de caché " + FILENAME + " , errores: " + e.getMessage());
        }
    }

    private void saveCacheToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String id : cachedIDs) {
                bw.write(id);
                bw.newLine();
            }
        } catch (Exception e) {
            logger.log(Level.DEBUG,"Error guardando en archivo de caché " + FILENAME + " , errores: " + e.getMessage());
        }
    }
}

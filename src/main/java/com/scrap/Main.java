package com.scrap;

import com.sun.media.jfxmedia.logging.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.File;

@Command(name = "Main", mixinStandardHelpOptions = true, version = "v1.1",
        description = "Scrap Pisos v1.1")
public class Main implements Runnable {

    @Option(names = "--start-headless", description = "Ejecutar automáticamente en modo headless (requiere archivo de configuración con --configFile)")
    boolean headless;

    @Option(names = "--config-file", description = "Ruta al archivo de configuración")
    String configFile;

    @Option(names = "--cache-all", description = "Ejecuta el bot si el modo polling está a modo libre. Ejecuta el fetch, cachea pero NO notifica a telegram.")
    boolean fetchAndCache;

    @Option(names = "--fetch-new", description = "Ejecuta el bot si el modo polling está a modo libre. Ejecuta el fetch, y notifica.")
    boolean fetchAndNotify;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        Logger.setLevel(Logger.DEBUG);
        if (headless) {
            if (configFile == null || configFile.isEmpty()) {
                System.out.println("Error: --configFile es obligatorio cuando se usa --headless.");
            } else {
                if (fetchAndNotify && fetchAndCache){
                    System.out.println("Solo puedes ejecutar o --cache-all o --fetch-new, los dos a la vez no es posible.");
                    System.exit(-1);
                }
                if (!fetchAndNotify && !fetchAndCache){
                    System.out.println("Debes indicar --cache-all o --fetch-new. al menos uno de los dos.");
                    System.exit(-1);
                }

                System.out.println("Modo headless activado con archivo de configuración: " + configFile);
                File file = new File(configFile);
                if (!file.exists()){
                    System.out.println("Fichero no encontrado: " + configFile);
                    System.exit(-1);
                }
                StartManager.startHeadlessWithConfigFile(configFile, fetchAndCache, fetchAndNotify);
            }
        } else {
            System.out.println("Ejecutando en modo GUI.");
            StartManager.startWithGUI();
        }
    }
}
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

    @Option(names = "--configFile", description = "Ruta al archivo de configuración")
    String configFile;

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
                System.out.println("Modo headless activado con archivo de configuración: " + configFile);
                File file = new File(configFile);
                if (!file.exists()){
                    System.out.println("Fichero no encontrado: " + configFile);
                    System.exit(-1);
                }
                StartManager.startHeadlessWithConfigFile(configFile);
            }
        } else {
            System.out.println("Ejecutando en modo GUI.");
            StartManager.startWithGUI();
        }
    }
}
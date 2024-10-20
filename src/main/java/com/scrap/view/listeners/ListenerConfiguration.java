package com.scrap.view.listeners;

import com.scrap.lib.ConfigFile;

import java.io.File;

public interface ListenerConfiguration {
    void onSaveAndStart(ConfigFile configFile);

    void onExportFile(ConfigFile configFile, String file);

    void choosedJsonFile(File path);
}

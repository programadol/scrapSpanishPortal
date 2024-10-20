package com.scrap.lib;

import com.google.gson.annotations.Expose;
import com.scrap.Main;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigFile {
    @Expose
    private Boolean sendFirst = false;
    @Expose
    private String telegram_bot_token;
    @Expose
    private String telegram_notify_user;
    @Expose
    private String telegram_bot_username;
    @Expose
    private int mode_polling = 0; //0 random // 1 polling millisecond //2 cronjob
    @Expose
    private String userinput_polling;

    private File filePath;

    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }
    @Expose
    private List<Endpoint> endpoints = new ArrayList<>();

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public String getTelegram_bot_token() {
        return telegram_bot_token;
    }

    public void setTelegram_bot_token(String telegram_bot_token) {
        this.telegram_bot_token = telegram_bot_token;
    }

    public String getTelegram_notify_user() {
        return telegram_notify_user;
    }

    public void setTelegram_notify_user(String telegram_notify_user) {
        this.telegram_notify_user = telegram_notify_user;
    }

    public Boolean getSendFirst() {
        return sendFirst;
    }

    public void setSendFirst(Boolean sendFirst) {
        this.sendFirst = sendFirst;
    }

    public String getTelegram_bot_username() {
        return telegram_bot_username;
    }

    public void setTelegram_bot_username(String telegram_bot_username) {
        this.telegram_bot_username = telegram_bot_username;
    }

    public static String getFolderConfigFile(){
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();

        String osFamily = os.getFamily().toLowerCase();

        switch (osFamily) {
            case "windows":
                return System.getenv("APPDATA") + File.separator + "scraper" + File.separator;
            default:
                return "." + File.separator + "scraper" + File.separator;
        }
    }

    public static String getDefaultFileConfigString(){
        return ConfigFile.getFolderConfigFile() + "configFile.json";
    }

    public static File getDefaultFileConfig(){
        return new File(getDefaultFileConfigString());
    }

    public int getMode_polling() {
        return mode_polling;
    }

    public void setMode_polling(int mode_polling) {
        this.mode_polling = mode_polling;
    }

    public String getUserinput_polling() {
        return userinput_polling;
    }

    public void setUserinput_polling(String userinput_polling) {
        this.userinput_polling = userinput_polling;
    }
}


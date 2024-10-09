package com.scrap.lib;

import java.util.ArrayList;
import java.util.List;

public class ConfigFile {

    private Boolean sendFirst = false;

    private String telegram_bot_token;

    private String telegram_notify_user;

    private String telegram_bot_username;

    private Long pollingInternalMilliseconds;

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

    public Long getPollingInternalMilliseconds() {
        return pollingInternalMilliseconds;
    }

    public void setPollingInternalMilliseconds(Long pollingInternalMilliseconds) {
        this.pollingInternalMilliseconds = pollingInternalMilliseconds;
    }

    public String getTelegram_bot_username() {
        return telegram_bot_username;
    }

    public void setTelegram_bot_username(String telegram_bot_username) {
        this.telegram_bot_username = telegram_bot_username;
    }
}


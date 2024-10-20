package com.scrap.lib;

import com.google.gson.annotations.Expose;

import java.util.Map;

public class Endpoint {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String type;
    @Expose
    private Map<String, String> params;
    @Expose
    private Map<String, String> headers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}

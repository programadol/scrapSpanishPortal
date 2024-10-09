package com.scrap.lib.spanishportal.coreapi;

import java.util.List;

public class SpanishPortalDetailResponse {
    private String result;
    private String message;
    private String code;
    private List<SpanishPortalDetailsFieldError> fieldErrors;
    private List<String> globalErrors;
    private SpanishPortalDetailsData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SpanishPortalDetailsFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<SpanishPortalDetailsFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<String> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(List<String> globalErrors) {
        this.globalErrors = globalErrors;
    }

    public SpanishPortalDetailsData getData() {
        return data;
    }

    public void setData(SpanishPortalDetailsData data) {
        this.data = data;
    }
}


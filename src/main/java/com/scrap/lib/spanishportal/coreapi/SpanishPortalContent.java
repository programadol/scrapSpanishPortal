package com.scrap.lib.spanishportal.coreapi;

import java.util.List;

public class SpanishPortalContent {
    private String type;
    private List<SpanishPortalDetailsImage> content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SpanishPortalDetailsImage> getContent() {
        return content;
    }

    public void setContent(List<SpanishPortalDetailsImage> content) {
        this.content = content;
    }
}

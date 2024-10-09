package com.scrap.lib.spanishportal.coreapi;

import java.util.List;

public class SpanishPortalDetailsData {
    private List<SpanishPortalDetailsItem> items;
    private String groupType;

    public List<SpanishPortalDetailsItem> getItems() {
        return items;
    }

    public void setItems(List<SpanishPortalDetailsItem> items) {
        this.items = items;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}

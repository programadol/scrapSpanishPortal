package com.scrap.lib.telegram;

import com.scrap.lib.spanishportal.coreapi.SpanishPortalResponse;

public class NewHomeMessage {
    private SpanishPortalResponse.Data.MapData.MapItem item;
    private String price;
    private String description;
    private String rooms;
    private String area;
    private String contactPhone;
    private String nameOwner;
    private String urlBrowser;

    public String getNameOwner() {
        return nameOwner;
    }

    public void setNameOwner(String nameOwner) {
        this.nameOwner = nameOwner;
    }

    public SpanishPortalResponse.Data.MapData.MapItem getItem() {
        return item;
    }

    public void setItem(SpanishPortalResponse.Data.MapData.MapItem item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getUrlBrowser() {
        return urlBrowser;
    }

    public void setUrlBrowser(String urlBrowser) {
        this.urlBrowser = urlBrowser;
    }
}

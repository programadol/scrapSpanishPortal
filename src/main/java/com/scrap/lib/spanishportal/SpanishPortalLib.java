package com.scrap.lib.spanishportal;

import com.google.gson.Gson;
import com.scrap.Constants;
import com.scrap.Main;
import com.scrap.lib.Endpoint;
import com.scrap.lib.spanishportal.coreapi.SpanishPortalDetailResponse;
import com.scrap.lib.telegram.NewHomeMessage;
import com.scrap.lib.spanishportal.coreapi.SpanishPortalResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpanishPortalLib {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static List<SpanishPortalResponse.Data.MapData.MapItem> parseIdHomes(String string, OkHttpClient client, Gson gson, Endpoint endpoint) {
        SpanishPortalResponse spanishPortalResponse = gson.fromJson(string, SpanishPortalResponse.class);
        ArrayList<SpanishPortalResponse.Data.MapData.MapItem> listReturn = new ArrayList<>();
        if (spanishPortalResponse == null || spanishPortalResponse.getData() == null || spanishPortalResponse.getData().getMap() == null) return listReturn;
        for (SpanishPortalResponse.Data.MapData.MapItem item : spanishPortalResponse.getData().getMap().getItems()) {
            listReturn.add(item);
        }
        return listReturn;
    }

    public static NewHomeMessage requestDetails(Long idHome, OkHttpClient client, Gson gson, Endpoint endpoint, SpanishPortalResponse.Data.MapData.MapItem item) {
        try {
            Thread.sleep(humanWait());
        } catch (InterruptedException e) {
            logger.log(Level.DEBUG,"Error wait requestDetails concretamente client.newCall(requestDetails).execute(), errores: " + e.getMessage());
        }
        Request.Builder reqBuilderSpanishPortal = new Request.Builder()
                .url("https://www." + endpoint.getType() + ".com/" + Constants.DETAILS_SUFFIX_REQUEST + idHome)
                .method("GET", null);

        endpoint.getHeaders().forEach(reqBuilderSpanishPortal::addHeader);

        Request reqDetailsSpanishPortal = reqBuilderSpanishPortal.build();
        try (Response responseDetailsSpanishPortal = client.newCall(reqDetailsSpanishPortal).execute()) {
            SpanishPortalDetailResponse detailsResponseSPortal = gson.fromJson(responseDetailsSpanishPortal.body().string(), SpanishPortalDetailResponse.class);

            NewHomeMessage newHomeMessage = new NewHomeMessage();
            newHomeMessage.setItem(item);
            if (detailsResponseSPortal.getData() != null) {
                newHomeMessage.setDescription(detailsResponseSPortal.getData().getItems().getFirst().getDescription());
                newHomeMessage.setPrice(detailsResponseSPortal.getData().getItems().getFirst().getPrice());
                newHomeMessage.setArea(detailsResponseSPortal.getData().getItems().getFirst().getArea());
                newHomeMessage.setContactPhone(detailsResponseSPortal.getData().getItems().getFirst().getContactPhone());
                newHomeMessage.setRooms(detailsResponseSPortal.getData().getItems().getFirst().getRooms());
                if (
                        detailsResponseSPortal.getData().getItems() != null &&
                                detailsResponseSPortal.getData().getItems().getFirst() != null &&
                                detailsResponseSPortal.getData().getItems().getFirst().getAgency() != null

                ) {
                    newHomeMessage.setNameOwner(detailsResponseSPortal.getData().getItems().getFirst().getAgency().getName());
                } else {
                    newHomeMessage.setNameOwner("");
                }
            } else {
                return null;
            }
            newHomeMessage.setUrlBrowser("https://" + endpoint.getType() + ".com" + detailsResponseSPortal.getData().getItems().getFirst().getUrl());
            return newHomeMessage;

        } catch (Exception e) {
            logger.log(Level.DEBUG,"Error requestDetails concretamente client.newCall(requestDetails).execute(), errores: " + e.getMessage());
        }
        return null;
    }

    private static int humanWait() {
        Random random = new Random();
        int min = 2000;
        int max = 15000;

        return random.nextInt(max - min + 1) + min;
    }
}

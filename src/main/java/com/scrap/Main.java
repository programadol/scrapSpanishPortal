package com.scrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scrap.lib.ConfigFile;
import com.scrap.lib.Endpoint;
import com.scrap.lib.cache.IDCache;
import com.scrap.lib.log.Logger;
import com.scrap.lib.telegram.MessageGenerator;
import com.scrap.lib.telegram.TelegramImpl;
import com.scrap.lib.telegram.NewHomeMessage;
import com.scrap.lib.spanishportal.SpanishPortalLib;
import com.scrap.lib.spanishportal.coreapi.SpanishPortalResponse;
import com.scrap.view.listeners.ListenerConfiguration;
import com.scrap.view.views.LiveControlView;
import com.scrap.view.views.StartConfigView;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final HashMap<String, IDCache> idCacheArr = new HashMap<>();
    private static ConfigFile config;
    private static TelegramImpl telegramImpl;

    public static void main(String[] args) {
        loadConfigFile();

        StartConfigView.setListenerConfiguration(new ListenerConfiguration() {
            @Override
            public void onSaveAndStart(ConfigFile configFile) {
                saveConfigFile(configFile);
                Platform.runLater(() -> new LiveControlView().start(new Stage()));
                loadConfigFile();
                startBot();
            }

            @Override
            public void choosedJsonFile(File file) {
                copyJsonFile(file);
                loadConfigFile();
            }
        });

        StartConfigView.launch(StartConfigView.class, args);
    }

    private static void startBot() {
        new Thread(() -> {
            initializeTelegramBot(config.getTelegram_bot_token(), config.getTelegram_notify_user(), config.getTelegram_bot_username());

            config.getEndpoints().forEach(endpoint -> {
                try {
                    telegramImpl.sendMessageToUserId(MessageGenerator.generateEndpointLoading(endpoint));
                } catch (Exception e) {
                    handleBotError("Error sending message to user id... ", e);
                }
                idCacheArr.put(endpoint.getId(), new IDCache(endpoint.getId()));
                processEndpoint(endpoint, true);
                try {
                    telegramImpl.sendMessageToUserId(MessageGenerator.generateFirstTracking(endpoint, idCacheArr.get(endpoint.getId()).getCachedIDs().size()));
                } catch (Exception e) {
                    handleBotError("Error sending message to user id... ", e);
                }
            });

            while (true) {
                try {
                    Thread.sleep(config.getPollingInternalMilliseconds());
                } catch (Exception e) {
                    handleBotError("Error gestionando el polling (hilo), abortando por posibles errores...", e);
                    System.exit(-1);
                }
                config.getEndpoints().forEach(endpoint -> processEndpoint(endpoint, false));
            }
        }).start();
    }

    private static void loadConfigFile() {
        File configFile = new File(System.getenv("APPDATA") + "/scraper/configFile.json");
        if (configFile.isFile()) {
            try (InputStream input = new FileInputStream(configFile)) {
                Gson gson = new Gson();
                String jsonText = IOUtils.toString(input, StandardCharsets.UTF_8);
                config = gson.fromJson(jsonText, ConfigFile.class);
                StartConfigView.setConfigFile(config);
            } catch (Exception e) {
                Logger.log("Error cargando archivo de configuración. Posiblemente no exista.");
            }
        }
    }

    private static void saveConfigFile(ConfigFile configFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        File outputDir = new File(System.getenv("APPDATA") + "/scraper/");
        outputDir.mkdirs();

        try (FileWriter writer = new FileWriter(new File(outputDir, "configFile.json"))) {
            gson.toJson(configFile, writer);
        } catch (Exception e) {
            Logger.log("Error guardando archivo de configuración. ¿Permisos? ¿Algo hiciste mal?");
        }
    }

    private static void copyJsonFile(File file) {
        File outputDir = new File(System.getenv("APPDATA") + "/scraper/");
        outputDir.mkdirs();
        try {
            Files.copy(file.toPath(), new File(outputDir, "configFile.json").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            Logger.log("Error importando archivo de configuración. ¿Permisos? ¿Algo hiciste mal?");
        }
    }

    private static void initializeTelegramBot(String token, String notifyUser, String username) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramImpl = new TelegramImpl(token, notifyUser, username);
            botsApi.registerBot(telegramImpl);
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            telegramImpl.sendMessageToUserId("<code>" + date + ": STARTING BOT TRACKER SYSTEM</code>");
        } catch (Exception e) {
            handleBotError("Error inicializando el sistema de envío de mensajes de telegram. Posiblemente timeout o algún error con el bot.", e);
        }
    }

    private static void processEndpoint(Endpoint endpoint, boolean firstStartup) {
        /*

         */
        String url = "https://www." + endpoint.getType() + ".com" + Constants.SUFFIX_AJAX_LIVEMAPGROUNDED + "?" + endpoint.getParams().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue().replace(" ", ""))
                .collect(Collectors.joining("&"));

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .method("GET", null);
        endpoint.getHeaders().forEach(requestBuilder::addHeader);

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                handleSpanishPortalResponse(endpoint, response.body().string(), client, firstStartup);
            }
        } catch (Exception e) {
            handleBotError("Error llamando al tracker (URL), posiblemente haya algún error con la respuesta o el portal está capando/bloqueando solicitudes. Mensaje de error devuelto:", e);
        }
    }

    private static void handleSpanishPortalResponse(Endpoint endpoint, String responseBody, OkHttpClient client, boolean firstStartup) {
        Gson gson = new GsonBuilder().create();
        try {
            List<SpanishPortalResponse.Data.MapData.MapItem> list = SpanishPortalLib.parseIdHomes(responseBody, client, gson, endpoint);
            for (SpanishPortalResponse.Data.MapData.MapItem item : list) {
                long idHome = item.getAds().get(0).getAdId();
                if (!idCacheArr.get(endpoint.getId()).isIDSeenBefore(String.valueOf(idHome))) {
                    idCacheArr.get(endpoint.getId()).addID(String.valueOf(idHome));
                    if (!firstStartup) {
                        LiveControlView.aumentarAnunciosEncontrados();
                        sendNewHomeNotification(endpoint, client, gson, item, idHome);
                    }
                }
            }
        } catch (Exception e) {
            handleBotError("Ha habido un problema leyendo la respuesta del tracker. También es posible que haya algún error a la hora de enviar el mensaje de notificación.", e);
        }
    }

    private static void sendNewHomeNotification(Endpoint endpoint, OkHttpClient client, Gson gson, SpanishPortalResponse.Data.MapData.MapItem item, long idHome) {
        try {
            NewHomeMessage newHomeMessage = SpanishPortalLib.requestDetails(idHome, client, gson, endpoint, item);
            if (newHomeMessage != null) {
                String messageNewHome = MessageGenerator.generateNewHome(newHomeMessage, endpoint);
                ReplyKeyboard replyMarkup = MessageGenerator.generateReplyMarkup(newHomeMessage.getUrlBrowser());
                telegramImpl.sendLocation(item.getLatitude(), item.getLongitude());
                if (!newHomeMessage.getNameOwner().isBlank()) {
                    telegramImpl.sendContact("+34" + newHomeMessage.getContactPhone(), newHomeMessage.getNameOwner());
                }
                telegramImpl.sendMessageToUserId(messageNewHome, replyMarkup);
            } else {
                telegramImpl.sendMessageToUserId("Error fetching " + idHome + " is null... see trace logs");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleBotError("Ha habido un error enviando el mensaje por telegram del ID: " + idHome + " apiResponse de telegram:" + ((TelegramApiRequestException) e).getApiResponse(), e);
        }
    }

    private static void handleBotError(String message, Exception e) {
        String loggerMsgError = message + " " + e.getMessage();
        Logger.log(loggerMsgError);
        try {
            telegramImpl.sendMessageToUserId(loggerMsgError);
        } catch (Exception ex) {
            Logger.log("Error enviando mensaje de telegram " + " " + e.getMessage());
        }
    }
}
package com.scrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scrap.lib.ConfigFile;
import com.scrap.lib.Endpoint;
import com.scrap.lib.cache.IDCache;
import com.scrap.lib.telegram.MessageGenerator;
import com.scrap.lib.telegram.TelegramImpl;
import com.scrap.lib.telegram.NewHomeMessage;
import com.scrap.lib.spanishportal.SpanishPortalLib;
import com.scrap.lib.spanishportal.coreapi.SpanishPortalResponse;
import com.scrap.view.listeners.ListenerConfiguration;
import com.scrap.view.views.LiveControlView;
import com.scrap.view.views.StartConfigView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
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

public class StartManager {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final HashMap<String, IDCache> idCacheArr = new HashMap<>();
    private static ConfigFile config;
    private static TelegramImpl telegramImpl;

    public static boolean startedWithGUI = false;

    public static void startWithGUI() {
        startedWithGUI = true;
        loadConfigFile();

        StartConfigView.setListenerConfiguration(new ListenerConfiguration() {
            @Override
            public void onSaveAndStart(ConfigFile configFile) {
                new File(configFile.getFilePath().getParent()).mkdirs();
                saveConfigFile(configFile, configFile.getFilePath().getPath());
                if (configFile.getMode_polling() == 3){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alerta");
                    alert.setHeaderText(null);
                    alert.setContentText("Se han guardado los cambios, pero el modo libre está activado, por lo que el BOT deberás ejecutarlo con los parámetros --start-headless, --config-file, --fetch-and-cache y --fetch-and-notify.");
                    alert.showAndWait();
                }else{
                    Platform.runLater(() -> new LiveControlView().start(new Stage()));
                    loadConfigFile();
                    startBotGUI();
                }
            }

            @Override
            public void onExportFile(ConfigFile configFile, String fileToExport) {
                saveConfigFile(configFile, fileToExport);
            }

            @Override
            public void choosedJsonFile(File file) {
                copyJsonFile(file);
                loadConfigFile();
            }
        });

        StartConfigView.launch(StartConfigView.class);
    }

    public static void startHeadlessWithConfigFile(String pathConfigFile, boolean fetchAndCache, boolean fetchAndNotify) {
        loadConfigFile(pathConfigFile);
        initializeTelegramBot(config.getTelegram_bot_token(), config.getTelegram_notify_user(), config.getTelegram_bot_username(), fetchAndCache);
        if (fetchAndCache){
            cacheAll();
        }
        if (fetchAndNotify){
            preloadFastCache();
            executeBot();
        }
    }

    private static void startBotGUI() {
        Thread thread = new Thread(() -> {
            initializeTelegramBot(config.getTelegram_bot_token(), config.getTelegram_notify_user(), config.getTelegram_bot_username(), true);
            cacheAll();
            executeBot();
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void executeBot() {
        switch (config.getMode_polling()){
            case 0:
            case 1:
                while (true) {
                    try {
                        switch(config.getMode_polling()){
                            case 0:
                                waitIntervalRandom();
                                break;
                            case 1:
                                waitConfiguredMinutes();
                                break;
                        }
                    } catch (Exception e) {
                        handleBotError("Error gestionando el polling (hilo), abortando por posibles errores...", e);
                        System.exit(-1);
                    }
                    logger.log(Level.DEBUG, "Polling reached, processing endpoints...");
                    config.getEndpoints().forEach(endpoint -> processEndpoint(endpoint, false));
                }
            case 2:
                scheduleCronjob();
                while (true) {
                    try {Thread.sleep(1000);} catch (Exception e) {}
                }
            case 3:
                config.getEndpoints().forEach(endpoint -> processEndpoint(endpoint, false));
                break;
        }
    }

    private static void preloadFastCache() {
        config.getEndpoints().forEach(endpoint -> {
            String idUnique = endpoint.getType() + "_" + endpoint.getId();
            idCacheArr.put(idUnique, new IDCache(idUnique, config.getFilePath().getParent()));
        });
    }

    private static void cacheAll() {
        config.getEndpoints().forEach(endpoint -> {
            try {
                telegramImpl.sendMessageToUserId(MessageGenerator.generateEndpointLoading(endpoint));
            } catch (Exception e) {
                handleBotError("Error sending message to user id... ", e);
            }
            String idUnique = endpoint.getType() + "_" + endpoint.getId();
            idCacheArr.put(idUnique, new IDCache(idUnique, config.getFilePath().getParent()));
            processEndpoint(endpoint, true);
            try {
                telegramImpl.sendMessageToUserId(MessageGenerator.generateFirstTracking(endpoint, idCacheArr.get(idUnique).getCachedIDs().size()));
            } catch (Exception e) {
                handleBotError("Error sending message to user id... ", e);
            }
        });
    }

    public static class CronjobWorker implements Job {
        @Override
        public void execute(JobExecutionContext context){
            logger.log(Level.DEBUG, "Cronjob reached, processing endpoints...");
            config.getEndpoints().forEach(endpoint -> processEndpoint(endpoint, false));
        }
    }

    private static void scheduleCronjob() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDetail job = JobBuilder.newJob(CronjobWorker.class)
                    .withIdentity("CronjobWorker", "CronjobWorker")
                    .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.getUserinput_polling()))
                    .build();

            scheduler.scheduleJob(job, trigger);

        } catch (Exception e) {
            handleBotError("Error en el cronjob", e);
        }
    }

    private static void waitIntervalRandom() throws InterruptedException {
        int min = 5*1000*60;
        int max = 120*1000*60;
        Thread.sleep((int) (Math.random() * (max - min + 1)) + min);
    }

    private static void waitConfiguredMinutes() throws InterruptedException {
        Thread.sleep(Long.parseLong(config.getUserinput_polling())*1000*60);
    }

    private static void loadConfigFile() {
        loadConfigFile(ConfigFile.getDefaultFileConfig().toString());
    }

    private static int loadConfigFile(String configPathFile) {
        File configFile = new File(configPathFile);
        if (configFile.isFile()) {
            try (InputStream input = new FileInputStream(configFile)) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().disableHtmlEscaping().create();
                String jsonText = IOUtils.toString(input, StandardCharsets.UTF_8);
                config = gson.fromJson(jsonText, ConfigFile.class);
                config.setFilePath(new File(configPathFile));
                if (startedWithGUI) StartConfigView.setConfigFile(config);
                return 1;
            } catch (Exception e) {
                logger.log(Level.DEBUG,"Error cargando archivo de configuración. Posiblemente no exista.");
                return -1;
            }
        }else{
            logger.log(Level.DEBUG, "File " + configPathFile + " no encontrado");
            return -1;
        }
    }

    private static void saveConfigFile(ConfigFile configFile, String outputFile) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().disableHtmlEscaping().create();
        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(configFile, writer);
        } catch (Exception e) {
            logger.log(Level.DEBUG,"Error guardando archivo de configuración. ¿Permisos? ¿Algo hiciste mal?");
        }
    }

    private static void copyJsonFile(File file) {
        File outputFile = ConfigFile.getDefaultFileConfig();
        outputFile.mkdirs();
        try {
            Files.copy(file.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.log(Level.DEBUG,"Error importando archivo de configuración. ¿Permisos? ¿Algo hiciste mal?");
        }
    }

    private static void initializeTelegramBot(String token, String notifyUser, String username, boolean sendInitialMessage) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramImpl = new TelegramImpl(token, notifyUser, username);
            botsApi.registerBot(telegramImpl);
            if (sendInitialMessage){
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                telegramImpl.sendMessageToUserId("<code>" + date + ": STARTING BOT TRACKER SYSTEM</code>");
            }
        } catch (Exception e) {
            handleBotError("Error inicializando el sistema de envío de mensajes de telegram. Posiblemente timeout o algún error con el bot.", e);
        }
    }

    private static void processEndpoint(Endpoint endpoint, boolean firstStartup) {
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
                String idUnique = endpoint.getType() + "_" + endpoint.getId();
                if (!idCacheArr.get(idUnique).isIDSeenBefore(String.valueOf(idHome))) {
                    idCacheArr.get(idUnique).addID(String.valueOf(idHome));
                    if (!firstStartup) {
                        if (startedWithGUI) LiveControlView.aumentarAnunciosEncontrados();
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
            handleBotError("Ha habido un error enviando el mensaje por telegram del ID: " + idHome + " apiResponse de telegram:" + ((TelegramApiRequestException) e).getApiResponse(), e);
        }
    }

    private static void handleBotError(String message, Exception e) {
        e.printStackTrace();
        String loggerMsgError = message + " " + e.getMessage();
        logger.log(Level.DEBUG, loggerMsgError);
        try {
            telegramImpl.sendMessageToUserId(loggerMsgError);
        } catch (Exception ex) {
            logger.log(Level.DEBUG,"Error enviando mensaje de telegram " + " " + e.getMessage());
        }
    }
}
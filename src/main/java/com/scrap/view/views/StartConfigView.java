package com.scrap.view.views;

import com.scrap.Main;
import com.scrap.lib.ConfigFile;
import com.scrap.lib.Endpoint;
import com.scrap.view.listeners.ListenerConfiguration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class StartConfigView extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static ConfigFile configFile;
    private static ListenerConfiguration listenerConfiguration;
    private static Stage primaryStage;

    public static void setListenerConfiguration(ListenerConfiguration listener) {
        listenerConfiguration = listener;
    }

    static {
        configFile = new ConfigFile();
        configFile.setFilePath(ConfigFile.getDefaultFileConfig());
    }

    public static void setConfigFile(ConfigFile config) {
        configFile = config;
    }

    public static void close() {
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        loadConfigurationFileAndGUI();
        primaryStage.show();
    }

    private void loadConfigurationFileAndGUI() {
        BorderPane root = new BorderPane();

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(
                buildTelegramConfigSection(),
                buildTrackersConfigSection()
        );

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);

        HBox footer = new HBox(10);
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER_RIGHT);

        Button saveButton = createButton("✔ GUARDAR & ARRANCAR BOT", e -> guardarConfiguracion(), "#2E7D32");
        Button importButton = createButton("\uD83D\uDCE4 IMPORTAR CONFIG", e -> importJsonDialog(), "#1565C0");
        Button exportButton = createButton("\uD83D\uDCE4 EXPORTAR CONFIG", e -> exportJsonDialog(), "#1565C0");
        Button openFolderBot = createButton("\uD83D\uDCC2 ABRIR CONFIG", e -> openFolderBot(), "#1565C0");

        footer.getChildren().addAll(openFolderBot, exportButton, importButton, saveButton);

        root.setCenter(scrollPane);
        root.setBottom(footer);

        Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configuración de Parámetros");
    }

    private VBox buildTelegramConfigSection() {
        VBox telegramConfigSection = new VBox(10);
        telegramConfigSection.setAlignment(Pos.TOP_CENTER);

        TextField botTokenField = createTextField(configFile.getTelegram_bot_token(), 400);
        TextField botUsernameField = createTextField(configFile.getTelegram_bot_username(), 400);
        TextField notifyUserField = createTextField(configFile.getTelegram_notify_user(), 400);

        botTokenField.textProperty().addListener((obs, oldVal, newVal) -> configFile.setTelegram_bot_token(newVal));
        botUsernameField.textProperty().addListener((obs, oldVal, newVal) -> configFile.setTelegram_bot_username(newVal));
        notifyUserField.textProperty().addListener((obs, oldVal, newVal) -> configFile.setTelegram_notify_user(newVal));


        ToggleGroup pollingModeGroup = new ToggleGroup();

        RadioButton randomModeButton = new RadioButton("Random (5min-300min)");
        randomModeButton.setToggleGroup(pollingModeGroup);
        randomModeButton.setUserData(0);
        randomModeButton.setSelected(true);

        RadioButton pollingMillisecondsButton = new RadioButton("Every X minutes");
        pollingMillisecondsButton.setToggleGroup(pollingModeGroup);
        pollingMillisecondsButton.setUserData(1);

        RadioButton cronjobModeButton = new RadioButton("Cronjob");
        cronjobModeButton.setToggleGroup(pollingModeGroup);
        cronjobModeButton.setUserData(2);

        TextField userInputText = new TextField();
        userInputText.setPromptText("");
        userInputText.setVisible(false);

        userInputText.textProperty().addListener((obs, oldVal, newVal) -> {
            configFile.setUserinput_polling(newVal);
        });

        Label labelBeforeInputPolling = new Label("");
        labelBeforeInputPolling.setVisible(false);

        switch (configFile.getMode_polling()){
            case 0:
                randomModeButton.setSelected(true);
                setRadioRandom(userInputText, labelBeforeInputPolling);
                break;
            case 1:
                pollingMillisecondsButton.setSelected(true);
                setRadioEveryXMinutes(userInputText, labelBeforeInputPolling);
                break;
            case 2:
                cronjobModeButton.setSelected(true);
                setRadioCronJob(userInputText, labelBeforeInputPolling);
                break;
        }
        userInputText.setText(configFile.getUserinput_polling());

        pollingModeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int mode_polling = (int) newValue.getUserData();
                switch (mode_polling) {
                    case 0: // Random
                        setRadioRandom(userInputText, labelBeforeInputPolling);
                        configFile.setMode_polling(0);
                        break;
                    case 1: // Polling Milliseconds
                        setRadioEveryXMinutes(userInputText, labelBeforeInputPolling);
                        configFile.setMode_polling(1);
                        break;
                    case 2: // Cronjob
                        setRadioCronJob(userInputText, labelBeforeInputPolling);
                        configFile.setMode_polling(2);
                        break;
                }
            }
        });

        HBox hBoxRadios = new HBox();
        hBoxRadios.setAlignment(Pos.CENTER);
        hBoxRadios.setMaxWidth(primaryStage.getMaxWidth());
        hBoxRadios.getChildren().addAll(randomModeButton,
                pollingMillisecondsButton,
                cronjobModeButton);

        telegramConfigSection.getChildren().addAll(
                createLabel("Configuración de Telegram", 18, true),
                createLabel("Telegram Bot Token (Generado por @BotFather):"), botTokenField,
                createLabel("Telegram Bot Username (Generado por @BotFather):"), botUsernameField,
                createLabel("Telegram Notify User ID (Extráelo de @getmy_idbot):"), notifyUserField,
                createLabel("Modo actualización"),
                hBoxRadios,
                labelBeforeInputPolling,
                userInputText
        );

        return telegramConfigSection;
    }

    public void setRadioRandom(TextField userInputText, Label labelBeforeInputPolling){
        userInputText.setVisible(false);
        labelBeforeInputPolling.setVisible(false);
    }

    public void setRadioEveryXMinutes(TextField userInputText, Label labelBeforeInputPolling){
        userInputText.setVisible(true);
        userInputText.setText("");
        userInputText.setPromptText("Actualización en minutos");
        labelBeforeInputPolling.setVisible(true);
        labelBeforeInputPolling.setText("Actualización en minutos");
    }

    public void setRadioCronJob(TextField userInputText, Label labelBeforeInputPolling){
        userInputText.setVisible(true);
        userInputText.setText("");
        userInputText.setPromptText("Cronjob String");
        labelBeforeInputPolling.setVisible(true);
        labelBeforeInputPolling.setText("Cronjob String");
    }

    private VBox buildTrackersConfigSection() {
        VBox trackerConfigSection = new VBox(10);
        trackerConfigSection.setAlignment(Pos.TOP_CENTER);

        Accordion endpointAccordion = new Accordion();
        endpointAccordion.setMaxWidth(800);
        cargarEndpointsDesdeConfig(endpointAccordion);

        Button addTrackerButton = createButton("+ Tracker", e -> agregarNuevoEndpoint(endpointAccordion));

        trackerConfigSection.getChildren().addAll(
                createLabel("Configuración de portales", 18, true),
                new HBox(addTrackerButton),
                endpointAccordion
        );

        return trackerConfigSection;
    }

    private void importJsonDialog() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            listenerConfiguration.choosedJsonFile(selectedFile);
            loadConfigurationFileAndGUI();
        }
    }

    private void exportJsonDialog() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("configFile.json");
        File file = fileChooser.showSaveDialog(primaryStage);
        if(file != null){
            listenerConfiguration.onExportFile(configFile, file.toString());
        }
    }

    private void openFolderBot() {
        try {
            File outputDir = new File(configFile.getFilePath().getParent());
            outputDir.mkdirs();
            Desktop.getDesktop().open(outputDir);
        } catch (IOException e) {
            logger.log(Level.DEBUG,"Error abriendo la carpeta de bot: " + e.getMessage());
        }
    }

    private void cargarEndpointsDesdeConfig(Accordion accordion) {
        List<Endpoint> endpoints = configFile.getEndpoints();
        if (endpoints != null) {
            for (Endpoint endpoint : endpoints) {
                accordion.getPanes().add(crearPanelDeEndpoint(endpoint, accordion));
            }
        }
    }

    private void agregarNuevoEndpoint(Accordion accordion) {
        Endpoint nuevoEndpoint = new Endpoint();
        nuevoEndpoint.setParams(createDefaultParams());
        nuevoEndpoint.setHeaders(createDefaultHeaders());

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(c -> (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
                .build();
        String randomLetters = generator.generate(20);

        nuevoEndpoint.setId(randomLetters);

        configFile.getEndpoints().add(nuevoEndpoint);
        accordion.getPanes().add(crearPanelDeEndpoint(nuevoEndpoint, accordion));
    }

    private TitledPane crearPanelDeEndpoint(Endpoint endpoint, Accordion accordion) {
        VBox endpointContent = new VBox(10);
        endpointContent.setPadding(new Insets(10));
        endpointContent.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 5px;");

        TextField endpointNameField = createTextField(endpoint.getName(), 300, "Para saber en el bot cuál estás ejecutando");
        endpointNameField.textProperty().addListener((obs, oldVal, newVal) -> endpoint.setName(newVal));

        TextField endpointTypeField = createTextField(endpoint.getType(), 300, "Portal inmobiliario");
        endpointTypeField.textProperty().addListener((obs, oldVal, newVal) -> endpoint.setType(newVal));

        Label areaChosenNorth = new Label(getNorthEastStr(endpoint));
        Label areaChosenSouth = new Label(getSouthWestStr(endpoint));
        Label zoomChosen = new Label(getZoomStr(endpoint));
        Button chooseMapButton = createButton("\uD83D\uDCCD Seleccionar área en el mapa", e -> openMapView(endpoint, areaChosenNorth, areaChosenSouth, zoomChosen));

        VBox filtros = createFilterSection(endpoint);

        Button deleteButton = createButton("Eliminar", e -> eliminarEndpoint(accordion, endpoint, endpointContent), "red");

        endpointContent.getChildren().addAll(
                createLabel("Descripción: "), endpointNameField,
                createLabel("Portal inmobiliario: "), endpointTypeField,
                chooseMapButton,
                areaChosenNorth, areaChosenSouth, zoomChosen,
                filtros, deleteButton
        );

        TitledPane titledPane = new TitledPane("Tracker: " + (endpoint.getName() != null ? endpoint.getName() : "Nuevo Endpoint"), endpointContent);
        titledPane.setStyle("-fx-font-size: 14px;");
        return titledPane;
    }

    private void openMapView(Endpoint endpoint, Label areaNorth, Label areaSouth, Label zoomLabel) {
        Platform.runLater(() -> {
            MapView mapView = new MapView();
            if (endpoint.getParams().get("northEast") != null && endpoint.getParams().get("southWest") != null) {
                mapView.preselectCoordinatesAndZoom(endpoint.getParams().get("northEast"), endpoint.getParams().get("southWest"), endpoint.getParams().get("zoom"));
            }

            mapView.setListenerMapView((northEast, southWest, zoom) -> {
                updateEndpointArea(endpoint, northEast, southWest, zoom);
                areaNorth.setText(getNorthEastStr(endpoint));
                areaSouth.setText(getSouthWestStr(endpoint));
                zoomLabel.setText(getZoomStr(endpoint));
                mapView.preselectCoordinatesAndZoom(endpoint.getParams().get("northEast"), endpoint.getParams().get("southWest"), endpoint.getParams().get("zoom"));
            });
            mapView.start(new Stage());
        });
    }

    private void updateEndpointArea(Endpoint endpoint, String northEast, String southWest, String zoom) {
        endpoint.getParams().put("northEast", northEast);
        endpoint.getParams().put("southWest", southWest);
        endpoint.getParams().put("zoom", zoom.replace(".0", ""));
    }

    private VBox createFilterSection(Endpoint endpoint) {
        VBox filters = new VBox(10);
        for (com.scrap.lib.spanishportal.parameters.Parameters parameter : com.scrap.lib.spanishportal.parameters.Parameters.values()) {
            filters.getChildren().add(createFilterPane(parameter, endpoint));
        }
        return filters;
    }

    private TitledPane createFilterPane(com.scrap.lib.spanishportal.parameters.Parameters parameter, Endpoint endpoint) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 5px;");

        //vbox.getChildren().add(createLabel(parameter.getDisplayName()));

        switch (parameter.getControlType()) {
            case "TextField":
                TextField textField = createTextField(endpoint.getParams().getOrDefault(parameter.getKey(), "0"), 400);
                textField.textProperty().addListener((obs, oldVal, newVal) ->
                        endpoint.getParams().put(parameter.getKey(), newVal)
                );
                vbox.getChildren().add(textField);
                break;
            case "ComboBox":
                ComboBox<String> comboBox = createComboBox(parameter, endpoint);
                vbox.getChildren().add(comboBox);
                break;
            case "CheckBox":
                createCheckBoxes(parameter, endpoint, vbox);
                break;
            case "RadioButton":
                createRadioButtons(parameter, endpoint, vbox);
                break;
        }

        return new TitledPane(parameter.getDisplayName(), vbox);
    }

    private void guardarConfiguracion() {
        listenerConfiguration.onSaveAndStart(configFile);
        close();
    }

    private void eliminarEndpoint(Accordion accordion, Endpoint endpoint, VBox endpointContent) {
        accordion.getPanes().remove(endpointContent.getParent());
        TitledPane paneToRemove = null;

        for (TitledPane pane : accordion.getPanes()) {
            if (pane.getContent() == endpointContent) {
                paneToRemove = pane;
                break;
            }
        }

        if (paneToRemove != null) {
            accordion.getPanes().remove(paneToRemove);
        }

        configFile.getEndpoints().remove(endpoint);
    }

    private TextField createTextField(String text, int width, String prompt) {
        TextField textField = new TextField(text);
        textField.setPrefWidth(width);
        textField.setPromptText(prompt);
        return textField;
    }

    private TextField createTextField(String text, int width) {
        return createTextField(text, width, "");
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private Label createLabel(String text, int fontSize, boolean bold) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: " + fontSize + "px;" + (bold ? " -fx-font-weight: bold;" : ""));
        return label;
    }

    private Button createButton(String text, EventHandler<ActionEvent> handler, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 8px 15px;");
        button.setOnAction(handler);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        return button;
    }

    private Button createButton(String text, EventHandler<ActionEvent> handler) {
        return createButton(text, handler, "green");
    }


    private ComboBox<String> createComboBox(com.scrap.lib.spanishportal.parameters.Parameters parameter, Endpoint endpoint) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(parameter.getOptions());

        String optionSelected = parameter.getOptions()[0];
        String foundValue = endpoint.getParams().get(parameter.getKey());
        int i = 0;
        for (String str : parameter.getValues()) {
            if (str.equals(foundValue)) optionSelected = parameter.getOptions()[i];
            i++;
        }

        comboBox.setValue(optionSelected);
        comboBox.setOnAction(e -> endpoint.getParams().put(parameter.getKey(), parameter.getValues()[comboBox.getSelectionModel().getSelectedIndex()]));
        return comboBox;
    }

    private void createCheckBoxes(com.scrap.lib.spanishportal.parameters.Parameters parameter, Endpoint endpoint, VBox vbox) {
        for (String option : parameter.getOptions()) {
            CheckBox checkBox = new CheckBox(option);
            checkBox.setSelected(endpoint.getParams().containsKey(parameter.getKey()));
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    endpoint.getParams().put(parameter.getKey(), parameter.getValues()[0]);
                } else {
                    endpoint.getParams().remove(parameter.getKey());
                }
            });
            vbox.getChildren().add(checkBox);
        }
    }

    private void createRadioButtons(com.scrap.lib.spanishportal.parameters.Parameters parameter, Endpoint endpoint, VBox vbox) {
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String option : parameter.getOptions()) {
            RadioButton radioButton = new RadioButton(option);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setSelected(option.equals(endpoint.getParams().getOrDefault(parameter.getKey(), "")));
            radioButton.setOnAction(e -> endpoint.getParams().put(parameter.getKey(), option));
            vbox.getChildren().add(radioButton);
        }
    }

    private HashMap<String, String> createDefaultParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("liveSearch", "true");
        params.put("device", "desktop");
        params.put("adfilter_published", "4");
        params.put("operation", "2");
        params.put("tipology", "10");
        return params;
    }

    private HashMap<String, String> createDefaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json, text/javascript, */*; q=0.01");
        headers.put("accept-language", "es-ES,es;q=0.9,en;q=0.8");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, como Gecko) Chrome/124.0.0.0 Safari/537.36");
        return headers;
    }

    private String getNorthEastStr(Endpoint endpoint) {
        return "Área seleccionada northEast: " + endpoint.getParams().getOrDefault("northEast", "No seleccionado");
    }

    private String getSouthWestStr(Endpoint endpoint) {
        return "Área seleccionada southWest: " + endpoint.getParams().getOrDefault("southWest", "No seleccionado");
    }

    private String getZoomStr(Endpoint endpoint) {
        return "Zoom: " + endpoint.getParams().getOrDefault("zoom", "No seleccionado");
    }
}

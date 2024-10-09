package com.scrap.view.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LiveControlView extends Application {

    private static final SimpleIntegerProperty anunciosEncontrados = new SimpleIntegerProperty(0);
    private static final SimpleIntegerProperty timeActive = new SimpleIntegerProperty(0);  // Nueva propiedad de tiempo arrancado

    private Timeline timeline;

    public static void aumentarAnunciosEncontrados() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                anunciosEncontrados.set(anunciosEncontrados.get() + 1);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Text liveRunningText = new Text("LIVE RUNNING");
        liveRunningText.setFont(new Font("Arial", 24));
        liveRunningText.setFill(Color.GREEN);

        Circle greenCircle = new Circle(15, Color.GREEN);

        Button stopButton = new Button("X");
        stopButton.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-font-size: 36px; -fx-text-fill: red;");

        stopButton.setOnAction(e -> {
            System.out.println("Aplicación cerrada.");
            if (timeline != null) {
                timeline.stop();
            }
            Platform.exit();
        });

        Label anunciosEncontradosLabel = new Label();
        Label tiempoArrancadoLabel = new Label();

        anunciosEncontradosLabel.setFont(new Font("Arial", 18));
        tiempoArrancadoLabel.setFont(new Font("Arial", 18));

        anunciosEncontradosLabel.textProperty().bind(anunciosEncontrados.asString("Anuncios encontrados: %d"));
        timeActive.addListener((obs, oldValue, newValue) -> {
            tiempoArrancadoLabel.setText("Tiempo arrancado: " + formatTime(newValue.intValue()));
        });

        VBox statsBox = new VBox(10, anunciosEncontradosLabel, tiempoArrancadoLabel);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(20));

        HBox statusBox = new HBox(10, greenCircle, liveRunningText);
        statusBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(stopButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20));

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(20);
        root.setHgap(50);
        root.setPadding(new Insets(20));

        root.add(statsBox, 0, 0);
        root.add(statusBox, 1, 0);
        root.add(buttonBox, 2, 0);

        Scene scene = new Scene(root, 800, 300);

        primaryStage.setTitle("Bot funcionando");
        primaryStage.setScene(scene);
        primaryStage.show();

        startTimer();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeActive.set(timeActive.get() + 1);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

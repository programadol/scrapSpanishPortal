package com.scrap.view.views;

import com.scrap.view.listeners.ListenerMapView;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

public class MapView extends Application {

    private ListenerMapView listenerMapView;
    private Coordinate preselectedSouthWest;
    private Coordinate preselectedNorthEast;
    private double preselectedzoom;
    private com.sothawo.mapjfx.MapView mapView;
    private Label statusLabel;
    private CoordinateLine polygon;

    public static void main(String[] args) {
        launch(args);
    }

    public void setListenerMapView(ListenerMapView listenerMapView) {
        this.listenerMapView = listenerMapView;
    }

    public void preselectCoordinatesAndZoom(String southWest, String northEast, String zoom) {
        String[] partsSouth = southWest.split(", ");
        preselectedSouthWest = new Coordinate(Double.parseDouble(partsSouth[0]), Double.parseDouble(partsSouth[1]));

        String[] partsNorth = northEast.split(", ");
        preselectedNorthEast = new Coordinate(Double.parseDouble(partsNorth[0]), Double.parseDouble(partsNorth[1]));

        preselectedzoom = Double.parseDouble(zoom);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Selección del área de búsqueda");

        mapView = new com.sothawo.mapjfx.MapView();
        if (preselectedSouthWest != null && preselectedNorthEast != null) {
            mapView.setZoom(preselectedzoom - 1);

            double latCenter = (preselectedSouthWest.getLatitude() + preselectedNorthEast.getLatitude()) / 2;
            double lonCenter = (preselectedSouthWest.getLongitude() + preselectedNorthEast.getLongitude()) / 2;

            mapView.setCenter(new Coordinate(latCenter, lonCenter));
        } else {
            mapView.setZoom(5);
            mapView.setCenter(new Coordinate(38.044994, -1.084397)); // Madrid, donde están las mejores playas.
        }

        statusLabel = new Label("Selecciona un área con CTRL + arrastrar. Es conveniente ajustar el zoom al rectángulo.");

        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            Extent extent = event.getExtent();
            Coordinate topLeft = extent.getMin();
            Coordinate bottomRight = extent.getMax();

            statusLabel.setText(String.format("Área seleccionada: \nNoroeste: %s \nSureste: %s \nZoom: %s", topLeft, bottomRight, mapView.getZoom()));
            drawPolygon(topLeft, bottomRight);
            listenerMapView.onMapAreaChosen(
                    bottomRight.getLatitude() + ", " + bottomRight.getLongitude(),
                    topLeft.getLatitude() + ", " + topLeft.getLongitude(),
                    String.valueOf(mapView.getZoom())
            );
        });

        mapView.initialize(Configuration.builder()
                .projection(Projection.WEB_MERCATOR)
                .showZoomControls(true)
                .build());

        BorderPane root = new BorderPane();
        root.setCenter(mapView);
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        mapView.initialize();

        if (preselectedSouthWest != null && preselectedNorthEast != null) {
            Platform.runLater(() -> {
                drawPolygon(preselectedSouthWest, preselectedNorthEast);
                mapView.setZoom(preselectedzoom);
            });
        }
    }

    private void drawPolygon(Coordinate topLeft, Coordinate bottomRight) {
        Coordinate topRight = new Coordinate(topLeft.getLatitude(), bottomRight.getLongitude());
        Coordinate bottomLeft = new Coordinate(bottomRight.getLatitude(), topLeft.getLongitude());

        if (polygon != null) {
            mapView.removeCoordinateLine(polygon);
        }

        polygon = new CoordinateLine(Arrays.asList(
                topLeft, topRight, bottomRight, bottomLeft, topLeft
        ))
                .setColor(Color.RED)
                .setClosed(true)
                .setFillColor(Color.web("rgba(255, 0, 0, 0.3)"))
                .setVisible(true);

        mapView.addCoordinateLine(polygon);
    }
}

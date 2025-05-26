package com.game01.controller;

import com.game01.network.TestClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GameController {

    @FXML private ScrollPane scrollPane;
    @FXML private Pane mapContainer;

    private TestClient client;
    private String mapId;

    public void initialize() {
        // 可加角色控制邏輯
    }

    public void setClient(TestClient client) {
        this.client = client;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
        loadMap(mapId);
    }

    private void loadMap(String mapId) {
        try {
            String path = "/fxml/maps/" + mapId + ".fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node map = loader.load();
            mapContainer.getChildren().add(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

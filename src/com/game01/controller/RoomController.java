package com.game01.controller;

import com.game01.SceneManager;
import com.game01.network.TestClient;
import com.game01.network.TestServer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.InetAddress;
import java.net.URI;

public class RoomController {
    @FXML private Button createBtn, joinBtn, startBtn;
    @FXML private TextField ipField, portField;
    @FXML private Label statusLabel;
    @FXML private ChoiceBox<String> mapChoiceBox;

    private TestServer server;
    private TestClient client;

    @FXML
    public void initialize() {
        // 地圖選單
        mapChoiceBox.setItems(FXCollections.observableArrayList("map1", "map2", "map3"));
        mapChoiceBox.getSelectionModel().selectFirst();

        startBtn.setDisable(true);
    }

    /** 房主：創建房間 */
    @FXML
    public void onCreateRoom() {
        try {
            server = new TestServer(0);
            new Thread(() -> server.start()).start();

            // 稍等一會兒讓 port 綁定
            Thread.sleep(200);
            int port = server.getPort();
            String ip = InetAddress.getLocalHost().getHostAddress();
            statusLabel.setText("房間已建立：IP=" + ip + "  Port=" + port);

            createBtn.setDisable(true);
            joinBtn.setDisable(true);
            ipField.setDisable(true);
            portField.setDisable(true);

            startBtn.setDisable(false);
        } catch (Exception ex) {
            showError("創建失敗：" + ex.getMessage());
        }
    }

    /** 加入者：連線到現有房間 */
    @FXML
    public void onJoinRoom() {
        try {
            String ip   = ipField.getText().trim();
            int    port = Integer.parseInt(portField.getText().trim());
            client = new TestClient(new URI("ws://" + ip + ":" + port));
            boolean ok = client.connectBlocking();
            if (!ok) throw new RuntimeException("連線逾時");

            statusLabel.setText("已連線到 " + ip + ":" + port);
            createBtn.setDisable(true);
            joinBtn.setDisable(true);
            mapChoiceBox.setDisable(true);

            startBtn.setDisable(false);
        } catch (Exception ex) {
            showError("連線失敗：" + ex.getMessage());
        }
    }

    /** 按下後切到選角，並注入 client + mapId */
    @FXML
    public void onStartGame() {
        try {
            // 房主自連
            if (client == null) {
                int port = server.getPort();
                String ip = InetAddress.getLocalHost().getHostAddress();
                client = new TestClient(new URI("ws://" + ip + ":" + port));
                client.connectBlocking();
            }

            String mapId = mapChoiceBox.getValue();
            SceneManager.switchTo("RoleSelect", (RoleSelectController ctrl) -> {
                ctrl.setClient(client);
                ctrl.setMapId(mapId);
            });
        } catch (Exception ex) {
            showError("準備遊戲失敗：" + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Platform.runLater(() ->
            new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait()
        );
    }
}

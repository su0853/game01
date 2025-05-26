package com.game01.controller;

import com.google.gson.Gson;
import com.game01.SceneManager;
import com.game01.network.TestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.UUID;

public class RoleSelectController {
    @FXML private Label mapLabel, statusLabel;
    @FXML private Button roleAFastBtn, roleBHighBtn;

    private TestClient client;
    private String mapId;
    private final String clientId = UUID.randomUUID().toString();
    private final Gson gson = new Gson();
    private String myChoice, otherChoice;

    /** 注入 WebSocket 客戶端 */
    public void setClient(TestClient client) {
        this.client = client;
        client.setOnMessage(this::onServerMessage);
    }

    /** 注入 Map ID */
    public void setMapId(String mapId) {
        this.mapId = mapId;
        if (mapLabel != null) {
            mapLabel.setText("關卡：" + mapId);
        }
    }

    @FXML
    public void initialize() {
        statusLabel.setText("請選擇角色");
    }

    @FXML
    public void onSelectFast() {
        sendChoice("角色A");
    }

    @FXML
    public void onSelectHigh() {
        sendChoice("角色B");
    }

    @FXML
    public void onBack() {
        // 回房間
        SceneManager.switchTo("Room", ctrl -> {
            ((RoomController) ctrl).onJoinRoom();
        });
    }

    private void sendChoice(String role) {
        myChoice = role;
        statusLabel.setText("已選：" + role);
        roleAFastBtn.setDisable(true);
        roleBHighBtn.setDisable(true);

        // 發送選角訊息
        String msg = gson.toJson(new RoleMsg(clientId, role));
        client.send(msg);
    }

    private void onServerMessage(String text) {
        Platform.runLater(() -> {
            if ("START".equals(text)) {
                statusLabel.setText("遊戲開始！");
                // ✅ 切換至 GameScene 並注入參數
                SceneManager.switchTo("GameScene", ctrl -> {
                    GameController gameController = (GameController) ctrl;
                    gameController.setClient(client);
                    gameController.setMapId(mapId);
                });
                return;
            }

            // 嘗試解析選角訊息
            try {
                RoleMsg rm = gson.fromJson(text, RoleMsg.class);
                if (!rm.sender.equals(clientId)) {
                    otherChoice = rm.role;
                    statusLabel.setText("對方已選：" + otherChoice);
                }
            } catch (Exception ignored) {}
        });
    }

    /** GSON 傳輸用角色資料結構 */
    static class RoleMsg {
        String sender, role;
        RoleMsg(String s, String r) { sender = s; role = r; }
    }
}

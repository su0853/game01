package com.game01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.init(stage);

        // 注册场景
        SceneManager.register("Room",       "/fxml/RoomScene.fxml");
        SceneManager.register("RoleSelect", "/fxml/RoleSelectScene.fxml");

        // 显示 RoomScene（也可先显示一个 StartScene）
        stage.setTitle("多人合作遊戲");
        stage.setScene(new Scene(new Pane(), 800, 600));
        SceneManager.switchTo("Room");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

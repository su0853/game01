package com.game01;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SceneManager {
    private static Stage stage;
    private static final Map<String, String> registry = new HashMap<>();

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void register(String name, String fxml) {
        registry.put(name, fxml);
    }

    /** 切换场景，并可对 Controller 做注入 */
    public static <T> void switchTo(String name, Consumer<T> injector) {
        try {
            String path = registry.get(name);
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));
            Parent root = loader.load();
            @SuppressWarnings("unchecked")
            T ctrl = (T) loader.getController();
            injector.accept(ctrl);
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 不需要注入时用 */
    public static void switchTo(String name) {
        switchTo(name, c -> {});
    }
}

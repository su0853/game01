module game01 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // WebSocket & 日志
    requires org.java_websocket;
    requires org.slf4j;

    // JSON 序列化
    requires com.google.gson;

    // 下面这些 opens 是让 FXML Loader 及 Gson 能通过反射访问你的包
    opens com.game01 to javafx.graphics, javafx.fxml;
    opens com.game01.controller to javafx.fxml, com.google.gson;
    opens com.game01.network    to com.google.gson;
    //opens com.game01.map        to javafx.fxml;
    //opens com.game01.platform   to javafx.fxml;
}

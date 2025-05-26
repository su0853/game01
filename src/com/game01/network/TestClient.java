package com.game01.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

public class TestClient extends WebSocketClient {
    private Consumer<String> onMessageCallback;

    public TestClient(URI serverUri) {
        super(serverUri);
    }

    /** 由上层 Controller 注入，用来接收任何文本消息 */
    public void setOnMessage(Consumer<String> callback) {
        this.onMessageCallback = callback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("連線至服务器: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("收到服务器消息: " + message);
        if (onMessageCallback != null) {
            onMessageCallback.accept(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("與服务器斷開: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}

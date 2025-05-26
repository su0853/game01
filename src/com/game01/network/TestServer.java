package com.game01.network;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestServer extends WebSocketServer {
    private final Map<String,String> choices = new ConcurrentHashMap<>();
    private final int maxPlayers = 2;

    public TestServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshakedata) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

    @Override
    public void onMessage(WebSocket conn, String message) {
        // 若是選角 JSON
        if (message.trim().startsWith("{")) {
            JsonObject obj = JsonParser.parseString(message).getAsJsonObject();
            String sender = obj.get("sender").getAsString();
            String role   = obj.get("role").getAsString();
            choices.put(sender, role);
            broadcast(message);
            if (choices.size() >= maxPlayers) {
                broadcast("START");
            }
        } else {
            // 其他訊息：Echo
            conn.send("Echo: " + message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {}
}

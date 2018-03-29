package com.ultrapower.ci.control.taskManage.controller;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
    
    public static void sendMessage(String uri,String message){
    	try {
    		WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
            Session session = container.connectToServer(WebSocketClient.class, new URI(uri)); // 连接会话
            session.getBasicRemote().sendText(message); // 发送文本消息
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
}
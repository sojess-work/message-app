package com.org.messageapp.config;

import com.org.messageapp.entity.Message;
import com.org.messageapp.enums.MessageType;
import com.org.messageapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
public class ChatWebsocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();

    private  final MessageRepository messageRepository;

    public ChatWebsocketHandler(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.add(session);
        String username = session.getUri().getQuery().split("=")[1];
        session.getAttributes().put("username",username);
        sendMessageToAll(session,new TextMessage(username + " has joined the chat"));
        Message message = Message.builder()
                .message(username + " has joined the chat")
                .messageType(MessageType.JOIN)
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        sendMessageToAll(session,message);
        Message messageEntity = Message.builder()
                .message(message.getPayload())
                .messageType(MessageType.CHAT)
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(messageEntity);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws IOException {
        sessions.remove(session);
        String username = (String) session.getAttributes().get("username");
        sendMessageToAll(session,new TextMessage(username + " has left the chat"));

        Message messageEntity = Message.builder()
                .message(username + " has left the chat")
                .messageType(MessageType.LEAVE)
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(messageEntity);

    }

    private void sendMessageToAll(WebSocketSession session, TextMessage message) throws IOException {
        for (WebSocketSession s : sessions) {
            if (!s.equals(session)) {
                s.sendMessage(message);
            }
        }
    }
}

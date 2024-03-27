package com.org.messageapp.entity;

import com.org.messageapp.enums.MessageType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    private String username;
    private String message;
    private MessageType messageType;
    private LocalDateTime timestamp;
}

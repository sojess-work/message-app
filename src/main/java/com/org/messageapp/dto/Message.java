package com.org.messageapp.dto;

import com.org.messageapp.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    private String sender;
    private String message;
    private MessageType messageType;
}

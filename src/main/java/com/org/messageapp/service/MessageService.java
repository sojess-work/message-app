package com.org.messageapp.service;

import com.org.messageapp.entity.Message;
import com.org.messageapp.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    public List<Message> getAllMessageHistory(){
        log.info("Fetching all messages from db");

        List<Message> messages = messageRepository.findAll();

        log.info("Fetched successfully all messages from db");

        return messages;
    }
}

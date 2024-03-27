package com.org.messageapp.controller;

import com.org.messageapp.constants.MessageAppConstants;
import com.org.messageapp.dto.response.MappResponse;
import com.org.messageapp.entity.Message;
import com.org.messageapp.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {
    @Autowired
    MessageService messageService;
    @GetMapping("/get-all-messages")
    public ResponseEntity<MappResponse> getAllMessages(HttpServletRequest request){
        log.info("GET - /messages/get-all-messages -- STARTS");
        try{
            List<Message> messages = messageService.getAllMessageHistory();

            log.info("GET - /messages/get-all-messages -- ENDS");
            return ResponseEntity.ok().body(MappResponse.builder()
                            .data(messages)
                    .build());
        }catch (Exception e){
            log.error("GET - /messages/get-all-messages -- FAILED",e);
            return ResponseEntity.status(500).body(MappResponse.builder()
                            .message(MessageAppConstants.COULD_NOT_PROCESS_REQUEST_MESSAGE)
                    .build());
        }

    }
}

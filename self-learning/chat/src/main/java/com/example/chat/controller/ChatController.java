package com.example.chat.controller;


import com.example.chat.model.Message;
import com.example.chat.model.MessageType;
import com.example.chat.model.User;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        message.setSender(user);
        message.setType(MessageType.CHAT);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        message.setSender(user);
        message.setType(MessageType.JOIN);
        message.setContent(user.getFullName() + " joined!");
        return messageRepository.save(message);
    }
}

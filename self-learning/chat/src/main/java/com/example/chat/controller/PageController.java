package com.example.chat.controller;

import com.example.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/chat")
    public String chat(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("oldMessages", messageRepository.findTop50ByOrderByTimestampDesc());
        return "chat";
    }
}

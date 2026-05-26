package com.example.notification.controller;

import com.example.notification.model.Notification;
import com.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Notification send(@RequestBody Notification notification) {
        return notificationService.sendNotification(notification);
    }

    @GetMapping("/{id}")
    public Notification getStatus(@PathVariable Long id) {
        return notificationService.getStatus(id);
    }
}

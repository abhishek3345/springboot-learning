package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Notification sendNotification(Notification notification) {
        try {
            // Only send email if channel is EMAIL
            if ("EMAIL".equalsIgnoreCase(notification.getChannel())) {
                sendRealEmail(notification);
                notification.setStatus("SENT");
                System.out.println("Email sent to: " + notification.getRecipient());
            } else {
                // For SMS/IN_APP, just save for now
                notification.setStatus("PENDING");
                System.out.println("Saved " + notification.getChannel() + " notification");
            }
        } catch (Exception e) {
            notification.setStatus("FAILED");
            notification.setBody(notification.getBody() + " [Error: " + e.getMessage() + "]");
            System.out.println("Failed to send: " + e.getMessage());
        }

        return notificationRepository.save(notification);
    }

    private void sendRealEmail(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getSubject() != null ? notification.getSubject() : "Notification");
        message.setText(notification.getBody());
        message.setFrom("YOUR_EMAIL@gmail.com"); // Use your Gmail

        mailSender.send(message);
    }

    public Notification getStatus(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
}

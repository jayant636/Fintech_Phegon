package com.example.phegon.phegonBank.notification.service;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.notification.dtos.NotificationDto;

public interface NotificationService {
    void sendEmail(NotificationDto notificationDto, User user);
}

package com.example.phegon.phegonBank.notification.dtos;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.Notificationtype;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long id;
    private String subject;
    private String recipient;
    private String body;
    private Notificationtype type;

    private LocalDateTime createdAt;

    private String templateName;
    private Map<String,Object> templateVariable;

}

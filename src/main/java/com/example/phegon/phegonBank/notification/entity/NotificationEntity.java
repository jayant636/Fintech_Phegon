package com.example.phegon.phegonBank.notification.entity;

import com.example.phegon.phegonBank.auth_user.entity.User;
import com.example.phegon.phegonBank.enums.Notificationtype;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String recipient;

    private String body;

    @Enumerated(EnumType.STRING)
    private Notificationtype type;


    @ManyToOne
    //Parent is Notification & child is user
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();

}

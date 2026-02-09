package com.example.phegon.phegonBank.notification.repository;

import com.example.phegon.phegonBank.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {

}

// --- File: src/main/java/com/mytextile/notificationservice/repository/NotificationLogRepository.java ---
package com.mytextile.notification.repository;

import com.mytextile.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    // Add this finder method
    List<NotificationLog> findAllByClientId(Long clientId);
}
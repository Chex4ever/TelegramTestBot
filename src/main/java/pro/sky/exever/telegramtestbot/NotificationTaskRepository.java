package pro.sky.exever.telegramtestbot;

import java.time.LocalDate;
import java.time.LocalDateTime;

@org.springframework.stereotype.Repository
public interface NotificationTaskRepository extends org.springframework.data.jpa.repository.JpaRepository<NotificationTask, Long> {
    java.util.List<NotificationTask> findByDateToSend(java.time.LocalDateTime date);
    java.util.List<NotificationTask> findByStatus(NotificationTaskStatus status);
    java.util.List<NotificationTask> findByStatusAndDateToSendLessThanEqual(NotificationTaskStatus status, LocalDateTime dateToSend);
}

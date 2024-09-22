package pro.sky.exever.telegramtestbot;

@org.springframework.stereotype.Repository
public interface NotificationTaskRepository extends org.springframework.data.jpa.repository.JpaRepository<NotificationTask, Long> {
    java.util.List<NotificationTask> findByDateToSend(java.time.LocalDateTime date);
    java.util.List<NotificationTask> findByStatus(NotificationTaskStatus status);
}

package pro.sky.exever.telegramtestbot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class TelegramBotScheduler {
    private final NotificationTaskService notificationTaskService;

    public TelegramBotScheduler(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotificationMessage() {
        notificationTaskService.sendNotificationMessage();
    }
}

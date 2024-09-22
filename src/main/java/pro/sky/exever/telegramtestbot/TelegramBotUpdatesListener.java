package pro.sky.exever.telegramtestbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final String ERROR = "Не понимаю.";
    private static final String INSTRUCTIONS = "Введите напоминание в формате дд.мм.гггг чч:мм напоминание.";
    private static final String WELCOME = "Добро пожаловать.";
    private static final String START = "/start";
    private final NotificationTaskService notificationTaskService;
    private final TelegramBot bot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService, TelegramBot bot) {
        this.notificationTaskService = notificationTaskService;
        this.bot = bot;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotificationMessage() {
        notificationTaskService.sendNotificationMessage();
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            logger.info("Processing update: {}", update);
            if (update.message() == null) {
                continue;
            }
            Message message = update.message();
            if (message.text().startsWith(START)) {
                logger.info("{} {}", START, LocalDateTime.now());
                notificationTaskService.sendMessage(getChatId(message), WELCOME + message.from() + " " + INSTRUCTIONS);
            } else {
                var processed_entry = notificationTaskService.parseMessage(message.text());
                if (processed_entry != null) {
                    scheduledNotification(getChatId(message), processed_entry);
                } else {
                    notificationTaskService.sendMessage(getChatId(message), ERROR);
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void scheduledNotification(long chatId, NotificationTask notificationTask) {
        notificationTaskService.scheduleNotification(notificationTask, chatId);
        notificationTaskService.sendMessage(chatId, "Задача создана: " + notificationTask.getNote());
    }

    private long getChatId(Message message) {
        return message.chat().id();
    }
}

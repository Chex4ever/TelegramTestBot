package pro.sky.exever.telegramtestbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            logger.info("Processing update: {}", update);
            long chatId = update.message().chat().id();
            if (update.message().text() == null) {
                notificationTaskService.sendMessage(chatId, ERROR);
                continue;
            }
            Message message = update.message();
            if (message.text().startsWith(START)) {
                logger.info("{} {} {}", START, chatId, LocalDateTime.now());
                notificationTaskService.sendMessage(message.chat().id(), WELCOME + message.from() + " " + INSTRUCTIONS);
            } else {
                NotificationTask task = notificationTaskService.parseMessage(message.text());
                if (task != null) {
                    notificationTaskService.scheduleNotification(task, chatId);
                    notificationTaskService.sendMessage(chatId, "Задача создана: " + task.getNote());
                } else {
                    notificationTaskService.sendMessage(chatId, ERROR);
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

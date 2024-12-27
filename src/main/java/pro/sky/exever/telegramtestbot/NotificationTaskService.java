package pro.sky.exever.telegramtestbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class NotificationTaskService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);
    private static final Pattern REGEX_DATETIME_TASK = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    public static final String PATTERN_DATETIME = "dd.MM.yyyy HH:mm";

    private final NotificationTaskRepository repo;
    private final TelegramBot bot;

    public NotificationTaskService(NotificationTaskRepository repo, TelegramBot bot) {
        this.repo = repo;
        this.bot = bot;
    }

    public void scheduleNotification(NotificationTask task, long chatId){
        task.setChatId(chatId);
        repo.save(task);
        logger.info("Уведомление запланировано: {}", task);
    }

    public NotificationTask parseMessage(String message){
        Matcher matcher = REGEX_DATETIME_TASK.matcher(message);
        if (matcher.find()){
            String note = matcher.group(3);
            LocalDateTime dateToSent = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern(PATTERN_DATETIME));
            NotificationTask task = new NotificationTask(note, dateToSent);
            logger.info("Сохраняю в базу: {}", task);
            repo.save(task);
            return task;
        } else{
            logger.error("Неверная дата");
        }
        return null;
    }

    public void sendNotificationMessage() {
        Collection<NotificationTask> tasks = repo.findByStatusAndDateToSendLessThanEqual(NotificationTaskStatus.SCHEDULED, LocalDateTime.now());
        for (NotificationTask task : tasks) {
            sendMessage(task.getChatId(), task.getNote());
            task.setAsSent();
            repo.save(task);
            logger.info("Уведомление отправлено {}", task);
        }
    }

    public void sendMessage(long chatId, String messageText) {
        bot.execute(new SendMessage(chatId, messageText));
        logger.info("В чат {} отправлено сообщение: {}", chatId, messageText);
    }
}

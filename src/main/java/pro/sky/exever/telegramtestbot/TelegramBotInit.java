package pro.sky.exever.telegramtestbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotInit {
    private final String token;

    public TelegramBotInit(@Value("${telegramBot.token}") String token ) {
        this.token = token;
    }

    @Bean
    public TelegramBot telegramBot(){
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}

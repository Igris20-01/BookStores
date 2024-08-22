package uz.booker.bookstore.configuration.other;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramErrorBot extends TelegramLongPollingBot {

    final ErrorBotConfiguration botConfig;

    public TelegramErrorBot(ErrorBotConfiguration botConfig){
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {}

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return botConfig.getToken();
    }

}

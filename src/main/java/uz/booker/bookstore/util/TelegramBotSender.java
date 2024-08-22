package uz.booker.bookstore.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.booker.bookstore.configuration.other.ErrorBotConfiguration;
import uz.booker.bookstore.configuration.other.TelegramErrorBot;

@Component
public class TelegramBotSender {


    private static final Logger logger = LoggerFactory.getLogger(TelegramBotSender.class);

    @Autowired
    private TelegramErrorBot telegramBot;

    @Autowired
    private ErrorBotConfiguration botConfig;

    public void sendErrorToTelegram(HttpServletRequest request, int errorCode, Throwable ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String errorLocation = "Unknown";
        if (stackTrace.length > 0) {
            errorLocation = stackTrace[0].getClassName() + ":" + stackTrace[0].getLineNumber();
        }
        String errorReason = ex.getMessage();
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("Error occurred in request ")
                .append(request.getRequestURI())
                .append(":\n")
                .append("Code status: ")
                .append(errorCode)
                .append("\n")
                .append("Location: ")
                .append(errorLocation)
                .append("\n")
                .append("Reason: ")
                .append(errorReason);
        String errorMessage = errorMessageBuilder.toString();
        logger.error(errorMessage, ex);
        SendMessage message = new SendMessage();
        message.setChatId(botConfig.getChannel());
        message.setText(errorMessage);

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error sending message to Telegram: ", e);
        }
    }
}

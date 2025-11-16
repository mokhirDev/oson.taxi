package uz.oson.taxi.infrastructure;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OsonTaxiBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(OsonTaxiBot.class);

    private final UpdateHandler updateHandler;

    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (!updateHandler.isAvailableMessage(update)) {
                return;
            }
            List<BotApiMethod<?>> responses = updateHandler.handle(update);
            if (responses != null && !responses.isEmpty()) {
                executeResponsesSafely(responses);
            }
        } catch (Exception e) {
            log.error("Unhandled exception while processing update", e);
        }
    }

    private void executeResponsesSafely(List<BotApiMethod<?>> responses) {
        for (BotApiMethod<?> response : responses) {
            try {
                Serializable result = execute(response);

                if (result instanceof Message message) {
                    updateHandler.saveLastMessageId(message);
                } else {
                    log.debug("Telegram method {} returned non-message result: {}",
                            response.getMethod(),
                            result.getClass().getSimpleName());
                }

            } catch (TelegramApiException e) {
                String msg = e.getMessage() == null ? "" : e.getMessage();
                if (msg.contains("message to delete not found")) {
                    log.warn("Message already deleted or not found (ignored) — {}", msg);
                    continue;
                }
                log.error("Failed to execute Telegram method: {}", response.getMethod(), e);
            } catch (Exception e) {
                log.error("Unexpected error executing response", e);
            }
        }
    }
}

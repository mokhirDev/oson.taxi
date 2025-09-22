package uz.oson.taxi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class CommentPage implements BotPage {
    @Override
    public String getName() {
        return "add_comment";
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && "save_seats_count".equals(update.getCallbackQuery().getData());
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(List.of(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("⏭ Пропустить")
                                .callbackData("skip_comment")
                                .build()
                )
        ));

        return List.of(
                SendMessage
                .builder()
                .chatId(chatId)
                .replyMarkup(keyboard)
                .text("order.comment")
                .build()
        );

    }
}

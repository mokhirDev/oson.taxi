package uz.oson.taxi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class CheckOrderPage implements BotPage {
    @Override
    public String getName() {
        return "check_order";
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && "skip_comment".equals(update.getCallbackQuery().getData());
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("✅ Подтвердить")
                                .callbackData("finish_order")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("❌ Отменить")
                                .callbackData("cancel_order")
                                .build()
                ))
                .build();

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("check.order\norder.full.detail")
                        .replyMarkup(keyboard)
                        .build()
        );
    }
}

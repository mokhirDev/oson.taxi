package uz.oson.taxi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.PageCodeEnum;

import java.util.List;

@Component
public class DriverPage implements BotPage {

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("📥 Вступить в группу")
                                .callbackData("join_to_group")
                                .build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("🔙 Назад")
                                .callbackData("back")
                                .build()
                ))
                .build();

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("driver.menu")
                        .replyMarkup(keyboard)
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.DRIVER_CODE, update);
    }
}

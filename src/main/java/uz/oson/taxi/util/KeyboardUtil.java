package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardUtil {

    private final MessageFactory messageFactory;


    // ------------------ Inline Keyboard ------------------

    public static InlineKeyboardMarkup inlineKeyboard(List<List<InlineKeyboardButton>> rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    // ------------------ Reply Keyboard ------------------

    public static ReplyKeyboardMarkup replyKeyboard(List<KeyboardRow> rows, boolean resize) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(resize);
        return keyboard;
    }

    public static ReplyKeyboardRemove cleanReplyKeyboard() {
        return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }

    // ------------------ Вспомогательные методы ------------------

    public InlineKeyboardButton button(ButtonEnum buttonEnum, LocaleEnum localeEnum) {
        return InlineKeyboardButton.builder()
                .text(messageFactory.getButtonText(buttonEnum, localeEnum))
                .callbackData(buttonEnum.getButtonCallBack())
                .build();
    }

    public List<InlineKeyboardButton> inlineRow(LocaleEnum localeEnum,  Object... buttons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (ButtonEnum button : (ButtonEnum[]) buttons) {
            row.add(button(button, localeEnum));
        }
        return row;
    }

    public KeyboardRow replyRow(LocaleEnum localeEnum, ButtonEnum... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (ButtonEnum button : buttons) {
            row.add(new KeyboardButton(messageFactory.getButtonText(button, localeEnum)));
        }
        return row;
    }

}


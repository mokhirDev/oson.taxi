package uz.oson.taxi.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.ButtonTypeEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {

    private KeyboardUtil() {
    }

    // ------------------ Inline Keyboard ------------------

    public static InlineKeyboardButton inlineButton(ButtonEnum button, MessageFactory messageFactory, LocaleEnum localeEnum) {
        String text = messageFactory.getButtonText(button, localeEnum);
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(button.getButtonCallBack())
                .build();
    }

    public static List<InlineKeyboardButton> inlineRow(MessageFactory messageFactory, LocaleEnum localeEnum, ButtonEnum... buttons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (ButtonEnum button : buttons) {
            if (button.getButtonTypeEnum() == ButtonTypeEnum.INLINE) {
                row.add(inlineButton(button, messageFactory, localeEnum));
            }
        }
        return row;
    }

    public static InlineKeyboardMarkup inlineKeyboard(List<List<InlineKeyboardButton>> rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    // ------------------ Reply Keyboard ------------------

    public static KeyboardButton replyButton(ButtonEnum button, MessageFactory messageFactory, LocaleEnum localeEnum) {
        String text = messageFactory.getButtonText(button, localeEnum);
        KeyboardButton keyboardButton = new KeyboardButton(text);
        keyboardButton.setRequestContact(button == ButtonEnum.SHARE_CONTACT);
        return keyboardButton;
    }

    public static KeyboardRow replyRow(MessageFactory messageFactory, LocaleEnum localeEnum, ButtonEnum... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (ButtonEnum button : buttons) {
            if (button.getButtonTypeEnum() == ButtonTypeEnum.REPLY) {
                row.add(replyButton(button, messageFactory, localeEnum));
            }
        }
        return row;
    }

    public static ReplyKeyboardMarkup replyKeyboard(List<KeyboardRow> rows, boolean resize) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(resize);
        return keyboard;
    }

    public static ReplyKeyboardRemove cleanReplyKeyboard() {
        return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }

}


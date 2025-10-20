package uz.oson.taxi.entity.enums;

import org.telegram.telegrambots.meta.api.objects.Update;

public enum InputType {
    TEXT,       // обычное текстовое сообщение
    CALLBACK,   // callback кнопки
    CONTACT,    // контакт
    LOCATION;    // геопозиция

    public static InputType getInputType(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                return InputType.TEXT;
            }
            if (update.getMessage().hasContact()) {
                return InputType.CONTACT;
            }
            if (update.getMessage().hasLocation()) {
                return InputType.LOCATION;
            }
        }
        if (update.hasCallbackQuery()) {
            return InputType.CALLBACK;
        }
        return null;
    }

    public static String extractValue(Update update, InputType inputType) {
        switch (inputType) {
            case TEXT -> {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    return update.getMessage().getText();
                }
            }
            case CALLBACK -> {
                if (update.hasCallbackQuery()) {
                    return update.getCallbackQuery().getData();
                }
            }
            case CONTACT -> {
                if (update.hasMessage() && update.getMessage().hasContact()) {
                    return update.getMessage().getContact().getPhoneNumber();
                }
            }
        }
        return null;
    }
}

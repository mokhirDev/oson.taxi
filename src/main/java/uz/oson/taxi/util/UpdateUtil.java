package uz.oson.taxi.util;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.InputType;

public class UpdateUtil {
    public static String getInput(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())
            return update.getMessage().getText();
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getData();
        if (update.hasMessage() && update.getMessage().hasContact())
            return update.getMessage().getContact().getPhoneNumber();
        return "";
    }

    public static Long getChatId(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return null;
        return switch (inputType) {
            case TEXT, CONTACT -> update.getMessage().getChatId();
            case CALLBACK -> update.getCallbackQuery().getFrom().getId();
            default -> null;
        };
    }

    public static Integer getMessageId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getMessageId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }

    public static Integer getCallBackMessageId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return null;
    }

}

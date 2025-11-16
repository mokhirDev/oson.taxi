package uz.oson.taxi.util;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

public class MessageBuilder {

    public static DeleteMessage buildDeleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage
                .builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }
}

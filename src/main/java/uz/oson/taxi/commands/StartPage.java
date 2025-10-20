package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;

import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class StartPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        userService.setNewCredentials(chatId);
        userService.setCurrentPage(PageCodeEnum.LANG_CODE, chatId);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .replyMarkup(keyboardFactory.cleanReplyKeyboard())
                        .text(messageFactory.getPageMessage(PageMessageEnum.START, LocaleEnum.UNKNOWN))
                        .build(),
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .replyMarkup(keyboardFactory.languageKeyboard())
                        .text(messageFactory.getPageMessage(PageMessageEnum.LANG, LocaleEnum.UNKNOWN))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.START_CODE, update);
    }

}

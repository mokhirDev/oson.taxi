package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StartPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public String getName() {
        return "/" + PageEnum.START.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().contains("start");
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        userService.setNewCredentials(chatId);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .replyMarkup(keyboardFactory.cleanReplyKeyboard())
                        .text(messageFactory.getPageMessage(PageEnum.START, LocaleEnum.UNKNOWN))
                        .build(),
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .replyMarkup(keyboardFactory.languageKeyboard())
                        .text(messageFactory.getPageMessage(PageEnum.LANG, LocaleEnum.UNKNOWN))
                        .build()
        );
    }

}

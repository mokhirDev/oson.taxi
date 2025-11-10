package uz.oson.taxi.commands.manual;

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


@Component
@RequiredArgsConstructor
public class StartPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        if (PageCommandEnum.isValid(List.of(PageCommandEnum.START_CODE), update)) {
            userService.setNewCredentials(chatId);
        }
        userService.setCurrentPage(BotPageStageEnum.LANGUAGE, chatId);
        LocaleEnum userLocale = userService.getUserLocale(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .replyMarkup(keyboardFactory.cleanReplyKeyboard())
                        .text(messageFactory.getPageMessage(PageMessageEnum.START, userLocale))
                        .build(),
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .replyMarkup(keyboardFactory.languageKeyboard())
                        .text(messageFactory.getPageMessage(PageMessageEnum.LANG, userLocale))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (PageCommandEnum.isValid(List.of(PageCommandEnum.START_CODE), update)) {
            return true;
        }

        if (currentPage == BotPageStageEnum.ROLE) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.BACK_CODE), update);
        }
        return false;
    }

}

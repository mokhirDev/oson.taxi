package uz.oson.taxi.commands.manual;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LangPage implements BotPage {

    private final UserService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Lang.matches(input)) {
            userService.setLang(LocaleEnum.getLocaleEnum(input), UpdateUtil.getChatId(update));
            return PageIdGenerator.generate(BotPageStageEnum.ROLE, UserTypeEnum.GUEST);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUserLocale(update);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.START, locale))
                        .replyMarkup(keyboardFactory.cleanReplyKeyboard())
                        .build(),

                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.LANG, locale))
                        .replyMarkup(keyboardFactory.languageKeyboard())
                        .build()
        );
    }


    @Override
    public String getPageId() {
        return PageIdGenerator.generate(BotPageStageEnum.LANGUAGE, UserTypeEnum.GUEST);
    }
}
package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.AliasesEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LangPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public String getName() {
        return PageEnum.LANG.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && getAliases()
                .contains(update.getCallbackQuery().getData());
    }

    @Override
    public List<String> getAliases() {
        return AliasesEnum.LANG.getValues();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String language = update.getCallbackQuery().getData();
        LocaleEnum localeEnum = LocaleEnum.getLocaleEnum(language);
        setUserLocale(localeEnum, chatId);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageEnum.ROLE, localeEnum))
                        .replyMarkup(
                                keyboardFactory
                                        .roleKeyboard(localeEnum)
                        )
                        .build()
        );
    }

    void setUserLocale(LocaleEnum language, Long chatId) {
        userService.setLang(language, chatId);
    }
}

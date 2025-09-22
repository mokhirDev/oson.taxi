package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.AliasesEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.service.UserStateCacheService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDatePage implements BotPage {
    private final KeyboardFactory keyboardFactory;
    private final MessageFactory messageFactory;
    private final UserStateService userStateService;

    @Override
    public String getName() {
        return PageEnum.ORDER_DATE.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && getAliases()
                .contains(update.getCallbackQuery().getData());
    }

    public List<String> getAliases() {
        return AliasesEnum.ORDER_DATE.getValues();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum localeEnum = userStateService.getUser(chatId).getLocale();

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageEnum.ORDER_DATE, localeEnum))
                        .replyMarkup(keyboardFactory.createDateKeyboard(localeEnum, 7))
                        .build()
        );
    }
}

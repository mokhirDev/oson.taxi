package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderPage implements BotPage {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;

    @Override
    public String getName() {
        return PageEnum.CREATE_ORDER.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && PageEnum.CREATE_ORDER.name().toLowerCase()
                .equals(
                        update.getCallbackQuery().getData()
                );
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageEnum.SHARE_CONTACT, localeEnum))
                        .replyMarkup(keyboardFactory.shareContactKeyboard(localeEnum))
                        .build()
        );
    }

}

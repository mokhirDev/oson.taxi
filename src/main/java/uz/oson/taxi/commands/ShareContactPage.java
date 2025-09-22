package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShareContactPage implements BotPage {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;
    private final OrderService orderService;

    @Override
    public String getName() {
        return PageEnum.SHARE_CONTACT.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasContact();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        orderService.createOrder(update);

        return List.of(
                buildSendMessage(chatId, messageFactory.getPageMessage(PageEnum.CONTACT_RECEIVED, localeEnum),
                        keyboardFactory.cleanReplyKeyboard()),
                buildSendMessage(chatId, messageFactory.getPageMessage(PageEnum.ORDER_FROM, localeEnum),
                        keyboardFactory.fromCityKeyboard(localeEnum))
        );
    }

    private SendMessage buildSendMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }

}

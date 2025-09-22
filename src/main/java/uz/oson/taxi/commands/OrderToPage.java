package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.AliasesEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderToPage implements BotPage {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final OrderService orderService;
    private final UserStateService userService;

    @Override
    public String getName() {
        return PageEnum.ORDER_TO.getPageName();
    }

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery()
                && getAliases()
                .contains(update.getCallbackQuery().getData());
    }

    @Override
    public List<String> getAliases() {
        return AliasesEnum.CITY.getValues();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        orderService.setCacheOrderTo(update);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageEnum.ORDER_SEATS, locale))
                        .replyMarkup(keyboardFactory.seatsKeyboard(locale, 0))
                        .build()
        );
    }
}

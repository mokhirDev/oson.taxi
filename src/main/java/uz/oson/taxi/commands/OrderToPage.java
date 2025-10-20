package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.OrderAction;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageCodeEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderToPage implements BotPage, OrderAction {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final OrderService orderService;
    private final UserStateService userService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        updateOrder(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_SEATS, locale))
                        .replyMarkup(keyboardFactory.seatsKeyboard(locale, 0))
                        .build()
        );
    }

    @Override
    public void updateOrder(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String toCity = update.getCallbackQuery().getData();
            Orders orderByChatId = orderService.findOrderByChatId(update.getCallbackQuery().getMessage().getChatId());
            orderByChatId.setTo_city(toCity);
            orderService.updateOrder(orderByChatId);
        }
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.CITY_FROM_CODE, update);
    }
}

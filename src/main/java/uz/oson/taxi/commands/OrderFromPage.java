package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
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
public class OrderFromPage implements BotPage {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;
    private final OrderService orderService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        updateOrder(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_TO, locale))
                        .replyMarkup(keyboardFactory.toCityKeyboard(locale))
                        .build()
        );
    }

    private void updateOrder(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String fromCity = InputType.extractValue(update, inputType);
            Long chatId = update.getCallbackQuery().getFrom().getId();
            Orders orderByChatId = orderService.findOrderByChatId(chatId);
            orderByChatId.setFrom_city(fromCity);
            orderService.updateOrder(orderByChatId);
        }
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.CITY_FROM_CODE, update);
    }
}

package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.OrderAction;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.*;
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
        Long chatId = userService.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.ORDER_TO, chatId);

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
            Orders orderByChatId = orderService.findOrderByChatIdInCache(update.getCallbackQuery().getMessage().getChatId());
            orderByChatId.setTo_city(toCity);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.ORDER_FROM) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.CITY_TO_CODE), update);
        }
        return false;
    }
}

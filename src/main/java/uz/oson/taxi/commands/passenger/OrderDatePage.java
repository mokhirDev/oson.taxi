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
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDatePage implements BotPage, OrderAction {
    private final OrderService orderService;
    private final KeyboardFactory keyboardFactory;
    private final MessageFactory messageFactory;
    private final UserService userService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Date.matches(input)) {
            updateOrder(update);
            return PageIdGenerator.generate(BotPageStageEnum.COMMENT, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_DATE, localeEnum))
                        .replyMarkup(keyboardFactory.createDateKeyboard(localeEnum, 7))
                        .build()
        );
    }

    @Override
    public void updateOrder(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String input = UpdateUtil.getInput(update);
            Long chatId = UpdateUtil.getChatId(update);
            Orders orderByChatId = orderService.findOrderByChatIdInCache(chatId);
            orderByChatId.setLeavingDate(input);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.ORDER_DATE,
                UserTypeEnum.PASSENGER
        );
    }
}

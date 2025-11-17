package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.Action;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.ChatKeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckPage implements BotPage, Action {
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final OrderService orderService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.ConfirmOrder.matches(input)) {
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.CONFIRM_ORDER, UserTypeEnum.PASSENGER);
        } else if (RegExEnum.CancelOrder.matches(input)) {
            orderService.cancelOrder(UpdateUtil.getChatId(update));
            return PageIdGenerator.generate(BotPageStageEnum.PASSENGER_MENU, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.CHECK_ORDER, locale);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(fillOrderDetails(pageMessage, chatId, locale))
                        .replyMarkup(chatKeyboardFactory.checkOrderKeyboard(locale))
                        .build()
        );
    }

    private String fillOrderDetails(String pageMessage, Long chatId, LocaleEnum locale) {
        Orders order = orderService.findOrderByChatIdInCache(chatId);
        return pageMessage.formatted(
                getValue(messageFactory.getCityDetails(order.getFrom_city(), locale)),
                getValue(messageFactory.getCityDetails(order.getTo_city(), locale)),
                getValue(messageFactory.getDateDetails(order.getLeavingDate(), locale)),
                getValue(order.getSeatsCount().toString()),
                getValue(order.getComment())
        );
    }

    private String getValue(String value) {
        return value != null ? value.trim() : "";
    }

    @Override
    public void update(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        orderService.confirmOrder(chatId);
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.CHECK_ORDER,
                UserTypeEnum.PASSENGER
        );
    }
}
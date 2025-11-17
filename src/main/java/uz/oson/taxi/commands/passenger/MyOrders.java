package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
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
public class MyOrders implements BotPage {
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final UserService userService;
    private final OrderService orderService;
    private final MessageFactory messageFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Home.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.PASSENGER_MENU, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        String pageMessage = fillMyOrderDetails(chatId, localeEnum);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(pageMessage)
                        .replyMarkup(chatKeyboardFactory.backToMainMenuKeyboard(localeEnum))
                        .build()
        );
    }

    private String fillMyOrderDetails(Long chatId, LocaleEnum locale) {
        List<Orders> allOrders = orderService.findAllOrdersByChatIdInDB(chatId);
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.MY_ORDERS, locale);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pageMessage).append("\n");
        for (int i = 0; i < allOrders.size(); i++) {
            String orderDetail = messageFactory.getPageMessage(PageMessageEnum.ORDER, locale);
            stringBuilder.append("#").append(1 + i).append("\n");
            String formatted = orderDetail.formatted(
                    getValue(messageFactory.getCityDetails(allOrders.get(i).getFrom_city(), locale)),
                    getValue(messageFactory.getCityDetails(allOrders.get(i).getTo_city(), locale)),
                    getValue(messageFactory.getDateDetails(allOrders.get(i).getLeavingDate(), locale)),
                    getValue(allOrders.get(i).getSeatsCount().toString()),
                    getValue(allOrders.get(i).getComment())
            );
            stringBuilder.append(formatted).append("\n");
        }
        return stringBuilder.toString();
    }

    private String getValue(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.MY_ORDERS,
                UserTypeEnum.PASSENGER
        );
    }
}

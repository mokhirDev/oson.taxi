package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.BotPageStageEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageCommandEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyOrders implements BotPage {
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;
    private final OrderService orderService;
    private final MessageFactory messageFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.MY_ORDERS, chatId);
        String pageMessage = fillMyOrderDetails(chatId, localeEnum);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(pageMessage)
                        .replyMarkup(keyboardFactory.backToMainMenuKeyboard(localeEnum))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.PASSENGER_MENU) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.MY_ORDERS_CODE), update);
        }
        return false;
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
}

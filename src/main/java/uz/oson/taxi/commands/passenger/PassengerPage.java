package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.ChatKeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PassengerPage implements BotPage {
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final OrderService orderService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.CreateOrder.matches(input)) {
            orderService.createOrderInCache(update);
            return PageIdGenerator.generate(BotPageStageEnum.CREATE_ORDER, UserTypeEnum.PASSENGER);
        } else if (RegExEnum.MyOrders.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.MY_ORDERS, UserTypeEnum.PASSENGER);
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
                        .text(messageFactory.getPageMessage(PageMessageEnum.PASSENGER_MENU, localeEnum))
                        .replyMarkup(chatKeyboardFactory.passengerMenuKeyboard(localeEnum))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.PASSENGER_MENU,
                UserTypeEnum.PASSENGER
        );
    }

    @Override
    public String previousPage() {
        return PageIdGenerator.generate(BotPageStageEnum.ROLE, UserTypeEnum.GUEST);
    }
}

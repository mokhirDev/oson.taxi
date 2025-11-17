package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

@Component
@RequiredArgsConstructor
public class ToPage implements BotPage, Action {
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final OrderService orderService;
    private final UserService userService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.CityTo.matches(input)) {
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.ORDER_SEATS, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUserLocale(update);
        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.DISTANCE_TO, locale))
                        .replyMarkup(chatKeyboardFactory.toCityKeyboard(locale))
                        .build());
    }

    @Override
    public void update(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String toCity = UpdateUtil.getInput(update);
            Orders orderByChatId = orderService.findOrderByChatIdInCache(update.getCallbackQuery().getMessage().getChatId());
            orderByChatId.setTo_city(toCity);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.ORDER_TO,
                UserTypeEnum.PASSENGER
        );
    }
}

package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.OrdersCacheService;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;


@Component
@RequiredArgsConstructor
public class OrderSeatsPage implements BotPage {
    private final OrdersCacheService ordersCacheService;
    private final KeyboardFactory keyboardFactory;
    private final MessageFactory messageFactory;
    private final UserService userService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.SaveSeats.matches(input) && isValidCountSeats(update)) {
            return PageIdGenerator.generate(BotPageStageEnum.ORDER_DATE, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        int seatsCount = modifySeatsCount(update);
        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_SEATS, locale))
                        .replyMarkup(keyboardFactory.seatsKeyboard(locale, seatsCount))
                        .build()
        );
    }

    public int modifySeatsCount(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        Orders orders = ordersCacheService.get(chatId);
        Integer seatsCount = orders.getSeatsCount();
        ButtonEnum buttonEnum = ButtonEnum.getButton(update.getCallbackQuery().getData());
        if (buttonEnum == null) {
            return seatsCount;
        }
        switch (buttonEnum) {
            case MINUS -> {
                if (seatsCount > 1 && seatsCount <= 4) {
                    seatsCount--;
                }
            }
            case PLUS -> {
                if (seatsCount >= 0 && seatsCount < 4) {
                    seatsCount++;
                }
            }
        }
        orders.setSeatsCount(seatsCount);
        ordersCacheService.put(orders);
        return seatsCount;
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.ORDER_SEATS,
                UserTypeEnum.PASSENGER
        );
    }

    private boolean isValidCountSeats(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        Orders orders = ordersCacheService.get(chatId);
        Integer seatsCount = orders.getSeatsCount();
        return seatsCount >= 1 && seatsCount <= 4;
    }

}

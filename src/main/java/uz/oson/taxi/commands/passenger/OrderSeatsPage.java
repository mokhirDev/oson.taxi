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
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;


@Component
@RequiredArgsConstructor
public class OrderSeatsPage implements BotPage {
    private final OrdersCacheService ordersCacheService;
    private final KeyboardFactory keyboardFactory;
    private final MessageFactory messageFactory;
    private final UserStateService userService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        int seatsCount = modifySeatsCount(update);
        userService.setCurrentPage(BotPageStageEnum.ORDER_SEATS, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_SEATS, locale))
                        .replyMarkup(keyboardFactory.seatsKeyboard(locale, seatsCount))
                        .build()
        );
    }

    public int modifySeatsCount(Update update) {
        Long chatId = userService.getChatId(update);
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
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.ORDER_SEATS) {
            return true;
        }
        if (currentPage == BotPageStageEnum.ORDER_TO) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.SEATS_CODE), update);
        }
        return false;
    }

}

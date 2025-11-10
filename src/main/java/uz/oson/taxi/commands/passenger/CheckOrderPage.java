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
import uz.oson.taxi.service.LocalizationService;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckOrderPage implements BotPage, OrderAction {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final OrderService orderService;
    private final LocalizationService localizationService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.CHECK_ORDER, locale);
        updateOrder(update);
        userService.setCurrentPage(BotPageStageEnum.CHECK_ORDER, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(fillOrderDetails(pageMessage, chatId, locale))
                        .replyMarkup(keyboardFactory.checkOrderKeyboard(locale))
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

    @Override
    public void updateOrder(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.TEXT) {
            String comment = InputType.extractValue(update, inputType);
            Long chatId = userService.getChatId(update);
            Orders orderByChatId = orderService.findOrderByChatIdInCache(chatId);
            orderByChatId.setComment(comment);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    private String getValue(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.COMMENT) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.SKIP_COMMENT, PageCommandEnum.ADD_COMMENT), update);
        }
        return false;
    }
}

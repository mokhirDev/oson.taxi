package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.OrderAction;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageCodeEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckOrderPage implements BotPage, OrderAction {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final OrderService orderService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.CHECK_ORDER, locale);
        updateOrder(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(fillOrderDetails(pageMessage, chatId, locale))
                        .replyMarkup(keyboardFactory.checkOrderKeyboard(locale))
                        .build()
        );
    }

    private String fillOrderDetails(String pageMessage, Long chatId, LocaleEnum locale) {
        Orders order = orderService.findOrderByChatId(chatId);
        return pageMessage.formatted(
                getValue(order.getFrom_city()),
                getValue(order.getTo_city()),
                getValue(getDateDetails(order.getLeavingDate(), locale)),
                getValue(order.getSeatsCount().toString()),
                getValue(order.getComment())
        );
    }

    @Override
    public void updateOrder(Update update) {
        InputType inputType = InputType.valueOf(update.getMessage().getText());
        if (inputType == InputType.TEXT) {
            String comment = InputType.extractValue(update, inputType);
            Long chatId = update.getCallbackQuery().getFrom().getId();
            Orders orderByChatId = orderService.findOrderByChatId(chatId);
            orderByChatId.setComment(comment);
            orderService.updateOrder(orderByChatId);
        }
    }

    private String getValue(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private String getDateDetails(String day, LocaleEnum localeEnum) {
        DateTimeFormatter textFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", localeEnum.getLocale());
        LocalDate localDate = LocalDate.parse(day, textFormatter);
        return localDate.format(textFormatter);
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.CHECK_ORDER_CODE, update);
    }
}

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
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentPage implements BotPage, OrderAction {
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final OrderService orderService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        updateOrder(update);
        userService.setCurrentPage(BotPageStageEnum.COMMENT, chatId);

        return List.of(
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(messageFactory.getPageMessage(PageMessageEnum.COMMENT, locale))
                        .replyMarkup(keyboardFactory.commentKeyboard(locale))
                        .build()
        );

    }

    @Override
    public void updateOrder(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String leavingDate = InputType.extractValue(update, inputType);
            Long chatId = userService.getChatId(update);
            Orders orderByChatId = orderService.findOrderByChatIdInCache(chatId);
            orderByChatId.setLeavingDate(leavingDate);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.ORDER_DATE) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.COMMENT), update);
        }
        return false;
    }
}

package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.OrderAction;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.OrderService;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmOrderPage implements BotPage, OrderAction {
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final UserStateService userService;
    private final OrderService orderService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.CONFIRM_ORDER, chatId);
        updateOrder(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.CONFIRM_ORDER, locale))
                        .replyMarkup(keyboardFactory.backToMainMenuKeyboard(locale))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.CHECK_ORDER) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.CONFIRM_ORDER_CODE), update);
        }
        return false;
    }

    @Override
    public void updateOrder(Update update) {
        Long chatId = userService.getChatId(update);
        orderService.confirmOrder(chatId);
    }
}

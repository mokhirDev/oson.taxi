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
public class CommentPage implements BotPage, Action {
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final OrderService orderService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Text.matches(input) || RegExEnum.SkipComment.matches(input)) {
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.CHECK_ORDER, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        return List.of(
                SendMessage
                        .builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.COMMENT, locale))
                        .replyMarkup(chatKeyboardFactory.commentKeyboard(locale))
                        .build()
        );

    }

    @Override
    public void update(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.TEXT) {
            Long chatId = UpdateUtil.getChatId(update);
            String input = UpdateUtil.getInput(update);
            Orders orderByChatId = orderService.findOrderByChatIdInCache(chatId);
            orderByChatId.setComment(input);
            orderService.updateOrderInCache(orderByChatId);
        }
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.COMMENT,
                UserTypeEnum.PASSENGER
        );
    }
}

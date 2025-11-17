package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.ChatKeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmOrderPage implements BotPage {
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final UserService userService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Home.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.PASSENGER_MENU, UserTypeEnum.PASSENGER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        LocaleEnum locale = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.CONFIRM_ORDER, chatId);
        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.CONFIRM_ORDER, locale))
                        .replyMarkup(chatKeyboardFactory.backToMainMenuKeyboard(locale))
                        .build()
        );
    }


    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.CONFIRM_ORDER,
                UserTypeEnum.PASSENGER
        );
    }
}

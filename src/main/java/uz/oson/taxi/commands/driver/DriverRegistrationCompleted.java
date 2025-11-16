package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverRegistrationCompleted implements BotPage {
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Home.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.DRIVER_MENU, UserTypeEnum.DRIVER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.DRIVER_REGISTRATION_DONE, chatId);
        userService.setSecondName(update);
        userService.pending(user);
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.DRIVER_REGISTRATION_DONE, locale);
        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(pageMessage.formatted(user.getFirstName()))
                        .replyMarkup(keyboardFactory.backToMainMenuKeyboard(locale))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.DRIVER_REGISTRATION_DONE,
                UserTypeEnum.DRIVER
        );
    }
}

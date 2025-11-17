package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.Action;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.ChatKeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverRegistrationCheck implements BotPage, Action {

    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        update(update);
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
        userService.setCurrentPage(BotPageStageEnum.DRIVER_REGISTRATION_COMPLETED, chatId);

        userService.pending(user);
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.DRIVER_REGISTRATION_DONE, locale);
        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(pageMessage.formatted(user.getFirstName()))
                        .replyMarkup(chatKeyboardFactory.backToMainMenuKeyboard(locale))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.DRIVER_REGISTRATION_COMPLETED,
                UserTypeEnum.DRIVER
        );
    }

    @Override
    public void update(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CALLBACK) {
            String driverSecondName = UpdateUtil.getInput(update);
            Long chatId = UpdateUtil.getChatId(update);
            UserState user = userService.getUser(chatId);
            user.setSecondName(driverSecondName);
        }
    }
}

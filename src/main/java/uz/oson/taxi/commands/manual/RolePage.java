package uz.oson.taxi.commands.manual;

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
public class RolePage implements BotPage {

    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Passenger.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.PASSENGER_MENU, UserTypeEnum.PASSENGER);
        } else if (RegExEnum.Driver.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.DRIVER_MENU, UserTypeEnum.DRIVER);
        }
        userService.setRole(update, UpdateUtil.getChatId(update));
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        LocaleEnum locale = userService.getUserLocale(update);
        Long chatId = UpdateUtil.getChatId(update);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.ROLE, locale))
                        .replyMarkup(chatKeyboardFactory.roleKeyboard(locale))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(BotPageStageEnum.ROLE, UserTypeEnum.GUEST);
    }

    @Override
    public String previousPage() {
        return PageIdGenerator.generate(BotPageStageEnum.START, UserTypeEnum.GUEST);
    }
}
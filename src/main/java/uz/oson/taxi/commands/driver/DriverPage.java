package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
public class DriverPage implements BotPage {
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.BecomeDriver.matches(input)) {
            return PageIdGenerator.generate(BotPageStageEnum.WRITE_NAME, UserTypeEnum.DRIVER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        InlineKeyboardMarkup inlineKeyboard;
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.DRIVER_MENU, locale);
        if (user.getIsVerified().equals(Verification.VERIFIED)) {
            inlineKeyboard = chatKeyboardFactory.searchPassengers(locale);
        } else if (user.getIsVerified().equals(Verification.PENDING)) {
            inlineKeyboard = chatKeyboardFactory.verificationPending(locale);
            pageMessage = messageFactory.getPageMessage(PageMessageEnum.PENDING_VERIFICATION, locale);
        } else {
            inlineKeyboard = chatKeyboardFactory.becomeDriver(locale);
        }

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(pageMessage)
                        .replyMarkup(inlineKeyboard)
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.DRIVER_MENU,
                UserTypeEnum.DRIVER
        );
    }

    @Override
    public String previousPage() {
        return PageIdGenerator.generate(BotPageStageEnum.ROLE, UserTypeEnum.GUEST);
    }
}

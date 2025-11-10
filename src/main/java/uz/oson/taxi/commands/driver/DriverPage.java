package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.DRIVER_MENU, chatId);
        InlineKeyboardMarkup inlineKeyboard;
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.DRIVER_MENU, locale);
        if (user.getIsVerified().equals(Verification.VERIFIED)) {
            inlineKeyboard = keyboardFactory.searchPassengers(locale);
        } else if (user.getIsVerified().equals(Verification.PENDING)) {
            inlineKeyboard = keyboardFactory.verificationPending(locale);
            messageFactory.getPageMessage(PageMessageEnum.PENDING_VERIFICATION, locale);
        } else {
            inlineKeyboard = keyboardFactory.becomeDriver(locale);
        }

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(pageMessage)
                        .replyMarkup(inlineKeyboard)
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.DRIVER_REGISTRATION_DONE) {
            return true;
        }
        if (currentPage == BotPageStageEnum.DRIVER_MENU) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.HOME_CODE), update);
        }
        return PageCommandEnum.isValid(List.of(PageCommandEnum.DRIVER_CODE), update);
    }
}

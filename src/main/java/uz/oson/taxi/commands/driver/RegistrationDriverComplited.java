package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.BotPageStageEnum;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegistrationDriverComplited implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.DRIVER_REGISTRATION_DONE, chatId);
        userService.setSecondName(update);
        userService.pending(user);
        String pageMessage = messageFactory.getPageMessage(PageMessageEnum.DRIVER_REGISTRATION_DONE, locale);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(pageMessage.formatted(user.getFirstName()))
                        .replyMarkup(keyboardFactory.backToMainMenuKeyboard(locale))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (Objects.equals(InputType.getInputType(update), InputType.TEXT)) {
            return currentPage.equals(BotPageStageEnum.WRITE_NAME);
        }
        return false;
    }
}

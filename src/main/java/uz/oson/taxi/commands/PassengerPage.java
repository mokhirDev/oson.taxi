package uz.oson.taxi.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageCodeEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.UserStateService;
import uz.oson.taxi.util.KeyboardFactory;
import uz.oson.taxi.util.MessageFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PassengerPage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        setUserRole(update, chatId);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.PASSENGER, localeEnum))
                        .replyMarkup(keyboardFactory.passengerMenuKeyboard(localeEnum))
                        .build()
        );
    }

    void setUserRole(Update update, Long chatId) {
        userService.setRole(update, chatId);
    }

    @Override
    public boolean isValid(Update update) {
        return PageCodeEnum.isValid(PageCodeEnum.PASSENGER_CODE, update);
    }
}

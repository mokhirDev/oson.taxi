package uz.oson.taxi.commands.passenger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.BotPageStageEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageCommandEnum;
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
        Long chatId = userService.getChatId(update);
        setUserRole(update, chatId);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.PASSENGER_MENU, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.PASSENGER_MENU, localeEnum))
                        .replyMarkup(keyboardFactory.passengerMenuKeyboard(localeEnum))
                        .build()
        );
    }

    void setUserRole(Update update, Long chatId) {
        userService.setRole(update, chatId);
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.CONFIRM_ORDER || currentPage == BotPageStageEnum.MY_ORDERS) {
            return true;
        }
        if (currentPage == BotPageStageEnum.ROLE) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.PASSENGER_CODE), update);
        }
        else if (currentPage == BotPageStageEnum.CHECK_ORDER) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.CANCEL_ORDER_CODE), update);
        }
        return false;
    }
}

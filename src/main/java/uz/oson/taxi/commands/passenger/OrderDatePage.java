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
public class OrderDatePage implements BotPage {

    private final KeyboardFactory keyboardFactory;
    private final MessageFactory messageFactory;
    private final UserStateService userService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.ORDER_DATE, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.ORDER_DATE, localeEnum))
                        .replyMarkup(keyboardFactory.createDateKeyboard(localeEnum, 7))
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.ORDER_SEATS) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.ORDER_DATE_CODE), update);
        }
        return false;
    }
}

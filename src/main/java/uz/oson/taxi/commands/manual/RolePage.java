package uz.oson.taxi.commands.manual;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class RolePage implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        LocaleEnum localeEnum = userService.getUser(chatId).getLocale();
        userService.setCurrentPage(BotPageStageEnum.ROLE, chatId);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.ROLE, localeEnum))
                        .replyMarkup(
                                keyboardFactory
                                        .roleKeyboard(localeEnum)
                        )
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        if (currentPage == BotPageStageEnum.LANGUAGE) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.BACK_CODE), update);
        }
        if (currentPage == BotPageStageEnum.DRIVER_MENU || currentPage == BotPageStageEnum.PASSENGER_MENU) {
            return PageCommandEnum.isValid(List.of(PageCommandEnum.BACK_CODE), update);
        }
        return false;
    }
}

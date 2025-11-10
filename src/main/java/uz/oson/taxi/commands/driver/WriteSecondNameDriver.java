package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.UserState;
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
public class WriteSecondNameDriver implements BotPage {
    private final UserStateService userService;
    private final MessageFactory messageFactory;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = userService.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.WRITE_NAME, chatId);
        userService.setFirstName(update);
        return List.of(
                SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(messageFactory.getPageMessage(PageMessageEnum.WRITE_SECOND_NAME, locale))
                        .replyMarkup(null)
                        .build()
        );
    }

    @Override
    public boolean isValid(Update update) {
        Long chatId = userService.getChatId(update);
        BotPageStageEnum currentPage = userService.getCurrentPage(chatId);
        return currentPage.equals(BotPageStageEnum.BECOME_DRIVER);
    }
}

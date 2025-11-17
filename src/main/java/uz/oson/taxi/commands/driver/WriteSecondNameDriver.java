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
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WriteSecondNameDriver implements BotPage, Action {
    private final UserService userService;
    private final MessageFactory messageFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.Text.matches(input)){
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.SHARE_CONTACT, UserTypeEnum.DRIVER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.WRITE_SECOND_NAME, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.WRITE_SECOND_NAME, locale))
                        .replyMarkup(null)
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.WRITE_SECOND_NAME,
                UserTypeEnum.DRIVER
        );
    }

    @Override
    public void update(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.TEXT) {
            String driverSecondName = UpdateUtil.getInput(update);
            Long chatId = UpdateUtil.getChatId(update);
            UserState user = userService.getUser(chatId);
            user.setSecondName(driverSecondName);
        }
    }
}

package uz.oson.taxi.commands.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.interfaces.Action;
import uz.oson.taxi.entity.UserState;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.ChatKeyboardFactory;
import uz.oson.taxi.util.MessageFactory;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShareContactPage implements BotPage, Action {
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;
    private final UserService userService;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        if (RegExEnum.ShareContact.matches(input)) {
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.DESTINATION_FROM_CITY, UserTypeEnum.DRIVER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum localeEnum = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.SHARE_CONTACT, chatId);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(messageFactory.getPageMessage(PageMessageEnum.SHARE_CONTACT, localeEnum))
                        .replyMarkup(chatKeyboardFactory.shareContactKeyboard(localeEnum))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.SHARE_CONTACT,
                UserTypeEnum.DRIVER
        );
    }

    @Override
    public void update(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == InputType.CONTACT) {
            String phoneNumber = UpdateUtil.getInput(update);
            Long chatId = UpdateUtil.getChatId(update);
            UserState user = userService.getUser(chatId);
            user.setPhoneNumber(phoneNumber);
        }
    }

}

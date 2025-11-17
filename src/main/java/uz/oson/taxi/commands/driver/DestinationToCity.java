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
import uz.oson.taxi.util.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationToCity implements BotPage, Action {
    private final PageNavigator navigator;
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final ChatKeyboardFactory chatKeyboardFactory;

    @Override
    public String nextPage(Update update) {
        String input = UpdateUtil.getInput(update);
        CityEnum city = CityEnum.getCity(input);
        if (city != null) {
            update(update);
            return PageIdGenerator.generate(BotPageStageEnum.DRIVER_REGISTRATION_COMPLETED, UserTypeEnum.DRIVER);
        }
        return getPageId();
    }

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        UserState user = userService.getUser(chatId);
        LocaleEnum locale = user.getLocale();
        userService.setCurrentPage(BotPageStageEnum.DESTINATION_TO_CITY, chatId);

        String message = messageFactory.getPageMessage(PageMessageEnum.SELECT_ROUTE, locale) +
                messageFactory.getPageMessage(PageMessageEnum.DISTANCE_FROM, locale);

        return List.of(
                SendMessage.builder()
                        .chatId(String.valueOf(chatId))
                        .text(message)
                        .replyMarkup(chatKeyboardFactory.routes(locale))
                        .build()
        );
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(
                BotPageStageEnum.DESTINATION_TO_CITY,
                UserTypeEnum.DRIVER
        );
    }

    @Override
    public void update(Update update) {
        String input = UpdateUtil.getInput(update);
        CityEnum city = CityEnum.getCity(input);
        navigator.addDriverRoute(UpdateUtil.getChatId(update), city);
    }
}

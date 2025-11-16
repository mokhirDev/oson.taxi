package uz.oson.taxi.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.RegExEnum;
import uz.oson.taxi.service.UserService;
import uz.oson.taxi.util.PageIdGenerator;
import uz.oson.taxi.util.PageNavigator;
import uz.oson.taxi.util.UpdateUtil;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UpdateHandler {
    private final UserService userService;
    private final PageNavigator navigator;

    // Проверка доступности сообщения перенесена сюда
    public boolean isAvailableMessage(Update update) {
        Integer incomingMessageId = UpdateUtil.getCallBackMessageId(update);
        if (incomingMessageId == null) return true;

        Long chatId = UpdateUtil.getChatId(update);
        Integer lastSaved = navigator.getLastMessage(chatId);
        if (lastSaved == null) return true;

        // messageId больше значит более новое
        return incomingMessageId >= lastSaved;
    }

    public List<BotApiMethod<?>> handle(Update update) {
        List<BotApiMethod<?>> result = new ArrayList<>();

        Long chatId = UpdateUtil.getChatId(update);
        String input = UpdateUtil.getInput(update);

        // Если это BACK - возвращаемся
        if (RegExEnum.Back.matches(input)) {
            String prev = navigator.goBack(chatId);
            result.addAll(navigator.goTo(prev, update));
            return result;
        }

        // Если старт / совпадает паттерн Start — перейти на старт
        if (RegExEnum.Start.matches(input) || navigator.current(chatId) == null) {
            // reset credentials & history
            userService.setNewCredentials(chatId);
            navigator.clearHistory(chatId);
            result.addAll(navigator.goTo(PageIdGenerator.startPageId(), update));
            return result;
        }

        // Иначе — обычный маршрут: найти текущую страницу и обработать
        String currentPage = navigator.current(chatId);
        result.addAll(navigator.goTo(currentPage, update));
        return result;
    }

    public void saveLastMessageId(Message message) {
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        navigator.setLastMessage(chatId, messageId);
    }

}

package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.config.PageRegistry;
import uz.oson.taxi.service.NavigationHistoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageNavigator {
    private static final int MAX_STORED_MESSAGE_IDS = 100;

    private final PageRegistry registry;
    private final NavigationHistoryService history;

    public List<BotApiMethod<?>> goTo(String pageId, Update update) {
        BotPage page = registry.getPage(pageId);
        Long chatId = UpdateUtil.getChatId(update);

        // получить id следующей страницы от текущей страницы (если page==null — fallback to start)
        String nextPageId = (page != null) ? page.nextPage(update) : PageIdGenerator.startPageId();

        // push nextPageId в историю (stack)
        history.push(chatId, nextPageId);

        // желательно удалить старые сообщений, которые мы хотим убрать — формируем list delete
        List<Integer> toDelete = history.consumeAll(chatId); // возвращает список messageIds для удаления
        List<BotApiMethod<?>> deletes = buildDeleteMethods(chatId, toDelete);

        // Возвращаем сначала удаления — затем рендер новой страницы
        List<BotApiMethod<?>> result = new ArrayList<>(deletes);
        BotPage nextPage = registry.getPage(nextPageId);
        if (nextPage != null) result.addAll(nextPage.handle(update));
        return result;
    }

    private List<BotApiMethod<?>> buildDeleteMethods(Long chatId, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<BotApiMethod<?>> out = new ArrayList<>();
        for (Integer id : ids) {
            if (id != null) out.add(new DeleteMessage(chatId.toString(), id));
        }
        return out;
    }

    public String goBack(Long chatId) {
        String current = history.current(chatId);
        String previousPageId = "";
        if (current != null) {
            history.rightPop(chatId);
            previousPageId = registry.getPage(current).previousPage();
        }
        return previousPageId;
    }

    public void clearHistory(Long chatId) {
        history.clear(chatId);
    }

    public String current(Long chatId) {
        return history.current(chatId);
    }

    public Integer getLastMessage(Long chatId) {
        return history.peekLastMessage(chatId);
    }

    public void setLastMessage(Long chatId, Integer messageId) {
        history.pushMessageId(chatId, messageId, MAX_STORED_MESSAGE_IDS);
    }
}

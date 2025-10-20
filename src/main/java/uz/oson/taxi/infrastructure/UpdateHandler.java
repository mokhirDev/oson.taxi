package uz.oson.taxi.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.entity.enums.PageCodeEnum;
import uz.oson.taxi.service.UserStateService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateHandler {

    private final ApplicationContext context; // Spring-контекст для получения бинов BotPage
    private final UserStateService userStateService;

    public List<BotApiMethod<?>> handle(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return null;
        Long chatId = userStateService.getChatId(update);
        PageCodeEnum currentPage = userStateService.getCurrentPage(chatId);

        if (currentPage != null) {
            Class<? extends BotPage> clazz = currentPage.getClazz();
            BotPage bean = context.getBean(clazz);
            if (bean.isValid(update)) {
                return bean.handle(update);
            }
        }
        return null;
    }
}


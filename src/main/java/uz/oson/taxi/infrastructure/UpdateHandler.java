package uz.oson.taxi.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.BotPage;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.entity.enums.PageCodeEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateHandler {

    private final ApplicationContext context; // Spring-контекст для получения бинов BotPage

    public List<BotApiMethod<?>> handle(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return null;

        String value = extractValue(update, inputType); // текст или callback
        BotPage page = findPage(value, inputType);
        if (page != null) {
            return page.handle(update);
        }
        return null;
    }

    private String extractValue(Update update, InputType inputType) {
        switch (inputType) {
            case TEXT -> {
                if (update.hasMessage() && update.getMessage().hasText()) {
                    return update.getMessage().getText();
                }
            }
            case CALLBACK -> {
                if (update.hasCallbackQuery()) {
                    return update.getCallbackQuery().getData();
                }
            }
            case CONTACT -> {
                if (update.hasMessage() && update.getMessage().hasContact()) {
                    return update.getMessage().getContact().getPhoneNumber();
                }
            }
        }
        return null;
    }

    private BotPage findPage(String value, InputType inputType) {
        if (value == null) return null;

        for (PageCodeEnum pageEnum : PageCodeEnum.values()) {
            // проверяем тип ввода
            if (pageEnum.getInputType() != inputType) continue;

            // проверяем RegEx
            if (value.matches(pageEnum.getCodeRegEx().getRegEx())) {
                // создаем бин страницы из Spring-контекста
                Class<? extends BotPage> clazz = pageEnum.getClazz();
                if (clazz == null) return null;
                return context.getBean(clazz);
            }
        }
        return null;
    }
}


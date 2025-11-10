package uz.oson.taxi.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.InputType;
import uz.oson.taxi.service.BotPageInitializerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateHandler {

    private final BotPageInitializerService botPageInitializerService;

    public List<BotApiMethod<?>> handle(Update update) {
        InputType inputType = InputType.getInputType(update);
        if (inputType == null) return null;
        BotPage botPage = botPageInitializerService.getBotPage(update);
        if (botPage != null) {
            return botPage.handle(update);
        }
        return null;
    }
}


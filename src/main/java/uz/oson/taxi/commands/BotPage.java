package uz.oson.taxi.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public interface BotPage {

    String getName();

    List<BotApiMethod<?>> handle(Update update);

    boolean supports(Update update);

    default List<String> getAliases() {
        return new ArrayList<>();
    }
}


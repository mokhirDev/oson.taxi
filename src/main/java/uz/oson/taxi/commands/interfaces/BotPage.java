package uz.oson.taxi.commands.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotPage {

    List<BotApiMethod<?>> handle(Update update);

    boolean isValid(Update update);

}


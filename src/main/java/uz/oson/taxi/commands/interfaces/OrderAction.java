package uz.oson.taxi.commands.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface OrderAction {
    void updateOrder(Update update);
}

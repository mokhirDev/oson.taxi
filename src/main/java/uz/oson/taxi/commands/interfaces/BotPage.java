package uz.oson.taxi.commands.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.enums.BotPageStageEnum;
import uz.oson.taxi.entity.enums.UserTypeEnum;
import uz.oson.taxi.util.PageIdGenerator;

import java.util.List;

public interface BotPage {

    /**
     * Проверяем, может ли эта страница обработать вход
     */
    default String nextPage(Update update) {
        return getPageId();
    }

    default String previousPage() {
        return PageIdGenerator.generate(BotPageStageEnum.START, UserTypeEnum.PASSENGER);
    }

    /**
     * Основная логика страницы
     */
    default List<BotApiMethod<?>> handle(Update update) {
        return List.of();
    }

    /**
     * Уникальный ID страницы
     */
    String getPageId();

}


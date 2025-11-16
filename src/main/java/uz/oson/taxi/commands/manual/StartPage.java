package uz.oson.taxi.commands.manual;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.entity.enums.*;
import uz.oson.taxi.util.PageIdGenerator;

@Component
@RequiredArgsConstructor
public class StartPage implements BotPage {

    @Override
    public String nextPage(Update update) {
        return PageIdGenerator.generate(BotPageStageEnum.LANGUAGE, UserTypeEnum.GUEST);
    }

    @Override
    public String getPageId() {
        return PageIdGenerator.generate(BotPageStageEnum.START, UserTypeEnum.GUEST);
    }
}

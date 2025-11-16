package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.manual.StartPage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotPageInitializerService {
    private final List<BotPage> botPages;
//
//    public BotPage getBotPage(Update update) {
//        Optional<StartPage> startPageOpt = botPages.stream()
//                .filter(page -> page instanceof StartPage)
//                .map(page -> (StartPage) page)
//                .findFirst();
//        if (startPageOpt.isPresent()) {
//            return startPageOpt.get();
//        }
//        for (BotPage botPage : botPages) {
//            if (botPage.isValid(update)) {
//                return botPage;
//            }
//        }
//        return null;
//    }
}

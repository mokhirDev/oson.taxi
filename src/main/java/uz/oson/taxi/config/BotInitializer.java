package uz.oson.taxi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.oson.taxi.infrastructure.OsonTaxiBot;

@Configuration
public class BotInitializer {

    private final OsonTaxiBot osonTaxiBot;

    public BotInitializer(OsonTaxiBot osonTaxiBot) {
        this.osonTaxiBot = osonTaxiBot;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(osonTaxiBot);
        return telegramBotsApi;
    }
}


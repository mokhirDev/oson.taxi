package uz.oson.taxi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram.bots")
@Getter
@Setter
public class BotConfig {
    private String botUsername;
    private String botToken;
}

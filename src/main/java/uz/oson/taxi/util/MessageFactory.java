package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageEnum;
import uz.oson.taxi.service.LocalizationService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageFactory {
    private final LocalizationService localeService;

    public String getPageMessage(PageEnum pageEnum, LocaleEnum localeEnum) {
        if (!localeEnum.equals(LocaleEnum.UNKNOWN)) {
            Locale locale = Locale.of(localeEnum.name());
            return groupingMessages(pageEnum, locale).toString();
        } else {
            return combineAllLanguages(pageEnum);
        }
    }

    public String getButtonText(ButtonEnum buttonEnum, LocaleEnum localeEnum) {
        Locale locale = Locale.of(localeEnum.name());
        return localeService.getLocaleText(buttonEnum.getButtonTextCode(), locale);
    }

    private String combineAllLanguages(PageEnum pageEnum) {
        return String.valueOf(groupingMessages(pageEnum, Locale.of(LocaleEnum.UZ.getName()))) +
                groupingMessages(pageEnum, Locale.of(LocaleEnum.RU.getName())) +
                groupingMessages(pageEnum, Locale.of(LocaleEnum.EN.getName()));
    }

    private StringBuilder groupingMessages(PageEnum pageEnum, Locale locale) {
        StringBuilder groupedMessages = new StringBuilder();
        for (String code : pageEnum.getMessageCodes()) {
            groupedMessages.append(localeService.getLocaleText(code, locale));
            groupedMessages.append("\n");
        }
        return groupedMessages;
    }

}

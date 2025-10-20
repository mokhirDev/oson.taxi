package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;
import uz.oson.taxi.entity.enums.PageMessageEnum;
import uz.oson.taxi.service.LocalizationService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageFactory {
    private final LocalizationService localeService;

    public String getPageMessage(PageMessageEnum pageMessageEnum, LocaleEnum localeEnum) {
        if (!localeEnum.equals(LocaleEnum.UNKNOWN)) {
            Locale locale = Locale.of(localeEnum.name());
            return groupingMessages(pageMessageEnum, locale).toString();
        } else {
            return combineAllLanguages(pageMessageEnum);
        }
    }

    public String getButtonText(ButtonEnum buttonEnum, LocaleEnum localeEnum) {
        Locale locale = Locale.of(localeEnum.name());
        return localeService.getLocaleText(buttonEnum.getButtonTextCode(), locale);
    }

    private String combineAllLanguages(PageMessageEnum pageMessageEnum) {
        return String.valueOf(groupingMessages(pageMessageEnum, Locale.of(LocaleEnum.UZ.getName()))) +
                groupingMessages(pageMessageEnum, Locale.of(LocaleEnum.RU.getName())) +
                groupingMessages(pageMessageEnum, Locale.of(LocaleEnum.EN.getName()));
    }

    private StringBuilder groupingMessages(PageMessageEnum pageMessageEnum, Locale locale) {
        StringBuilder groupedMessages = new StringBuilder();
        for (String code : pageMessageEnum.getMessageCodes()) {
            groupedMessages.append(localeService.getLocaleText(code, locale));
            groupedMessages.append("\n");
        }
        return groupedMessages;
    }

}

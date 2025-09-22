package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocalizationService {

    private final MessageSource messageSource;

    public String getLocaleText(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

}

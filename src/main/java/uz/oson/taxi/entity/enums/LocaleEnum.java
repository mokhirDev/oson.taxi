package uz.oson.taxi.entity.enums;

import lombok.Getter;

import java.util.Locale;


@Getter
public enum LocaleEnum {
    UZ, EN, RU, UNKNOWN;

    public String getName() {
        return this.name().toLowerCase();
    }

    public static LocaleEnum getLocaleEnum(String language) {
        return LocaleEnum.valueOf(language.toUpperCase());
    }

    public Locale getLocale() {
        String name = this.getName();
        return Locale.forLanguageTag(name);
    }
}

package uz.oson.taxi.entity.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum AliasesEnum {
    LANG(List.of("uz", "ru", "en")),
    CITY(List.of("kokand", "tashkent")),
    SEATS(List.of("minus", "plus", "seats_count")),
    ORDER_DATE(List.of("save_seats_count")),;
    private final List<String> values;

    AliasesEnum(List<String> values) {
        this.values = values;
    }
}

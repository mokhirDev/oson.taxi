package uz.oson.taxi.entity.enums;


import lombok.Getter;

import java.util.List;

@Getter
public enum CityEnum implements BaseEnum {
    TASHKENT("city.tashkent", "tashkent", ButtonTypeEnum.INLINE),
    TASHKENT_REGION("city.tashkent.region", "tashkent_region", ButtonTypeEnum.INLINE),
    SIRDARIA("city.sirdaria", "sirdaria", ButtonTypeEnum.INLINE),
    SAMARKAND("city.samarkand", "samarkand", ButtonTypeEnum.INLINE),
    BUKHARA("city.bukhara", "bukhara", ButtonTypeEnum.INLINE),
    KHOREZM("city.khorezm", "khorezm", ButtonTypeEnum.INLINE), // Хива — центр Хорезмской области
    FERGANA("city.fergana", "fergana", ButtonTypeEnum.INLINE),
    NAMANGAN("city.namangan", "namangan", ButtonTypeEnum.INLINE),
    ANDIJAN("city.andijan", "andijan", ButtonTypeEnum.INLINE),
    KARAKALPAKSTAN("city.karakalpakistan", "karakalpakistan", ButtonTypeEnum.INLINE), // Нукус — центр
    KASHKADARIA("city.kashkadaria", "kashkadaria", ButtonTypeEnum.INLINE),
    SURKHANDARIA("city.surkhandaria", "surkhandaria", ButtonTypeEnum.INLINE),
    NAVOI("city.navoi", "navoi", ButtonTypeEnum.INLINE);

    private final String buttonTextCode;
    private final String buttonCallBack;
    private final ButtonTypeEnum buttonTypeEnum;

    CityEnum(String buttonTextCode, String buttonCallBack, ButtonTypeEnum buttonTypeEnum) {
        this.buttonTextCode = buttonTextCode;
        this.buttonCallBack = buttonCallBack;
        this.buttonTypeEnum = buttonTypeEnum;
    }

    public static List<CityEnum> getCities() {
        return List.of(values());
    }

    public static CityEnum getCity(String city) {
        for (CityEnum cityEnum : CityEnum.values()) {
            if (cityEnum.getButtonCallBack().equals(city)) {
                return cityEnum;
            }
        }
        return null;
    }
}


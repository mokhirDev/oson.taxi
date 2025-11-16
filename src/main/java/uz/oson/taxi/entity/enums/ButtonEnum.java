package uz.oson.taxi.entity.enums;


import lombok.Getter;

@Getter
public enum ButtonEnum {

    // ---------------- Inline Buttons ----------------
    MINUS("btn.seats.minus", "seat:minus", ButtonTypeEnum.INLINE),
    SEATS_COUNT("btn.current.seats", "seats_count", ButtonTypeEnum.INLINE),
    PLUS("btn.seats.plus", "seat:plus", ButtonTypeEnum.INLINE),
    SAVE_SEATS("btn.save", "save_seats_count", ButtonTypeEnum.INLINE),

    UZ_BUTTON("uz.flag", "uz", ButtonTypeEnum.INLINE),
    RU_BUTTON("ru.flag", "ru", ButtonTypeEnum.INLINE),
    EN_BUTTON("en.flag", "en", ButtonTypeEnum.INLINE),

    DRIVER_BUTTON("btn.driver", "driver", ButtonTypeEnum.INLINE),
    PASSENGER_BUTTON("btn.passenger", "passenger", ButtonTypeEnum.INLINE),
    BACK_BUTTON("btn.back", "back", ButtonTypeEnum.INLINE),

    BECOME_DRIVER("become.driver", "become_driver", ButtonTypeEnum.INLINE),
    SEARCH_PASSENGERS("search.passengers", "search_passengers", ButtonTypeEnum.INLINE),
    JOIN_GROUP("join.to.group", "join_to_group", ButtonTypeEnum.INLINE),

    CREATE_ORDER("btn.create.order", "create_order", ButtonTypeEnum.INLINE),
    MY_ORDERS("btn.my.orders", "my_orders", ButtonTypeEnum.INLINE),

    SKIP_COMMENT("btn.skip", "skip_comment", ButtonTypeEnum.INLINE),

    CONFIRM_ORDER("btn.confirm", "confirm_order", ButtonTypeEnum.INLINE),
    CANCEL_ORDER("btn.cancel", "cancel_order", ButtonTypeEnum.INLINE),

    HOME("btn.main.menu", "main_menu", ButtonTypeEnum.INLINE),

    KOKAND("kokand", "kokand", ButtonTypeEnum.INLINE),
    TASHKENT("tashkent", "tashkent", ButtonTypeEnum.INLINE),
    // ---------------- Reply Buttons ----------------
    SHARE_CONTACT("btn.share.contact", "share_contact", ButtonTypeEnum.REPLY);

    private final String buttonTextCode;
    private final String buttonCallBack;
    private final ButtonTypeEnum buttonTypeEnum;

    ButtonEnum(String buttonTextCode, String buttonCallBack, ButtonTypeEnum buttonTypeEnum) {
        this.buttonTextCode = buttonTextCode;
        this.buttonCallBack = buttonCallBack;
        this.buttonTypeEnum = buttonTypeEnum;
    }

    public static ButtonEnum getButton(String buttonCallBack) {
        for (ButtonEnum buttonEnum : ButtonEnum.values()) {
            if (buttonEnum.getButtonCallBack().equals(buttonCallBack)) {
                return buttonEnum;
            }
        }
        return null;
    }
}


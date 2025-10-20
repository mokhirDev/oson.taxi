package uz.oson.taxi.entity.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PageMessageEnum {

    START("start.greeting"),
    LANG("choose.language"),
    ROLE("choose.option"),
    DRIVER("driver.menu"),
    PASSENGER("passenger.menu"),
    CREATE_ORDER("create.order"),
    SHARE_CONTACT("share.contact"),
    CONTACT_RECEIVED("contact.received"),
    ORDER_FROM("left.from"),
    ORDER_TO("arrive.to"),
    ORDER_DATE("departure.date"),
    ORDER_SEATS("seats"),
    COMMENT("order.comment"),
    CHECK_ORDER("check.order", "order.full.detail"),
    CONFIRM_ORDER("confirm.order");

    private final List<String> messageCodes;

    PageMessageEnum(String... messageCodes) {
        this.messageCodes = Arrays.asList(messageCodes);
    }
}
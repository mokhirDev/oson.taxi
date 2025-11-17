package uz.oson.taxi.entity.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum PageMessageEnum {

    START("start.greeting"),
    LANG("choose.language"),
    ROLE("choose.option"),
    DRIVER_MENU("driver.menu"),
    PENDING_VERIFICATION("registration.passenger.pending"),
    WRITE_NAME("write.name"),
    WRITE_SECOND_NAME("write.second.name"),
    DRIVER_REGISTRATION_DONE("registration.driver.completed"),
    SELECT_DISTANCE("driver.select.distance.subtitle"),
    SELECT_ROUTE("driver.select.distance.title"),
    DISTANCE_CONFIRM("driver.select.distance.confirm"),
    DISTANCE_CANCEL("driver.select.distance.cancel"),
    PASSENGER_MENU("passenger.menu"),
    CREATE_ORDER("create.order"),
    SHARE_CONTACT("share.contact"),
    CONTACT_RECEIVED("contact.received"),
    DISTANCE_FROM("left.from"),
    DISTANCE_TO("arrive.to"),
    ORDER_DATE("departure.date"),
    ORDER_SEATS("seats"),
    COMMENT("order.comment"),
    CHECK_ORDER("check.order", "order.full.detail"),
    CONFIRM_ORDER("confirm.order"),
    LINE("line"),
    MY_ORDERS("my.orders"),
    ORDER("order"),;

    private final List<String> messageCodes;

    PageMessageEnum(String... messageCodes) {
        this.messageCodes = Arrays.asList(messageCodes);
    }
}
package uz.oson.taxi.entity.enums;

import lombok.Getter;

@Getter
public enum RegExEnum {

    StartCode("^/start$"),
    LangCode("^(uz|ru|en)$"),
    PassengerCode("^(passenger)$"),
    DriverCode("^(driver)$"),
    CreateOrderCode("^(create_order)$"),
    MyOrdersCode("^(my_orders)$"),
    ShareContactCode("^\\+998\\d{9}$"),
    CityFromCode("^from:.*$"),
    CityToCode("^to:.*$"),
    SeatsCode("^seat:.*$"),
    SaveSeatsCode("^(save_seats_count)$"),
    DateCode("^(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[0-2])$"),
    SkipCommentCode("^(skip_comment)$"),
    CommentCode("^.+$"),
    CancelOrderCode("^(cancel)$"),
    ConfirmOrderCode("^(confirm_order)$");

    private final String regEx;

    RegExEnum(String regEx) {
        this.regEx = regEx;
    }

}
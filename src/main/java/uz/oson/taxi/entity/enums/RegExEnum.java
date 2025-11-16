package uz.oson.taxi.entity.enums;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public enum RegExEnum {

    Start("^/start$"),
    Back("^back"),
    Home("^main_menu"),
    Lang("^(uz|ru|en)$"),
    Passenger("^(passenger|client:.*)$"),
    Driver("^(driver)$"),
    BecomeDriver("^become_driver$"),
    CreateOrder("^(create_order)$"),
    MyOrders("^(my_orders)$"),
    ShareContact("^\\+998\\d{9}$"),
    CityFrom("^.*$"),
    CityTo("^.*$"),
    Seats("^seat:.*$"),
    SaveSeats("^(save_seats_count)$"),
    Date("^(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[0-2])$"),
    SkipComment("^(skip_comment)$"),
    Text("^.+$"),
    CancelOrder("^(cancel_order)$"),
    ConfirmOrder("^(confirm_order)$");

    private final Pattern pattern;

    RegExEnum(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public boolean matches(String text) {
        return text != null && pattern.matcher(text).matches();
    }

}
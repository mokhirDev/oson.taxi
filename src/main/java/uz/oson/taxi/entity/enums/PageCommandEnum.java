package uz.oson.taxi.entity.enums;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.driver.WriteName;
import uz.oson.taxi.commands.driver.DriverPage;
import uz.oson.taxi.commands.interfaces.BotPage;
import uz.oson.taxi.commands.manual.LangPage;
import uz.oson.taxi.commands.manual.StartPage;
import uz.oson.taxi.commands.passenger.*;

import java.util.List;

@Getter
public enum PageCommandEnum {

    START_CODE(InputType.TEXT, RegExEnum.Start, StartPage.class),
    BACK_CODE(InputType.CALLBACK, RegExEnum.Back, null),
    HOME_CODE(InputType.CALLBACK, RegExEnum.Home, null),
    LANG_CODE(InputType.CALLBACK, RegExEnum.Lang, LangPage.class),
    ROLE(InputType.CALLBACK, RegExEnum.Lang, LangPage.class),
    DRIVER_CODE(InputType.CALLBACK, RegExEnum.Driver, DriverPage.class),
    BECOME_DRIVER(InputType.CALLBACK, RegExEnum.BecomeDriver, WriteName.class),
    PASSENGER_CODE(InputType.CALLBACK, RegExEnum.Passenger, PassengerPage.class),
    MY_ORDERS_CODE(InputType.CALLBACK, RegExEnum.MyOrders, null),
    CREATE_ORDER_CODE(InputType.CALLBACK, RegExEnum.CreateOrder, CreatePage.class),
    CITY_FROM_CODE(InputType.CALLBACK, RegExEnum.CityFrom, OrderFromPage.class),
    CITY_TO_CODE(InputType.CALLBACK, RegExEnum.CityTo, ToPage.class),
    SEATS_CODE(InputType.CALLBACK, RegExEnum.Seats, OrderSeatsPage.class),
    ORDER_DATE_CODE(InputType.CALLBACK, RegExEnum.SaveSeats, DatePage.class),
    COMMENT(InputType.CALLBACK, RegExEnum.Date, CommentPage.class),
    SKIP_COMMENT(InputType.CALLBACK, RegExEnum.SkipComment, CheckPage.class),
    CANCEL_ORDER_CODE(InputType.CALLBACK, RegExEnum.CancelOrder, DriverPage.class),
    CONFIRM_ORDER_CODE(InputType.CALLBACK, RegExEnum.ConfirmOrder, ConfirmOrderPage.class);

    private final InputType inputType;
    private final RegExEnum codeRegEx;
    private final Class<? extends BotPage> clazz;

    PageCommandEnum(InputType inputType, RegExEnum codeRegEx, Class<? extends BotPage> clazz) {
        this.inputType = inputType;
        this.codeRegEx = codeRegEx;
        this.clazz = clazz;
    }

    public static boolean isValid(List<PageCommandEnum> pageCommandEnums, Update update) {
        InputType inputType = InputType.getInputType(update);
        assert inputType != null;
        for (PageCommandEnum pageCommandEnum : pageCommandEnums) {
//            if (pageCommandEnum.getInputType().equals(inputType)) {
//                RegExEnum codeRegEx = pageCommandEnum.getCodeRegEx();
//                if (codeRegEx.equals(RegExEnum.SkipRegEx)) {
//                    return true;
//                } else {
//                    String extractedValue = InputType.extractValue(update, inputType);
//                    return codeRegEx.matches(extractedValue);
//                }
//
//            }
        }
        return false;
    }
}
package uz.oson.taxi.entity.enums;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.commands.*;
import uz.oson.taxi.commands.interfaces.BotPage;

import java.util.Objects;

@Getter
public enum PageCodeEnum {

    START_CODE(InputType.TEXT, RegExEnum.StartCode, StartPage.class),
    LANG_CODE(InputType.CALLBACK, RegExEnum.LangCode, LangPage.class),
    DRIVER_CODE(InputType.CALLBACK, RegExEnum.DriverCode, DriverPage.class),
    PASSENGER_CODE(InputType.CALLBACK, RegExEnum.PassengerCode, PassengerPage.class),
    MY_ORDERS_CODE(InputType.CALLBACK, RegExEnum.MyOrdersCode, null),
    CREATE_ORDER_CODE(InputType.CALLBACK, RegExEnum.CreateOrderCode, CreateOrderPage.class),
    SHARE_CONTACT_CODE(InputType.CONTACT, RegExEnum.ShareContactCode, ShareContactPage.class),
    CITY_FROM_CODE(InputType.CALLBACK, RegExEnum.CityFromCode, OrderFromPage.class),
    CITY_TO_CODE(InputType.CALLBACK, RegExEnum.CityToCode, OrderToPage.class),
    SEATS_CODE(InputType.CALLBACK, RegExEnum.SeatsCode, OrderSeatsPage.class),
    ORDER_DATE_CODE(InputType.CALLBACK, RegExEnum.SaveSeatsCode, OrderDatePage.class),
    COMMENT(InputType.CALLBACK, RegExEnum.DateCode, CommentPage.class),
    CHECK_ORDER_CODE(InputType.CALLBACK, RegExEnum.SkipCommentCode, CheckOrderPage.class),
    CANCEL_ORDER_CODE(InputType.CALLBACK, RegExEnum.SkipCommentCode, DriverPage.class),
    CONFIRM_ORDER_CODE(InputType.CALLBACK, RegExEnum.ConfirmOrderCode, ConfirmOrderPage.class);

    private final InputType inputType;
    private final RegExEnum codeRegEx;
    private final Class<? extends BotPage> clazz;

    PageCodeEnum(InputType inputType, RegExEnum codeRegEx, Class<? extends BotPage> clazz) {
        this.inputType = inputType;
        this.codeRegEx = codeRegEx;
        this.clazz = clazz;
    }

    public static boolean isValid(PageCodeEnum pageCodeEnum, Update update) {
        InputType inputType = InputType.getInputType(update);
        assert inputType != null;
        String extractedValue = InputType.extractValue(update, inputType);
        return pageCodeEnum.getInputType().equals(inputType) &&
                Objects.requireNonNull(extractedValue).trim().matches(pageCodeEnum.getCodeRegEx().getRegEx());
    }
}
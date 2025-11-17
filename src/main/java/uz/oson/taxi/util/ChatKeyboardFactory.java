package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.CityEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatKeyboardFactory {
    private final KeyboardUtil keyboardUtil;

    // ------------------ Inline Keyboards ------------------

    public InlineKeyboardMarkup seatsKeyboard(LocaleEnum localeEnum, int count) {
        InlineKeyboardButton seatsCount = keyboardUtil.button(ButtonEnum.SEATS_COUNT, localeEnum);
        seatsCount.setText(String.valueOf(count));


        List<InlineKeyboardButton> seatsRow = List.of(
                keyboardUtil.button(ButtonEnum.MINUS, localeEnum),
                seatsCount,
                keyboardUtil.button(ButtonEnum.PLUS, localeEnum)
        );

        List<List<InlineKeyboardButton>> rows = List.of(
                seatsRow,
                List.of(keyboardUtil.button(ButtonEnum.SAVE_SEATS, localeEnum))
        );

        return KeyboardUtil.inlineKeyboard(rows);
    }

    public InlineKeyboardMarkup createDateKeyboard(LocaleEnum localeEnum, int daysCount) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter textFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", localeEnum.getLocale());
        DateTimeFormatter callbackFormatter = DateTimeFormatter.ofPattern("dd.MM");

        for (int i = 0; i < daysCount; i++) {
            LocalDate date = today.plusDays(i);
            String text = date.format(textFormatter);
            String callback = date.format(callbackFormatter);

            rows.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(text)
                            .callbackData(callback)
                            .build()
            ));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    public InlineKeyboardMarkup languageKeyboard() {
        return KeyboardUtil.inlineKeyboard(List.of(
                List.of(
                        keyboardUtil.button(ButtonEnum.UZ_BUTTON, LocaleEnum.UZ),
                        keyboardUtil.button(ButtonEnum.EN_BUTTON, LocaleEnum.EN),
                        keyboardUtil.button(ButtonEnum.RU_BUTTON, LocaleEnum.RU)
                )
        ));
    }

    public InlineKeyboardMarkup roleKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.DRIVER_BUTTON, ButtonEnum.PASSENGER_BUTTON),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup passengerMenuKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.CREATE_ORDER),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.MY_ORDERS),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup commentKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.SKIP_COMMENT)
        ));
    }

    public InlineKeyboardMarkup checkOrderKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.CONFIRM_ORDER),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.CANCEL_ORDER)
        ));
    }

    public InlineKeyboardMarkup verificationPending(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup becomeDriver(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BECOME_DRIVER),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup searchPassengers(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.SEARCH_PASSENGERS),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup backToMainMenuKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.HOME)
        ));
    }

    public InlineKeyboardMarkup routes(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, CityEnum.TASHKENT),
                keyboardUtil.inlineRow(localeEnum, CityEnum.TASHKENT_REGION),
                keyboardUtil.inlineRow(localeEnum, CityEnum.SIRDARIA),
                keyboardUtil.inlineRow(localeEnum, CityEnum.SAMARKAND),
                keyboardUtil.inlineRow(localeEnum, CityEnum.BUKHARA),
                keyboardUtil.inlineRow(localeEnum, CityEnum.FERGANA),
                keyboardUtil.inlineRow(localeEnum, CityEnum.NAMANGAN),
                keyboardUtil.inlineRow(localeEnum, CityEnum.ANDIJAN),
                keyboardUtil.inlineRow(localeEnum, CityEnum.NAVOI),
                keyboardUtil.inlineRow(localeEnum, CityEnum.KASHKADARIA),
                keyboardUtil.inlineRow(localeEnum, CityEnum.SURKHANDARIA),
                keyboardUtil.inlineRow(localeEnum, CityEnum.KHOREZM),
                keyboardUtil.inlineRow(localeEnum, CityEnum.KARAKALPAKSTAN)
        ));
    }

    // ------------------ Reply Keyboards ------------------

    public ReplyKeyboardMarkup shareContactKeyboard(LocaleEnum localeEnum) {
        KeyboardRow keyboardRow = keyboardUtil.replyRow(localeEnum, ButtonEnum.SHARE_CONTACT);
        keyboardRow.getFirst().setRequestContact(true);
        return KeyboardUtil.replyKeyboard(
                List.of(keyboardRow),
                true
        );
    }

    public ReplyKeyboardRemove cleanReplyKeyboard() {
        return KeyboardUtil.cleanReplyKeyboard();
    }


}

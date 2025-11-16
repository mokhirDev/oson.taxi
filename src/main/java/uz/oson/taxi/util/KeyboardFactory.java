package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardFactory {

    private final MessageFactory messageFactory;

    // ------------------ Inline Keyboards ------------------

    public InlineKeyboardMarkup seatsKeyboard(LocaleEnum localeEnum, int count) {
        InlineKeyboardButton seatsCount = button(ButtonEnum.SEATS_COUNT, localeEnum);
        seatsCount.setText(String.valueOf(count));


        List<InlineKeyboardButton> seatsRow = List.of(
                button(ButtonEnum.MINUS, localeEnum),
                seatsCount,
                button(ButtonEnum.PLUS, localeEnum)
        );

        List<List<InlineKeyboardButton>> rows = List.of(
                seatsRow,
                List.of(button(ButtonEnum.SAVE_SEATS, localeEnum))
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
                        button(ButtonEnum.UZ_BUTTON, LocaleEnum.UZ),
                        button(ButtonEnum.EN_BUTTON, LocaleEnum.EN),
                        button(ButtonEnum.RU_BUTTON, LocaleEnum.RU)
                )
        ));
    }

    public InlineKeyboardMarkup roleKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.DRIVER_BUTTON, ButtonEnum.PASSENGER_BUTTON),
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup driverMenuKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.JOIN_GROUP),
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup passengerMenuKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.CREATE_ORDER),
                inlineRow(localeEnum, ButtonEnum.MY_ORDERS),
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup fromCityKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.KOKAND, ButtonEnum.TASHKENT)
        ));
    }

    public InlineKeyboardMarkup toCityKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.KOKAND, ButtonEnum.TASHKENT)
        ));
    }

    public InlineKeyboardMarkup commentKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.SKIP_COMMENT)
        ));
    }

    public InlineKeyboardMarkup checkOrderKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.CONFIRM_ORDER),
                inlineRow(localeEnum, ButtonEnum.CANCEL_ORDER)
        ));
    }

    public InlineKeyboardMarkup verificationPending(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup becomeDriver(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.BECOME_DRIVER),
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup searchPassengers(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.SEARCH_PASSENGERS),
                inlineRow(localeEnum, ButtonEnum.BACK_BUTTON)
        ));
    }

    public InlineKeyboardMarkup backToMainMenuKeyboard(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                inlineRow(localeEnum, ButtonEnum.HOME)
        ));
    }

    // ------------------ Reply Keyboards ------------------

    public ReplyKeyboardMarkup shareContactKeyboard(LocaleEnum localeEnum) {
        KeyboardRow keyboardRow = replyRow(localeEnum, ButtonEnum.SHARE_CONTACT);
        keyboardRow.getFirst().setRequestContact(true);
        return KeyboardUtil.replyKeyboard(
                List.of(keyboardRow),
                true
        );
    }

    public ReplyKeyboardRemove cleanReplyKeyboard() {
        return KeyboardUtil.cleanReplyKeyboard();
    }

    // ------------------ Вспомогательные методы ------------------

    private InlineKeyboardButton button(ButtonEnum buttonEnum, LocaleEnum localeEnum) {
        return InlineKeyboardButton.builder()
                .text(messageFactory.getButtonText(buttonEnum, localeEnum))
                .callbackData(buttonEnum.getButtonCallBack())
                .build();
    }

    private List<InlineKeyboardButton> inlineRow(LocaleEnum localeEnum, ButtonEnum... buttons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (ButtonEnum button : buttons) {
            row.add(button(button, localeEnum));
        }
        return row;
    }

    private KeyboardRow replyRow(LocaleEnum localeEnum, ButtonEnum... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (ButtonEnum button : buttons) {
            row.add(new KeyboardButton(messageFactory.getButtonText(button, localeEnum)));
        }
        return row;
    }
}

package uz.oson.taxi.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.oson.taxi.entity.enums.ButtonEnum;
import uz.oson.taxi.entity.enums.LocaleEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupKeyboardFactory {
    private final KeyboardUtil keyboardUtil;

    public InlineKeyboardMarkup newDriverAccess(LocaleEnum localeEnum) {
        return KeyboardUtil.inlineKeyboard(List.of(
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.ACCEPT_DRIVER),
                keyboardUtil.inlineRow(localeEnum, ButtonEnum.DISMISS_DRIVER)
        ));
    }
}

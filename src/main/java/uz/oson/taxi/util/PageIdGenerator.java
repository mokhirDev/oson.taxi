package uz.oson.taxi.util;

import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.enums.BotPageStageEnum;
import uz.oson.taxi.entity.enums.UserTypeEnum;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@Service
public class PageIdGenerator {

    public static String generate(BotPageStageEnum stage, UserTypeEnum role) {
        String base = stage.name() + ":" + role.name();
        CRC32 crc = new CRC32();
        crc.update(base.getBytes(StandardCharsets.UTF_8));
        return Long.toHexString(crc.getValue());
    }

    public static String startPageId() {
        return generate(BotPageStageEnum.START, UserTypeEnum.GUEST);
    }
}

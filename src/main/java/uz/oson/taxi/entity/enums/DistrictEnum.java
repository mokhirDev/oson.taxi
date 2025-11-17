package uz.oson.taxi.entity.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum DistrictEnum implements BaseEnum {

    // ---------------------- TASHKENT CITY ----------------------
    CHILONZOR("district.chilonzor", "chilonzor", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    YUNUSOBOD("district.yunusobod", "yunusobod", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    YASHNOBOD("district.yashnobod", "yashnobod", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    MIROBOD("district.mirobod", "mirobod", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    SHAYXONTOHUR("district.shayxontohur", "shayxontohur", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    OLMYORT("district.olmayot", "olmayot", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    UCHTEPA("district.uchtepa", "uchtepa", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    SERGELI("district.sergeli", "sergeli", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),

    // ---------------------- TASHKENT REGION ----------------------
    ANGREN("district.angren", "angren", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    BEKTEMIR("district.bektemir", "bektemir", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    CHINOZ("district.chinoz", "chinoz", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    OQQORGON("district.oqqorgon", "oqqorgon", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    PARKENT("district.parkent", "parkent", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    OHANGARON("district.ohangaron", "ohangaron", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    YANGIYUL("district.yangiyul", "yangiyul", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    CHIRCHIK("district.chirchik", "chirchik", CityEnum.TASHKENT, ButtonTypeEnum.INLINE),
    OLMALYK("district.olmalyk", "olmalyk", CityEnum.TASHKENT, ButtonTypeEnum.INLINE), // добавлен Олмалык

    // ---------------------- SIRDARIA ----------------------
    GULISTAN("district.gulistan", "gulistan", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    BOYKENT("district.boykent", "boykent", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    SHIRIN("district.shirin", "shirin", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    MIRZACHUL("district.mirzachul", "mirzachul", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    OQDARYO("district.oqdaryo", "oqdaryo", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    SARDOBO("district.sardobo", "sardobo", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    SAYHUN("district.sayhun", "sayhun", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),
    YANGIARYK("district.yangiaryk", "yangiaryk", CityEnum.SIRDARIA, ButtonTypeEnum.INLINE),

    // ---------------------- SAMARKAND ----------------------
    BULUNGUR("district.bulungur", "bulungur", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    ISHTIXON("district.ishtixon", "ishtixon", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    KATTAKURGAN("district.kattakurgan", "kattakurgan", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    NARPAY("district.narpay", "narpay", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    PASTDARGOM("district.pastdargom", "pastdargom", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    PAYARIQ("district.payariq", "payariq", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    TAYLAK("district.taylak", "taylak", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),
    URGUT("district.urgut", "urgut", CityEnum.SAMARKAND, ButtonTypeEnum.INLINE),

    // ---------------------- FERGANA ----------------------
    KOKAND("district.kokand", "kokand", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    UCHKOPRIK("district.uchkoprik", "uchkoprik", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    MARGILAN("district.margilan", "margilan", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    BAGDOD("district.bogdod", "bogdod", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    BUVAIDA("district.buvaida", "buvaida", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    DANGARA("district.dangara", "dangara", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    FURKAT("district.furkat", "furkat", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    BESHARIQ("district.beshariq", "beshariq", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    OLTIARIQ("district.oltiariq", "oltiariq", CityEnum.FERGANA, ButtonTypeEnum.INLINE),
    YOZYOVON("district.yozyovon", "yozyovon", CityEnum.FERGANA, ButtonTypeEnum.INLINE),

    // ---------------------- NAMANGAN ----------------------
    CHUST("district.chust", "chust", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    CHORTAQ("district.chortaq", "chortaq", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    POP("district.pop", "pop", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    TURAKURGON("district.turakurgon", "turakurgon", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    NORIN("district.norin", "norin", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    UYCHI("district.uychi", "uychi", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    YANGIQURGHON("district.yangiqurgon", "yangiqurgon", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    KOSONSOY("district.kosonsoy", "kosonsoy", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    NAMANGAN_CITY("district.namangan_city", "namangan_city", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    MINGBULOQ("district.mingbuloq", "mingbuloq", CityEnum.NAMANGAN, ButtonTypeEnum.INLINE),
    // ---------------------- ANDIJAN ----------------------
    ASAKA("district.asaka", "asaka", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    BUSTON("district.buston", "buston", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    MARHAMAT("district.marhamat", "marhamat", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    KHONOBOD("district.khonobod", "khonobod", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    ANDIJAN_CITY("district.andijan_city", "andijan_city", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    PAKHTABAD("district.pakhtabad", "pakhtabad", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    BULOQBOSHI("district.buloqboshi", "buloqboshi", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    IZBOSKAN("district.izboskan", "izboskan", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    ULUGNOR("district.ulugnor", "ulugnor", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    BALIQCHI("district.baliqchi", "baliqchi", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    SHAHRIHAN("district.shahrihan", "shahrihan", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    KURGANTEPA("district.kurgantepa", "kurgantepa", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    XOJAOBOD("district.xojaobod", "xojaobod", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),
    OLTINKOL("district.oltinkol", "oltinkol", CityEnum.ANDIJAN, ButtonTypeEnum.INLINE),

    // ---------------------- KASHKADARIA ----------------------
    KASBI("district.kasbi", "kasbi", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    KOSON("district.koson", "koson", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    KASHKADARIA_CITY("district.KASHKADARIA_city", "KASHKADARIA_city", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    KITOB("district.kitob", "kitob", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    SHAKHRISABZ("district.shakhrisabz", "shakhrisabz", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    YAKKABOG("district.yakkabog", "yakkabog", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    DEHQONOBOD("district.dehqonobod", "dehqonobod", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    NISHON("district.nishon", "nishon", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    QAMASHI("district.qamashi", "qamashi", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    CHIROQCHI("district.chiroqchi", "chiroqchi", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    GUZAR("district.guzar", "guzar", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),
    MIRISHKOR("district.mirishkor", "mirishkor", CityEnum.KASHKADARIA, ButtonTypeEnum.INLINE),

    // ---------------------- Karakalpakistan ----------------------
    AMUDARYO("district.amudaryo", "amudaryo", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    KUNGRAD("district.kungrad", "kungrad", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    KARAKALPAKSTAN_CITY("district.KARAKALPAKSTAN_city", "KARAKALPAKSTAN_city", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    BERUNI("district.beruni", "beruni", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    KEGEYLI("district.kegeyli", "kegeyli", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    MANGIT("district.mangit", "mangit", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    MOYNOK("district.moynok", "moynok", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    QANLIKOL("district.qanlikol", "qanlikol", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    QORAOZAK("district.qoraozak", "qoraozak", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    SHUMANAY("district.shumanay", "shumanay", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    TAXTAKOPIR("district.taxtakopir", "taxtakopir", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    TURTKUL("district.turtkul", "turtkul", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),
    ELLIKKALA("district.ellikkala", "ellikkala", CityEnum.KARAKALPAKSTAN, ButtonTypeEnum.INLINE),

    // ---------------------- KHOREZM ----------------------
    KHANKA("district.khanka", "khanka", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    SHOVOT("district.shovot", "shovot", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    KHOREZM_CITY("district.KHOREZM_city", "KHOREZM_city", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    BOGOT("district.bogot", "bogot", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    GURLAN("district.gurlan", "gurlan", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    QUSHKOPIR("district.qushkopir", "qushkopir", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    YANGIARIQ("district.yangiariq", "yangiariq", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    YANGIBOZOR("district.yangibozor", "yangibozor", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),
    XIVA_CITY("district.khiva_city", "khiva_city", CityEnum.KHOREZM, ButtonTypeEnum.INLINE),

    // ---------------------- NAVOI ----------------------
    KONIMEX("district.konimex", "konimex", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    NUROTA("district.nurota", "nurota", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    NAVOI_CITY("district.navoi_city", "navoi_city", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    KARAKATA("district.karakata", "karakata", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    KHATIRCHI("district.khatirchi", "khatirchi", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    TOMDIO("district.tomdio", "tomdio", CityEnum.NAVOI, ButtonTypeEnum.INLINE),
    UCHKUDUK("district.uchkuduk", "uchkuduk", CityEnum.NAVOI, ButtonTypeEnum.INLINE);


    private final String buttonTextCode;
    private final String buttonCallBack;
    private final CityEnum city;
    private final ButtonTypeEnum buttonTypeEnum;

    DistrictEnum(String buttonTextCode, String buttonCallBack, CityEnum city, ButtonTypeEnum buttonTypeEnum) {
        this.buttonTextCode = buttonTextCode;
        this.buttonCallBack = buttonCallBack;
        this.city = city;
        this.buttonTypeEnum = buttonTypeEnum;
    }


    public static List<DistrictEnum> getDistricts() {
        return List.of(values());
    }

    public static List<DistrictEnum> getCityDistricts(CityEnum city) {
        for (DistrictEnum districtEnum : DistrictEnum.values()) {
            if (districtEnum.getCity() == city) {
                return List.of(districtEnum);
            }
        }
        return List.of();
    }
}

package uz.oson.taxi.entity.enums;

public enum UserTypeEnum {
    DRIVER,
    PASSENGER,
    GUEST;

    public static Boolean existRole(String role) {
        UserTypeEnum[] values = UserTypeEnum.values();
        for (UserTypeEnum value : values) {
            if (value.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}

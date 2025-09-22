package uz.oson.taxi.entity.enums;

import lombok.Getter;


@Getter
public enum AnswerTypeEnum {
    Text,
    CallBack,
    CallBackOfExpression,
    CallBackOfVariable,
    Date,
    Contact;
}

package kakao.springsecurity.global.common.customValid.validator;

import kakao.springsecurity.global.common.customValid.valid.ValidNickName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NickNameValidator implements ConstraintValidator<ValidNickName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= 2 && isValidString(value);
    }

    private boolean isValidString(String value) {
        // 숫자, 특수문자, 공백 제외 2~10자
        String regex = "^[a-zA-Z가-힣]{2,10}$";
        return value.matches(regex);
    }
}
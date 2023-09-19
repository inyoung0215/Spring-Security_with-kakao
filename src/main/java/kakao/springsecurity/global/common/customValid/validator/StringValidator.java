package kakao.springsecurity.global.common.customValid.validator;

import kakao.springsecurity.global.common.customValid.valid.ValidString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringValidator implements ConstraintValidator<ValidString, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^[a-zA-Z0-9]*$") && value.length() >= 1 && value.length() <= 10;
    }
}

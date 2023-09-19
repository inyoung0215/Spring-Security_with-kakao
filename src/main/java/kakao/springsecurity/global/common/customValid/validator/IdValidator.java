package kakao.springsecurity.global.common.customValid.validator;

import kakao.springsecurity.global.common.customValid.valid.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<ValidId, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        // null이 아니면서 양수인 값
        return value != null && value > 0;
    }
}
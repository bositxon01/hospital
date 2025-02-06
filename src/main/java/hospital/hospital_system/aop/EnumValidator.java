package hospital.hospital_system.aop;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private Enum<?>[] enumValues;

    @Override
    public void initialize(ValidEnum annotation) {
        enumValues = annotation.enumClass().getEnumConstants();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.asList(enumValues).contains(value);
    }
}


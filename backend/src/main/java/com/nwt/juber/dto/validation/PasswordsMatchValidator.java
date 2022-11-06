package com.nwt.juber.dto.validation;

import com.nwt.juber.dto.request.LocalRegistrationRequest;
import com.nwt.juber.dto.request.PasswordResetRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof LocalRegistrationRequest dto)
            return passwordsMatch(dto.getPassword(), dto.getPasswordConfirmation());
        if (o instanceof PasswordResetRequest dto)
            return passwordsMatch(dto.getPassword(), dto.getPasswordConfirmation());
        return false;
    }

    private Boolean passwordsMatch(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}

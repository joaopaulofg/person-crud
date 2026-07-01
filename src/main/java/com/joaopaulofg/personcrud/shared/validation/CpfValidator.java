package com.joaopaulofg.personcrud.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int firstDigit = calculateDigit(cpf.substring(0, 9), 10);
        int secondDigit = calculateDigit(cpf.substring(0, 9) + firstDigit, 11);

        return cpf.equals(cpf.substring(0, 9) + firstDigit + secondDigit);
    }

    private int calculateDigit(String base, int weight) {
        int sum = 0;

        for (char digit : base.toCharArray()) {
            sum += Character.getNumericValue(digit) * weight--;
        }

        int result = 11 - (sum % 11);

        return result > 9 ? 0 : result;
    }
}
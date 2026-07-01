package com.joaopaulofg.personcrud.person.service;

import org.springframework.stereotype.Component;

@Component
public class PersonNormalizer {

    public String normalizeName(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    public String normalizeEmail(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().toLowerCase();
    }

    public String normalizeDocumentNumber(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }

    public String normalizePhoneNumber(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }

    public String normalizeState(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().toUpperCase();
    }

    public String normalizeZipCode(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("\\D", "");
    }
}

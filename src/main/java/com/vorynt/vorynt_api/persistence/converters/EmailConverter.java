package com.vorynt.vorynt_api.persistence.converters;

import com.vorynt.vorynt_api.domain.user.valueObjects.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {
    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String databaseValue) {
        return Email.of(databaseValue);
    }
}

package com.chung.receiptsmanager.entity.attributeConverters;

import com.chung.receiptsmanager.exceptions.DatabaseConversionException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Currency;

@Slf4j
@Converter
public class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency currency) {
        if(currency == null) {
            throw new DatabaseConversionException(
                "Received a null Currency object and so unable to convert this to a String"
            );
        }

        return currency.getCurrencyCode();
    }

    @Override
    public Currency convertToEntityAttribute(String iso4217CurrencyCode) {
        if(iso4217CurrencyCode == null) {
            throw new DatabaseConversionException(
                "Received a null String and so unable to convert this to a Currency object"
            );
        }

        try {
            return Currency.getInstance(iso4217CurrencyCode);
        } catch(IllegalArgumentException ex) {
            throw new DatabaseConversionException(iso4217CurrencyCode + " does not conform to the ISO-4217 standard");
        }
    }
}

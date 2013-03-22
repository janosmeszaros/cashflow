package com.cashflow.database.parentdao;

import java.lang.reflect.Field;

import org.apache.commons.collections.Transformer;

import com.cashflow.exceptions.IllegalTableException;

/**
 * Transformer class to transform {@link Field} to {@link String}.
 * @author Janos_Gyula_Meszaros
 *
 */
public class FieldToStringTransformer implements Transformer {

    @Override
    public Object transform(Object input) {
        Field field = (Field) input;
        String result = "";

        try {
            result = (String) field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalTableException(field.getClass().getName(), field.getName());
        }

        return result;
    }
}

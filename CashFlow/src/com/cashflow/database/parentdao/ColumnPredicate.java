package com.cashflow.database.parentdao;

import java.lang.reflect.Field;

import org.apache.commons.collections.Predicate;

/**
 * Predicate class for select column names from table.
 * @author Janos_Gyula_Meszaros
 *
 */
public class ColumnPredicate implements Predicate {

    @Override
    public boolean evaluate(Object obj) {
        Field field = (Field) obj;

        return field.getName().contains("COLUMN_NAME");
    }

}

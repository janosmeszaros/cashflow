package com.cashflow.database;

/**
 * a
 * @author Janos_Gyula_Meszaros
 *
 */
public abstract class Tables {
    /**
     * Return table name.
     * @return table name.
     */
    abstract String getName();

    /**
     * Return nullable column name.
     * @return column name.
     */
    abstract String getNullableColumnName();

}

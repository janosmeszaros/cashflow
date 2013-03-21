package com.cashflow.database;

/**
 * a
 * @author Janos_Gyula_Meszaros
 *
 */
public interface Tables {
    /**
     * Return table name.
     * @return table name.
     */
    String getName();

    /**
     * Return nullable column name.
     * @return column name.
     */
    String getNullableColumnName();

}

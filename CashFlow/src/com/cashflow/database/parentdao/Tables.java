package com.cashflow.database.parentdao;

import java.util.Set;

/**
 * Marker interface for tables in database.
 * @author Janos_Gyula_Meszaros
 *
 */
public interface Tables {
    /**
     * Returns the datatable's name.
     * @return tableName
     */
    String getTableName();

    /**
     * Returns the projection of the table.
     * @return string array with the projection.
     */
    String[] getProjection();

    /**
     * Returns the columns in the table.
     * @return set of columns.
     */
    Set<String> getColumns();
}

package com.cashflow.exceptions;

/**
 * Exception class for illegal statement id. It is thrown when statement doesn't exist for the specified id.
 * @author Janos_Gyula_Meszaros
 *
 */
public class IllegalStatementIdException extends RuntimeException {

    private static final long serialVersionUID = 6379973083610727615L;

    /**
     * Constructor which gets the id which is not exist.
     * @param id id.
     */
    public IllegalStatementIdException(final String id) {
        super("The statement for the " + id + " can't find in the database.");
    }

}

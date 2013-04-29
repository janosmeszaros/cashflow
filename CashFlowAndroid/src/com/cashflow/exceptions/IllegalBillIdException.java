package com.cashflow.exceptions;

/**
 * Exception class for illegal bill id. It is thrown when bill doesn't exist for the specified id.
 * @author Janos_Gyula_Meszaros
 */
public class IllegalBillIdException extends RuntimeException {

    private static final long serialVersionUID = 6379973083610727615L;

    /**
     * Constructor which gets the id which is not exist.
     * @param billId
     *            id.
     */
    public IllegalBillIdException(final String billId) {
        super("The bill for the " + billId + " can't find in the database.");
    }

}

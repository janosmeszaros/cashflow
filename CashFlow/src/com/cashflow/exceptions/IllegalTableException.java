package com.cashflow.exceptions;

/**
 * Exception which will be throwed when wrong class passed to ParentDao.
 * @author Janos_Gyula_Meszaros
 *
 */
public class IllegalTableException extends RuntimeException {

    private static final long serialVersionUID = 6214074363365316363L;

    /**
     * Default constructor.
     * @param clazz class name.
     * @param name field/method name.
     */
    public IllegalTableException(final String clazz, final String name) {
        super("Public " + name + " did'nt find in " + clazz);
    }
}

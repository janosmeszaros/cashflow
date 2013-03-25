package com.cashflow.exceptions;

/**
 * Exception which will be throwed when wrong class passed to ParentDao.
 * @author Janos_Gyula_Meszaros
 *
 */
public class IllegalTableException extends RuntimeException {

    /**
     * Default constructor.
     * @param clazz class name.
     * @param name field/method name.
     */
    public IllegalTableException(String clazz, String name) {
        super("Public " + name + " did'nt find in " + clazz);
    }
}

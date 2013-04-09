package com.cashflow.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Category.
 * @author Kornel_Refi
 *
 */
public class Category {

    private final String categoryId;
    private final String name;

    /**
     * Constructor.
     * @param categoryId of {@link Category}
     * @param name of {@link Category}
     */
    public Category(final String categoryId, final String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public String getId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}

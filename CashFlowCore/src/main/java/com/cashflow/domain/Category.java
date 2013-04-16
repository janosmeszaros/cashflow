package com.cashflow.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Category.
 * @author Kornel_Refi
 */
public final class Category {

    private final String categoryId;
    private final String name;

    private Category(final Builder builder) {
        this.categoryId = builder.categoryId;
        this.name = builder.name;
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

    /**
     * Returns a builder for category.
     * @param categoryId
     *            id
     * @param name
     *            name
     * @return {@link Builder}
     */
    public static Builder builder(final String categoryId, final String name) {
        return new Builder(categoryId, name);
    }

    /**
     * Builder class for category.
     * @author Janos_Gyula_Meszaros
     */
    public static final class Builder {
        private final String categoryId;
        private final String name;

        /**
         * Constructor with mandatory params.
         * @param categoryId
         *            id
         * @param name
         *            name
         */
        private Builder(final String categoryId, final String name) {
            super();
            this.categoryId = categoryId;
            this.name = name;
        }

        /**
         * Builds the category.
         * @return {@link Category}
         */
        public Category build() {
            return new Category(this);
        }

    }
}

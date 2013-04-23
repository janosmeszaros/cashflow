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

    public String getCategoryId() {
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
     * @param name
     *            name
     * @return {@link Builder}
     */
    public static Builder builder(final String name) {
        return new Builder(name);
    }

    /**
     * Builder class for category.
     * @author Janos_Gyula_Meszaros
     */
    public static final class Builder {
        private static final String DEFAULT_NAME = "uncategorized";
        private String categoryId = "0";
        private String name = DEFAULT_NAME;

        /**
         * Constructor with mandatory parameters. If empty then set to <code>DEFAULT_NAME</code>.
         * @param name
         *            name
         */
        private Builder(final String name) {
            if (validateName(name)) {
                this.name = name;
            }
        }

        private boolean validateName(final String name) {
            return !name.isEmpty();
        }

        /**
         * Set ID for category if name is not default.
         * @param categoryId
         *            ID of {@link Category}.
         * @return {@link Builder}
         */
        public Builder categoryId(final String categoryId) {
            if (isNameNotDefault()) {
                this.categoryId = categoryId;
            }
            return this;
        }

        private boolean isNameNotDefault() {
            return !DEFAULT_NAME.equals(name);
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

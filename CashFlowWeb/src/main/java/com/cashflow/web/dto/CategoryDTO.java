package com.cashflow.web.dto;

import com.cashflow.domain.Category;

/**
 * DTO for category.
 * @author Janos_Gyula_Meszaros
 */
public class CategoryDTO {
    private String categoryId = "";
    private String name = "";

    /**
     * Convert DTO to Category.
     * @return category.
     */
    public Category convert() {
        return Category.builder(name).categoryId(categoryId).build();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(final String name) {
        this.name = name;
    }

}

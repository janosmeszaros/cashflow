package com.cashflow.web;

import java.beans.PropertyEditorSupport;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Category;
import com.cashflow.web.dto.CategoryDTO;

/**
 * Property editor for category.
 * @author Janos_Gyula_Meszaros
 */
@Component
public class CategoryPropertyEditor extends PropertyEditorSupport {

    @Autowired
    private CategoryDAO dao;
    @Autowired
    private Mapper mapper;

    @Override
    public void setAsText(final String text) {
        final Category category = dao.getCategoryById(text);
        final CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

        setValue(categoryDTO);
    }

}

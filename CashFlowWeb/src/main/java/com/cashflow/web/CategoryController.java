package com.cashflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.WordUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Category;
import com.cashflow.web.dto.CategoryDTO;

/**
 * Controller for category pages.
 * @author Kornel_Refi
 *
 */
@Controller
public class CategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CategoryPropertyEditor categoryPropertyEditor;

    /**
     * Set DataBinder.
     * @param dataBinder {@link WebDataBinder}
     */
    @InitBinder
    public void setDataBinder(final WebDataBinder dataBinder) {
        dataBinder.setAutoGrowNestedPaths(false);

        dataBinder.registerCustomEditor(CategoryDTO.class, categoryPropertyEditor);
    }

    /**
     * Selects the add_category page.
     * @param model
     *            {@link Model}
     * @return add_category
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.GET)
    public String addCategory(final Model model) {
        model.addAttribute("category", new CategoryDTO());

        return "add_category";
    }

    /**
     * Post an add_category page.
     * @param category {@link CategoryDTO}
     * @param model {@link Model}
     * @return string
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDTO> addCategory(final CategoryDTO category, final Model model) {
        LOGGER.info("Save category: " + category);

        final String categoryName = WordUtils.capitalize(category.getName());
        final Category categoryToSave = Category.builder(categoryName).categoryId(category.getCategoryId()).build();

        categoryDAO.save(categoryToSave);

        return convertToCategoryList(categoryDAO.getAllCategories());
        //        return "string";
    }

    /**
     * Selects the manage_categories page.
     * @param model {@link Model}
     * @param locale {@link Locale}
     * @return manage_categories
     */
    @RequestMapping(value = "/manage_categories", method = RequestMethod.GET)
    public String manageCategories(final Model model, final Locale locale) {
        LOGGER.info("Manage categories.");
        addCategories(model);

        putDataTableMessagesIntoTheModel(model, locale);
        model.addAttribute("category", new CategoryDTO());

        return "manage_categories";
    }

    /**
     * Post edit category modal window.
     * @param category {@link CategoryDTO} to update.
     * @param model {@link Model}
     * @return manage_categories
     */
    @RequestMapping(value = "/edit_category", method = RequestMethod.POST)
    @ResponseBody
    public String editCategory(final CategoryDTO category, final Model model) {
        LOGGER.info("Edit category. ");

        final Category categoryToSave = Category.builder(category.getName()).categoryId(category.getCategoryId()).build();

        categoryDAO.update(categoryToSave, categoryToSave.getCategoryId());

        return "";
    }

    private void addCategories(final Model model) {
        final List<Category> allCategories = categoryDAO.getAllCategories();
        model.addAttribute("categories", convertToCategoryList(allCategories));
    }

    private List<CategoryDTO> convertToCategoryList(final List<Category> list) {
        final List<CategoryDTO> categoryList = new ArrayList<CategoryDTO>();

        for (final Category entity : list) {
            final CategoryDTO category = convertToCategory(entity);
            categoryList.add(category);
        }

        return categoryList;
    }

    private CategoryDTO convertToCategory(final Category category) {
        return mapper.map(category, CategoryDTO.class);
    }

    private void putDataTableMessagesIntoTheModel(final Model model, final Locale locale) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("i18n.xml");
        final ReloadableResourceBundleMessageSource bean = (ReloadableResourceBundleMessageSource) applicationContext.getBean("messageSource");

        final String[] keys = new String[]{"sort_ascending", "sort_descending", "first", "last", "next", "previous", "empty_table", "info",
            "info_empty", "info_filtered", "info_postfix", "info_thousands", "length_menu", "loading_records", "processing", "search", "zero_records"};

        for (final String key : keys) {
            final String value = bean.getMessage("table." + key, null, locale);
            model.addAttribute(key, value);
        }

        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
}

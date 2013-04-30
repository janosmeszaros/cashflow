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
import com.cashflow.web.dto.BillDTO;
import com.cashflow.web.dto.CategoryDTO;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
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
     * Selects the home page and populates the model with a message.
     * @param model
     *            {@link Model}
     * @return home
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Model model) {
        LOGGER.info("Welcome home!");
        model.addAttribute("controllerMessage", "This is the message from the controller!");
        return "home";
    }

    /**
     * Selects the register page.
     * @param model
     *            {@link Model}
     * @return register
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "This is the message from the controller!");
        return "register";
    }

    /**
     * Selects the register page.
     * @param model
     *            {@link Model}
     * @return redirect to home
     */
    @RequestMapping(value = "/add_user", method = RequestMethod.GET)
    public String registerPost(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "muhaha");

        return "redirect:/";
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

    /**
     * Selects the add_bill page.
     * @param model
     *            {@link Model}
     * @return add_bill
     */
    @RequestMapping(value = "/add_bill", method = RequestMethod.GET)
    public String addBill(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("bill", new BillDTO());
        model.addAttribute("category", new CategoryDTO());
        addCategories(model);

        return "add_bill";
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
    public String addCategory(final CategoryDTO category, final Model model) {
        LOGGER.info("Save category: " + category);

        final String categoryName = WordUtils.capitalize(category.getName());
        final Category categoryToSave = Category.builder(categoryName).categoryId(category.getCategoryId()).build();

        categoryDAO.save(categoryToSave);

        //        addCategories(model);

        return "";
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

        return "manage_categories";
    }

    private void putDataTableMessagesIntoTheModel(final Model model, final Locale locale) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("i18n.xml");
        final ReloadableResourceBundleMessageSource bean = (ReloadableResourceBundleMessageSource) applicationContext.getBean("messageSource");

        final String[] keys = new String[]{"sort_ascending", "sort_descending", "first", "last", "next", "previous", "empty_table", "info",
            "info_empty", "info_filtered", "info_postfix", "info_thousands", "length_menu", "loading_records", "processing", "search", "zero_records"};

        for (final String key : keys) {
            final String value = bean.getMessage("table." + key, null, locale);
            LOGGER.debug("table." + key + " = " + value);
            model.addAttribute(key, value);
        }

        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
}

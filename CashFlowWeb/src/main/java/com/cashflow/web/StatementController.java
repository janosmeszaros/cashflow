package com.cashflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.cashflow.dao.CategoryDAO;
import com.cashflow.dao.StatementDAO;
import com.cashflow.domain.Category;
import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.cashflow.web.dto.CategoryDTO;
import com.cashflow.web.dto.StatementDTO;

/**
 * Controller for statement pages.
 * @author Kornel_Refi
 *
 */
@Controller
public class StatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementController.class);
    @Autowired
    private StatementDAO statementDAO;
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
     * Selects add_income page.
     * @param model
     *            {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_income", method = RequestMethod.GET)
    public String addIncome(final Model model) {
        LOGGER.info("Add Income.");
        final StatementDTO income = new StatementDTO();
        income.setType(StatementType.Income);
        model.addAttribute("category", new CategoryDTO());

        model.addAttribute("statement", income);
        model.addAttribute("is_income", true);
        addCategories(model);

        return "add_statement";
    }

    /**
     * Selects the add_expense page.
     * @param model
     *            {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_expense", method = RequestMethod.GET)
    public String addExpense(final Model model) {
        LOGGER.info("Add Expense.");
        final StatementDTO expense = new StatementDTO();
        expense.setType(StatementType.Expense);
        expense.setInterval(RecurringInterval.none);
        model.addAttribute("category", new CategoryDTO());

        model.addAttribute("statement", expense);
        model.addAttribute("is_income", false);
        addCategories(model);

        return "add_statement";
    }

    /**
     * Posts the add_statement page.
     * @param statement
     *            {@link StatementDTO}
     * @param model
     *            {@link Model}
     * @return redirect:/
     */
    @RequestMapping(value = "/add_statement", method = RequestMethod.POST)
    public String addStatement(final StatementDTO statement, final Model model) {
        String redirect;
        final Statement statementToSave = createStatement(statement);

        statementDAO.save(statementToSave);

        if (statementToSave.getType().isIncome()) {
            redirect = "redirect:/list_incomes";
        } else {
            redirect = "redirect:/list_expenses";
        }
        return redirect;
    }

    /**
     * Selects the list_incomes page.
     * @param model {@link Model}
     * @param locale {@link Locale}
     * @return list_statements
     */
    @RequestMapping(value = "/list_incomes", method = RequestMethod.GET)
    public String listIncomes(final Model model, final Locale locale) {

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("i18n.xml");
        final ReloadableResourceBundleMessageSource bean = (ReloadableResourceBundleMessageSource) applicationContext.getBean("messageSource");

        LOGGER.info("List incomes.");

        final String listIncomes = bean.getMessage("label.list_incomes", null, locale);
        model.addAttribute("legendLabel", listIncomes);
        model.addAttribute("statements", convertToStatementList(statementDAO.getIncomes()));

        putDataTableMessagesIntoTheModel(model, locale);

        ((ClassPathXmlApplicationContext) applicationContext).close();
        return "list_statements";
    }

    /**
     * Selects the list_expenses page.
     * @param model {@link Model}
     * @param locale {@link Locale}
     * @return list_statements
     */
    @RequestMapping(value = "/list_expenses", method = RequestMethod.GET)
    public String listExpenses(final Model model, final Locale locale) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("i18n.xml");
        final ReloadableResourceBundleMessageSource bean = (ReloadableResourceBundleMessageSource) applicationContext.getBean("messageSource");

        LOGGER.info("List expenses.");
        final String listExpenses = bean.getMessage("label.list_expenses", null, locale);
        model.addAttribute("legendLabel", listExpenses);
        model.addAttribute("statements", convertToStatementList(statementDAO.getExpenses()));

        putDataTableMessagesIntoTheModel(model, locale);

        ((ClassPathXmlApplicationContext) applicationContext).close();
        return "list_statements";
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

    private Statement createStatement(final StatementDTO statement) {
        final CategoryDTO categoryDTO = statement.getCategory();
        final Category category = Category.builder(categoryDTO.getName()).categoryId(categoryDTO.getCategoryId()).build();
        return Statement.builder(statement.getAmount(), statement.getDate()).note(statement.getNote()).type(statement.getType()).category(category)
                .recurringInterval(statement.getInterval()).build();
    }

    private List<StatementDTO> convertToStatementList(final List<Statement> list) {
        final List<StatementDTO> statementList = new ArrayList<StatementDTO>();

        for (final Statement entity : list) {
            final StatementDTO category = convertToStatement(entity);
            statementList.add(category);
        }

        return statementList;
    }

    private StatementDTO convertToStatement(final Statement statement) {
        return mapper.map(statement, StatementDTO.class);
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

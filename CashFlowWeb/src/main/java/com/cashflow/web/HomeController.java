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
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;
import com.cashflow.web.dto.BillDTO;
import com.cashflow.web.dto.CategoryDTO;
import com.cashflow.web.dto.StatementDTO;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
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

    /**
     * Selects the add_statement page.
     * @param model
     *            {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_income", method = RequestMethod.GET)
    public String addIncome(final Model model) {
        final StatementDTO income = new StatementDTO();
        income.setType(StatementType.Income);
        model.addAttribute("category", new CategoryDTO());

        model.addAttribute("statement", income);
        model.addAttribute("is_income", true);
        addCategories(model);

        return "add_statement";
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

    /**
     * Selects the add_statement page.
     * @param model
     *            {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_expense", method = RequestMethod.GET)
    public String addExpense(final Model model) {
        final StatementDTO expense = new StatementDTO();
        expense.setType(StatementType.Expense);
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

        final Statement statementToSave = createStatement(statement);

        statementDAO.save(statementToSave);
        return "redirect:/";
    }

    private Statement createStatement(final StatementDTO statement) {
        final CategoryDTO categoryDTO = statement.getCategory();
        final Category category = Category.builder(categoryDTO.getName()).categoryId(categoryDTO.getCategoryId()).build();
        return Statement.builder(statement.getAmount(), statement.getDate()).note(statement.getNote()).type(statement.getType()).category(category)
                .recurringInterval(statement.getInterval()).build();
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

        ((ClassPathXmlApplicationContext) applicationContext).close();
        return "list_statements";
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
     * @return to the home page
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public String addCategory(final CategoryDTO category, final Model model) {
        LOGGER.info("Save category: " + category);
        final Category categoryToSave = Category.builder(category.getName()).categoryId(category.getCategoryId()).build();

        categoryDAO.save(categoryToSave);

        return "redirect:/";
    }

    /**
     * Selects the manage_categories page.
     * @param model
     *            {@link Model}
     * @return manage_categories
     */
    @RequestMapping(value = "/manage_categories", method = RequestMethod.GET)
    public String manageCategories(final Model model) {
        LOGGER.info("Manage categories.");
        addCategories(model);

        return "manage_categories";
    }
}

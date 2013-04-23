package com.cashflow.web;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StatementDAO dao;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CategoryPropertyEditor categoryPropertyEditor;

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

        model.addAttribute("statement", expense);
        model.addAttribute("is_income", false);
        addCategories(model);

        return "add_statement";
    }

    /**
     * Selects the add_statement page.
     * @param statement
     *            {@link StatementDTO}
     * @param model
     *            {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_statement", method = RequestMethod.POST)
    public String addStatement(final StatementDTO statement, final Model model) {
        final Statement statementToSave = createStatement(statement);

        dao.save(statementToSave);
        return "redirect:/";
    }

    private Statement createStatement(final StatementDTO statement) {
        final CategoryDTO categoryDTO = statement.getCategory();
        final Category category = Category.builder(categoryDTO.getName()).categoryId(categoryDTO.getCategoryId()).build();
        return Statement.builder(statement.getAmount(), statement.getDate()).note(statement.getNote()).type(statement.getType())
                .category(category)
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

        return "add_bill";
    }

    /**
     * Selects the list page.
     * @param model
     *            {@link Model}
     * @return list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "List:");
        return "list";
    }

    /**
     * Selects the list page.
     * @param model
     *            {@link Model}
     * @return list
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.GET)
    public String addCategory(final Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "add_category";
    }

    /**
     * Anyád
     * @param category
     * @param model
     * @return
     */
    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public String addCategory(final CategoryDTO category, final Model model) {
        final Category categoryToSave = Category.builder(category.getName()).categoryId(category.getCategoryId()).build();

        categoryDAO.save(categoryToSave);

        return "redirect:/";
    }
}

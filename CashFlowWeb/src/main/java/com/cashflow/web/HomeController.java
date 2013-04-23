package com.cashflow.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cashflow.web.dto.BillDTO;
import com.cashflow.web.dto.CategoryDTO;
import com.cashflow.web.dto.StatementDTO;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    /**
     * Selects the home page and populates the model with a message.
     * @param model {@link Model}
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
     * @param model {@link Model}
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
     * @param model {@link Model}
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
     * @param model {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_income", method = RequestMethod.GET)
    public String addIncome(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("statement", new StatementDTO());
        model.addAttribute("is_income", true);
        final List<CategoryDTO> list = new ArrayList<CategoryDTO>();
        final CategoryDTO category = new CategoryDTO();
        category.setCategoryId("0");
        category.setName("cate");
        list.add(category);
        model.addAttribute("categories", list);

        return "add_statement";
    }

    /**
     * Selects the add_statement page.
     * @param model {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_expense", method = RequestMethod.GET)
    public String addExpense(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("statement", new StatementDTO());
        model.addAttribute("is_income", false);

        return "add_statement";
    }

    /**
     * Selects the add_statement page.
     * @param statement {@link StatementDTO}
     * @param model {@link Model}
     * @return add_statement
     */
    @RequestMapping(value = "/add_statement", method = RequestMethod.POST)
    public String addStatement(@RequestParam final StatementDTO statement, final Model model) {
        LOGGER.debug("Persist statement: " + statement.toString());

        return "redirect:/";
    }

    /**
     * Selects the add_bill page.
     * @param model {@link Model}
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
     * @param model {@link Model}
     * @return list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "List:");
        return "list";
    }
}

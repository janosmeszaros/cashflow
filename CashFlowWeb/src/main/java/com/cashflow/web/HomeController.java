package com.cashflow.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @RequestMapping(value = "/add_statement", method = RequestMethod.GET)
    public String addIncome(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("statement", new StatementDTO());

        return "add_statement";
    }

    /**
     * Selects the add_bill page.
     * @param model {@link Model}
     * @return add_bill
     */
    @RequestMapping(value = "/add_bill", method = RequestMethod.GET)
    public String addBill(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "Add Income:");

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

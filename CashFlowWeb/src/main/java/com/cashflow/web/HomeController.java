package com.cashflow.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "This is the message from the controller!");
        return "register";
    }

    /**
     * Selects the register page.
     */
    @RequestMapping(value = "/add_user", method = RequestMethod.GET)
    public String registerPost(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "muhaha");

        return "redirect:/";
    }

    /**
     * Selects the register page.
     */
    @RequestMapping(value = "/add_statement", method = RequestMethod.GET)
    public String addIncome(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "Add Income:");
        //        model.addAttribute("statement", Statement.class);

        return "add_statement";
    }

    /**
     * Selects the register page.
     */
    @RequestMapping(value = "/add_bill", method = RequestMethod.GET)
    public String addBill(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "Add Income:");

        return "add_bill";
    }

    /**
     * Selects the list page.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(final Model model) {
        LOGGER.info("Register");
        model.addAttribute("controllerMessage", "List:");
        return "list";
    }
}

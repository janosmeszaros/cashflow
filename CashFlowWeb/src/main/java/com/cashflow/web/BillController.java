package com.cashflow.web;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.cashflow.dao.BillDAO;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Bill;
import com.cashflow.domain.Category;
import com.cashflow.web.dto.BillDTO;
import com.cashflow.web.dto.CategoryDTO;

/**
 * Controller for bill pages.
 * @author Kornel_Refi
 *
 */
@Controller
public class BillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillController.class);
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private BillDAO billDAO;
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
     * Selects the add_bill page.
     * @param model
     *            {@link Model}
     * @return add_bill
     */
    @RequestMapping(value = "/add_bill", method = RequestMethod.GET)
    public String addBill(final Model model) {
        LOGGER.info("Add new bill form.");
        model.addAttribute("bill", new BillDTO());
        model.addAttribute("category", new CategoryDTO());
        addCategories(model);

        return "add_bill";
    }

    /**
     * Posts the add_bill page.
     * @param billDTO {@link BillDTO}
     * @param model {@link Model}
     * @return list_bills
     */
    @RequestMapping(value = "/add_bill", method = RequestMethod.POST)
    public String saveBill(final BillDTO billDTO, final Model model) {
        LOGGER.info("Save bill...");

        final Bill billToSave = createBillFromDTO(billDTO);
        billDAO.save(billToSave);

        return "redirect:/list_bills";
    }

    /**
     * Selects the list_bills page.
     * @param model {@link Model}
     * @param locale {@link Locale}
     * @return list_statements
     */
    @RequestMapping(value = "/list_bills", method = RequestMethod.GET)
    public String listIncomes(final Model model, final Locale locale) {

        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("i18n.xml");
        final ReloadableResourceBundleMessageSource bean = (ReloadableResourceBundleMessageSource) applicationContext.getBean("messageSource");

        LOGGER.info("List bills.");

        final String listBills = bean.getMessage("navbar.bills", null, locale);
        model.addAttribute("legendLabel", listBills);
        model.addAttribute("bills", convertToBillList(billDAO.getAllBills()));

        putDataTableMessagesIntoTheModel(model, locale);

        ((ClassPathXmlApplicationContext) applicationContext).close();
        return "list_bills";
    }

    private Bill createBillFromDTO(final BillDTO billDTO) {
        final CategoryDTO categoryDTO = billDTO.getCategory();
        final Category category = Category.builder(categoryDTO.getName()).categoryId(categoryDTO.getCategoryId()).build();

        final Calendar calendar = Calendar.getInstance();
        final DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);

        return Bill.builder(billDTO.getAmount(), fmtDateAndTime.format(calendar.getTime()), billDTO.getDeadlineDate()).category(category)
                .interval(billDTO.getRecurringInterval()).payedDate(billDTO.getPayedDate()).note(billDTO.getNote()).isPayed(billDTO.isPayed())
                .build();
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

    private List<BillDTO> convertToBillList(final List<Bill> list) {
        final List<BillDTO> billList = new ArrayList<BillDTO>();

        for (final Bill entity : list) {
            LOGGER.debug(entity.toString());
            final BillDTO bill = convertToBill(entity);
            LOGGER.debug(bill.toString());
            billList.add(bill);
        }

        return billList;
    }

    private BillDTO convertToBill(final Bill bill) {
        return mapper.map(bill, BillDTO.class);
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

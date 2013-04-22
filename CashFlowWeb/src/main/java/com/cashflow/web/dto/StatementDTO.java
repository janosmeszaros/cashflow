package com.cashflow.web.dto;

import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.Statement;
import com.cashflow.domain.StatementType;

/**
 * DTO for statement.
 * @author Janos_Gyula_Meszaros
 */
public class StatementDTO {
    private String statementId;
    private String amount;
    private CategoryDTO category = new CategoryDTO();
    private String note;
    private String date;
    private StatementType type;
    private RecurringInterval recurringInterval;

    /**
     * Convert DTO to statement.
     * @return {@link Statement}
     */
    public Statement convert() {
        return Statement.builder(amount, date).category(category.convert()).note(note).recurringInterval(recurringInterval)
                .statementId(statementId).type(type).build();
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(final String statementId) {
        this.statementId = statementId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(final CategoryDTO category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public StatementType getType() {
        return type;
    }

    public void setType(final StatementType type) {
        this.type = type;
    }

    public RecurringInterval getRecurringInterval() {
        return recurringInterval;
    }

    public void setRecurringInterval(final RecurringInterval recurringInterval) {
        this.recurringInterval = recurringInterval;
    }
}

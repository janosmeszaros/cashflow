package com.cashflow.dao.objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cashflow.domain.RecurringInterval;
import com.cashflow.domain.StatementType;

/**
 * Entity class for statement.
 * @author Janos_Gyula_Meszaros
 */
@Entity
@Table(name = "statement")
@NamedNativeQueries({ @NamedNativeQuery(name = "Statement.getAllStatements", query = "SELECT * FROM statement"),
    @NamedNativeQuery(name = "Statement.getStatementsByType", query = "SELECT * FROM statement WHERE type=:type"),
    @NamedNativeQuery(name = "Statement.getStatementsById", query = "SELECT * FROM statement WHERE statementId=:id"),
    @NamedNativeQuery(name = "Statement.getRecurringStatements", query = "SELECT * FROM statement WHERE NOT(recurringInterval=NONE)") })
public class StatementEntity {
    @Id
    @GeneratedValue
    private int statementId;
    private String amount;
    @ManyToOne(cascade = CascadeType.ALL)
    private CategoryEntity category;
    private String note;
    private String date;
    @Enumerated(EnumType.STRING)
    private StatementType type;
    @Enumerated(EnumType.STRING)
    private RecurringInterval recurringInterval;

    public void setStatementId(final int statementId) {
        this.statementId = statementId;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }

    public void setCategory(final CategoryEntity category) {
        this.category = category;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setType(final StatementType type) {
        this.type = type;
    }

    public void setRecurringInterval(final RecurringInterval recurringInterval) {
        this.recurringInterval = recurringInterval;
    }

    public int getStatementId() {
        return statementId;
    }

    public String getAmount() {
        return amount;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public StatementType getType() {
        return type;
    }

    public RecurringInterval getRecurringInterval() {
        return recurringInterval;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

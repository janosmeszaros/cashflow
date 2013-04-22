package com.cashflow.dao.objects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Entity class for Category.
 * @author Janos_Gyula_Meszaros
 */
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue
    private String categoryId;
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<StatementEntity> statements;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<BillEntity> bills;

    public List<BillEntity> getBills() {
        return bills;
    }

    public void setBills(final List<BillEntity> bills) {
        this.bills = bills;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public List<StatementEntity> getStatements() {
        return statements;
    }

    public void setStatements(final List<StatementEntity> statements) {
        this.statements = statements;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(final String name) {
        this.name = name;
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

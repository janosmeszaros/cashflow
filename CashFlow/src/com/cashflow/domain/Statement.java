package com.cashflow.domain;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.statement.StatementType;

/**
 * Simple class for statement. This will hold data for one statement.
 * @author Janos_Gyula_Meszaros
 *
 */
public final class Statement {
    private final String id;
    private final String amount;
    private final String categoryId;
    private final String note;
    private final String date;
    private final StatementType type;
    private final RecurringInterval recurringInterval;

    private Statement(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.categoryId = builder.categoryId;
        this.date = builder.date;
        this.note = builder.note;
        this.recurringInterval = builder.recurringInterval;
        this.type = builder.type;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
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

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((recurringInterval == null) ? 0 : recurringInterval.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Statement other = (Statement) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (categoryId == null) {
            if (other.categoryId != null) {
                return false;
            }
        } else if (!categoryId.equals(other.categoryId)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (note == null) {
            if (other.note != null) {
                return false;
            }
        } else if (!note.equals(other.note)) {
            return false;
        }
        if (recurringInterval != other.recurringInterval) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    /**
     * Builder for statement class. Required parameters are <code>amount</code> and <code>date</code>.
     * Default statement type is {@link StatementType.Income}.
     * Default {@link RecurringInterval} is none.
     * @author Janos_Gyula_Meszaros
     *
     */
    public static class Builder {
        private final String amount;
        private final String date;

        private String id = "";
        private String note = "";
        private StatementType type = StatementType.Income;
        private RecurringInterval recurringInterval = RecurringInterval.none;
        private String categoryId = "";

        /**
         * Create builder for {@link Statement} with required values <code>amount</code> and <code>date</code>.
         * @param amount the amount of {@link Statement}
         * @param date the date of {@link Statement}
         */
        public Builder(String amount, String date) {
            this.amount = amount;
            this.date = date;
        }

        /**
         * Set the note for the {@link Statement}.
         * @param note note for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setNote(String note) {
            this.note = note;
            return this;
        }

        /**
         * Set type for the {@link Statement}.
         * @param type type for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setType(StatementType type) {
            this.type = type;
            return this;
        }

        /**
         * Set interval for the {@link Statement}. 
         * @param interval interval for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setRecurringInterval(RecurringInterval interval) {
            recurringInterval = interval;
            return this;
        }

        /**
         * Set id for the {@link Statement}.
         * @param id id for the {@link Statement}
         * @return {@link Builder}
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Set category for the {@link Statement}.
         * @param category 
         * @return {@link Builder}
         */
        public Builder setCategory(String category) {
            this.categoryId = category;
            return this;
        }

        /**
         * Build the {@link Statement}.
         * @return the {@link Statement} with the obligatory fields set
         */
        public Statement build() {
            return new Statement(this);
        }

    }

}

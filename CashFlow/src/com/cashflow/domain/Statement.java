package com.cashflow.domain;

import com.cashflow.constants.RecurringInterval;
import com.cashflow.database.statement.StatementType;

/**
 * Simple class for statement. This will hold data for one statement.
 * @author Janos_Gyula_Meszaros
 *
 */
public final class Statement {
    private String id;
    private String amount;
    private String note;
    private String date;
    private StatementType type;
    private RecurringInterval recurringInterval;

    private Statement(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
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
        if (!(obj instanceof Statement)) {
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
        if (recurringInterval == null) {
            if (other.recurringInterval != null) {
                return false;
            }
        } else if (!recurringInterval.equals(other.recurringInterval)) {
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
        private String amount;
        private String date;

        private String id = "";
        private String note = "";
        private StatementType type = StatementType.Income;
        private RecurringInterval recurringInterval = RecurringInterval.none;

        /**
         * Create builder for statement with required values <code>amount</code> and <code>date</code>.
         * @param amount the amount of statement.
         * @param date the date of statement.
         */
        public Builder(String amount, String date) {
            this.amount = amount;
            this.date = date;
        }

        /**
         * Set the note for the statement.
         * @param note note for the statement.
         * @return builder.
         */
        public Builder setNote(String note) {
            this.note = note;
            return this;
        }

        /**
         * Set type for the statement.
         * @param type type for the statement.
         * @return builder.
         */
        public Builder setType(StatementType type) {
            this.type = type;
            return this;
        }

        /**
         * Set interval for the statement. 
         * @param interval interval for the statement.
         * @return builder.
         */
        public Builder setRecurringInterval(RecurringInterval interval) {
            recurringInterval = interval;
            return this;
        }

        /**
         * Set id for the statement.
         * @param id id for the statement.
         * @return builder
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Build the statement.
         * @return the statement.
         */
        public Statement build() {
            return new Statement(this);
        }
    }

}

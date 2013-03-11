package com.cashflow.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cashflow.constants.Constants.INCOME_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cashflow.R;
import com.cashflow.activity.listeners.DateButtonOnClickListener;
import com.cashflow.database.balance.Balance;
import com.cashflow.database.statement.StatementPersistenceService;
import com.cashflow.database.statement.StatementType;
import com.cashflow.domain.Statement;
import com.google.inject.Inject;

/**
 * Statement adding. It gets it's type in the intent in extra named by <code>STATEMENT_TYPE_EXTRA</code>
 * @author Janos_Gyula_Meszaros
 */
public class AddStatementActivity extends RoboFragmentActivity {
    private static final int DURATION_MILLIS = 1000;
    private static final Logger LOG = LoggerFactory.getLogger(AddStatementActivity.class);
    @Inject
    private StatementPersistenceService service;

    @InjectView(R.id.amountText)
    private EditText amountText;
    @InjectView(R.id.dateButton)
    private Button dateButton;
    @InjectView(R.id.notesText)
    private EditText notesText;
    @InjectView(R.id.checkbox_area)
    private LinearLayout checkBoxLayout;
    @InjectView(R.id.recurring_income)
    private LinearLayout recurringArea;
    @Inject
    private Balance balance;
    @Inject
    private DateButtonOnClickListener listener;
    private StatementType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.debug("AddStatementActivity is creating...");

        setContentView(R.layout.activity_add_statement);
        setUpDateButton();
        setStatementType();
        setTitle();

        LOG.debug("AddStatementActivity has created with type: " + type);
    }

    /**
     * Onclick event for add statement button on add statement screen. Save the expense to database. If the save was successful then refresh the
     * balance else sets the result to canceled and close the activity. 
     * @param view
     *            Required for onClick.
     */
    public void submit(View view) {
        String amountStr = amountText.getText().toString();
        String date = dateButton.getText().toString();
        String note = notesText.getText().toString();
        Statement statement = createStatement(amountStr, date, note, type);

        if (service.saveStatement(statement)) {
            refreshBalance(amountStr);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    /**
     * Checkbox clicked event handler.
     * @param view 
     *            needed by event handler.
     */
    @SuppressLint("NewApi")
    public void checkBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            startInAnimationForCheckboxArea();
        } else {
            startOutAnimationForCheckboxArea();
        }
    }

    private void refreshBalance(String amountStr) {
        BigDecimal amount = new BigDecimal(amountStr);

        if (type.isIncome()) {
            balance.add(amount);
        } else {
            balance.subtract(amount);
        }

    }

    private void setStatementType() {
        String statementType = getIntent().getStringExtra(STATEMENT_TYPE_EXTRA);
        if (isIncome(statementType)) {
            type = StatementType.Income;
            recurringArea.setVisibility(VISIBLE);
        } else {
            type = StatementType.Expense;
        }
    }

    private boolean isIncome(String type) {
        return type.equals(INCOME_EXTRA);
    }

    private void setTitle() {
        if (type.isIncome()) {
            setTitle(R.string.title_activity_add_income);
        } else {
            setTitle(R.string.title_activity_add_expense);
        }
    }

    private void setUpDateButton() {
        final Calendar calendar = Calendar.getInstance();
        DateFormat fmtDateAndTime = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateButton.setText(fmtDateAndTime.format(calendar.getTime()));

        dateButton.setOnClickListener(listener);
    }

    private Statement createStatement(String amountStr, String date, String note, StatementType type) {
         return new Statement.Builder(amountStr, date).setNote(note).setType(type).build();
	}

    private void startOutAnimationForCheckboxArea() {
        Animation out = AnimationUtils.makeOutAnimation(this, true);
        out.setDuration(DURATION_MILLIS);
        out.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, GONE));
        checkBoxLayout.setAnimation(out);
        checkBoxLayout.startLayoutAnimation();
    }

    private void startInAnimationForCheckboxArea() {
        Animation in = AnimationUtils.makeInChildBottomAnimation(this);
        in.setDuration(DURATION_MILLIS);
        in.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, VISIBLE));
        checkBoxLayout.setAnimation(in);
        checkBoxLayout.startLayoutAnimation();

    }

    /**
     * Animation listener to animate the fade in and out the recurring details.
     * @author Janos_Gyula_Meszaros
     *
     */
    private final class AnimationListenerImplementation implements AnimationListener {
        private final LinearLayout layout;
        private int onAnimationEnd;

        private AnimationListenerImplementation(LinearLayout layout, int onAnimationEnd) {
            this.layout = layout;
            this.onAnimationEnd = onAnimationEnd;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            layout.setVisibility(onAnimationEnd);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
            layout.setVisibility(INVISIBLE);
        }
    }
}

package com.cashflow.category.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.dao.CategoryDAO;
import com.cashflow.domain.Category;
import com.google.inject.Inject;

/**
 * Create new Category.
 * @author Kornel_Refi
 */
public class CreateCategoryActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(CreateCategoryActivity.class);
    @Inject
    private CategoryDAO categoryDAO;

    @InjectView(R.id.categoryNameText)
    private EditText nameText;
    @InjectView(R.id.createCategoryButton)
    private Button submitButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_category);
        submitButton.setOnClickListener(new SubmitButtonOnClick());
    }

    /**
     * Submit button onClick listener for submit button on {@link CreateCategoryActivity}.
     * @author Janos_Gyula_Meszaros
     */
    protected class SubmitButtonOnClick implements OnClickListener {

        @Override
        public void onClick(final View view) {
            LOG.debug("Creating category: " + nameText.getText());
            final String name = nameText.getText().toString();

            try {
                final Category category = createCategory(name);
                if (categoryDAO.save(category) >= 0) {
                    finishActivity();
                } else {
                    showToast(getString(R.string.database_error));
                }
            } catch (final IllegalArgumentException exception) {
                showToast(exception.getMessage());
            }
        }

        private void showToast(final String msg) {
            Toast.makeText(CreateCategoryActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        private void finishActivity() {
            setResult(RESULT_OK);
            finish();
        }

        private Category createCategory(final String name) {
            return Category.builder(name).build();
        }

    }
}

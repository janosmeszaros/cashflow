package com.cashflow.activity.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cashflow.R;
import com.cashflow.database.category.CategoryPersistenceService;
import com.google.inject.Inject;

/**
 * Create new Category.
 * @author Kornel_Refi
 *
 */
public class CreateCategoryActivity extends RoboActivity {
    private static final Logger LOG = LoggerFactory.getLogger(CreateCategoryActivity.class);
    @Inject
    private CategoryPersistenceService service;

    @InjectView(R.id.categoryNameText)
    private EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_category);
        // Show the Up button in the action bar.
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_category, menu);
        return true;
    }

    /**
     * Submit button onClick method.
     * @param view Required for onClick.
     */
    public void createCategory(View view) {
        LOG.debug("Creating category: " + nameText.getText());
        String name = nameText.getText().toString();

        if (service.saveCategory(name)) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.empty_category), Toast.LENGTH_SHORT).show();
        }

    }
}

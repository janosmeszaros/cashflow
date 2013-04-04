package com.cashflow.statement.activity;

import static com.cashflow.constants.Constants.EDIT_ACTIVITY_CODE;
import static com.cashflow.constants.Constants.ID_EXTRA;
import static com.cashflow.constants.Constants.STATEMENT_TYPE_EXTRA;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.PROJECTION;
import static com.cashflow.database.DatabaseContracts.AbstractStatement.TO_VIEWS;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.cashflow.statement.database.StatementPersistenceService;
import com.cashflow.statement.database.StatementType;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

/**
 * Basic class to list statements. The type is setted from intent's <code>STATEMENT_TYPE_EXTRA</code> extra.
 * @author Janos_Gyula_Meszaros 
 */
public class ListStatementFragment extends RoboSherlockFragment implements OnCheckedChangeListener {
    private static final int EDIT_ID = 1;
    private static final int DELETE_ID = 0;
    private static final int GROUP_ID = 0;
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";

    private static final Logger LOG = LoggerFactory.getLogger(ListStatementFragment.class);

    private StatementType type;
    private SimpleCursorAdapter mAdapter;
    private final List<String> selectedItems = new ArrayList<String>();
    private final OnCheckedChangeListener listener = this;

    @Inject
    private StatementPersistenceService statementService;

    @InjectView(R.id.list_statement)
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_list_statements, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LOG.debug("ListStatementActivity is creating...");

        setHasOptionsMenu(true);
        setStatementType();
        getDataFromDatabase();

        LOG.debug("ListStatementActivity has created with type: " + type);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (selectedItems.size() == 1) {
            menu.add(GROUP_ID, DELETE_ID, Menu.NONE, DELETE).setIcon(android.R.drawable.ic_menu_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.add(GROUP_ID, EDIT_ID, Menu.NONE, EDIT).setIcon(android.R.drawable.ic_menu_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else if (selectedItems.size() >= 1) {
            menu.add(GROUP_ID, DELETE_ID, Menu.NONE, DELETE).setIcon(android.R.drawable.ic_menu_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    /**
     * Starts the edit statement interface. Add actual values to the {@link EditStatementActivity}'s 
     * intent under the proper extra.
     * @param view
     *            Needed by onClick event.
     */
    public void editButtonOnClick(final View view) {
        LOG.debug("Edit button clicked");
        final Intent intent = new Intent(this.getActivity(), EditStatementActivity.class);
        addExtras((View) view.getParent(), intent);
        startActivityForResult(intent, EDIT_ACTIVITY_CODE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LOG.debug("ListStatementActivity's onActivityResult method called with params: \nrequestCode: " + requestCode + "\nresultCode: "
                + requestCode);
        if (isEditActivity(requestCode, resultCode)) {
            getDataFromDatabase();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        View view = (View) buttonView.getParent();

        if (isChecked) {
            addSelectedId(view);
        } else {
            removeFromSelection(view);
        }
        getSherlockActivity().invalidateOptionsMenu();
    }

    private void removeFromSelection(View view) {
        String id = getIdFromView(view);
        selectedItems.remove(id);
    }

    private void addSelectedId(View view) {
        String id = getIdFromView(view);
        selectedItems.add(id);
    }

    private String getIdFromView(View view) {
        TextView text = (TextView) view.findViewById(R.id.row_id);
        return text.getText().toString();
    }

    private void addExtras(final View view, final Intent intent) {
        final TextView id = (TextView) view.findViewById(R.id.row_id);

        intent.putExtra(ID_EXTRA, id.getText());
    }

    private boolean isEditActivity(final int requestCode, final int resultCode) {
        return resultCode == Activity.RESULT_OK && requestCode == EDIT_ACTIVITY_CODE;
    }

    @SuppressLint("NewApi")
    private void getDataFromDatabase() {
        LOG.debug("Starting query for type: " + type);

        final Cursor cursor = statementService.getStatement(type);

        mAdapter = new SimpleCursorAdapter(this.getActivity(), R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.selectedCheckbox);
                checkBox.setOnCheckedChangeListener(listener);

                return view;
            }
        };

        list.setAdapter(mAdapter);

        LOG.debug("Query has done.");
    }

    private void setStatementType() {
        final Bundle bundle = getArguments();
        type = StatementType.valueOf(bundle.getString(STATEMENT_TYPE_EXTRA));
    }

}

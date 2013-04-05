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

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
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
    private final List<String> selectedIds = new ArrayList<String>();
    private final List<Integer> selectedPositions = new ArrayList<Integer>();
    private ActionMode actionMode;

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

    /**
     * Starts the edit statement interface. Add actual values to the {@link EditStatementActivity}'s 
     * intent under the proper extra.
     */
    private void editButtonOnClick() {
        LOG.debug("Edit button clicked");
        Intent intent;
        if (type.isIncome()) {
            intent = new Intent(this.getActivity(), EditIncomeActivity.class);
        } else {
            intent = new Intent(this.getActivity(), EditStatementActivity.class);
        }
        addExtras(selectedIds.get(0), intent);
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

        actionMode.invalidate();
    }

    private void removeFromSelection(View view) {
        String id = getIdFromView(view);
        int position = list.getPositionForView(view);

        selectedIds.remove(id);
        selectedPositions.remove((Object) position);

        if (selectedIds.size() == 0) {
            actionMode.finish();
        }
    }

    private void addSelectedId(View view) {
        createActionModeIfNecesarry();

        String id = getIdFromView(view);
        int position = list.getPositionForView(view);
        selectedIds.add(id);
        selectedPositions.add(position);
    }

    private void createActionModeIfNecesarry() {
        if (selectedIds.size() == 0) {
            actionMode = getSherlockActivity().startActionMode(new AnActionModeOfEpicProportions());
        }
    }

    private String getIdFromView(View view) {
        TextView text = (TextView) view.findViewById(R.id.row_id);
        return text.getText().toString();
    }

    private void addExtras(final String id, final Intent intent) {
        intent.putExtra(ID_EXTRA, id);
    }

    private boolean isEditActivity(final int requestCode, final int resultCode) {
        return resultCode == Activity.RESULT_OK && requestCode == EDIT_ACTIVITY_CODE;
    }

    @SuppressLint("NewApi")
    private void getDataFromDatabase() {
        LOG.debug("Starting query for type: " + type);

        final Cursor cursor = statementService.getStatement(type);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this.getActivity(), R.layout.list_statements_row, cursor, PROJECTION, TO_VIEWS) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.selectedCheckbox);
                checkBox.setOnCheckedChangeListener(ListStatementFragment.this);

                return view;
            }
        };

        list.setAdapter(adapter);

        LOG.debug("Query has done.");
    }

    private void setStatementType() {
        final Bundle bundle = getArguments();
        type = StatementType.valueOf(bundle.getString(STATEMENT_TYPE_EXTRA));
    }

    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.removeGroup(GROUP_ID);

            mode.setTitle(selectedPositions.size() + " selected.");
            if (selectedIds.size() == 1) {
                menu.add(GROUP_ID, EDIT_ID, Menu.NONE, EDIT).setIcon(android.R.drawable.ic_menu_edit)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.add(GROUP_ID, DELETE_ID, Menu.NONE, DELETE).setIcon(android.R.drawable.ic_menu_delete)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            } else if (selectedIds.size() >= 1) {
                menu.add(GROUP_ID, DELETE_ID, Menu.NONE, DELETE).setIcon(android.R.drawable.ic_menu_delete)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == EDIT_ID) {
                editButtonOnClick();
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            for (Integer i : selectedPositions) {
                View view = list.getChildAt(i);
                CheckBox checkbox = (CheckBox) view.findViewById(R.id.selectedCheckbox);
                checkbox.setChecked(false);
            }
        }
    }

}

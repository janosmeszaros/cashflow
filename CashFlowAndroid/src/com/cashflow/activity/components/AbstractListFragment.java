package com.cashflow.activity.components;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cashflow.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

/**
 * Base class for listview with action mode.
 * @author Janos_Gyula_Meszaros
 */
public abstract class AbstractListFragment extends RoboSherlockFragment implements OnCheckedChangeListener {
    private static final int EDIT_ID = 1;
    private static final int DELETE_ID = 0;
    private static final int GROUP_ID = 0;
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";

    private final List<String> selectedIds = new ArrayList<String>();
    private final List<Integer> selectedPositions = new CopyOnWriteArrayList<Integer>();
    private ActionMode actionMode;

    protected abstract ListView getList();

    protected abstract void editButtonOnClick();

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        final View view = (View) buttonView.getParent();

        if (isChecked) {
            addSelectedId(view);
        } else {
            removeFromSelection(view);
        }

        actionMode.invalidate();
    }

    private void removeFromSelection(final View view) {
        final String statementId = getIdFromView(view);
        final int position = getList().getPositionForView(view);

        selectedIds.remove(statementId);
        selectedPositions.remove((Object) position);

        if (selectedIds.isEmpty()) {
            actionMode.finish();
        }
    }

    private void addSelectedId(final View view) {
        createActionModeIfNecesarry();

        final String statementId = getIdFromView(view);
        final int position = getList().getPositionForView(view);
        selectedIds.add(statementId);
        selectedPositions.add(position);
    }

    private String getIdFromView(final View view) {
        final TextView text = (TextView) view.findViewById(R.id.row_id);
        return text.getText().toString();
    }

    private void createActionModeIfNecesarry() {
        if (selectedIds.isEmpty()) {
            actionMode = getSherlockActivity().startActionMode(new ActionModeWithDeleteAndEdit());
        }
    }

    protected List<String> getSelectedIds() {
        return selectedIds;
    }

    private final class ActionModeWithDeleteAndEdit implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
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
        public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
            final int statementId = item.getItemId();
            if (statementId == EDIT_ID) {
                editButtonOnClick();
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(final ActionMode mode) {
            for (final Integer i : selectedPositions) {
                final View view = getList().getChildAt(i);
                final CheckBox checkbox = (CheckBox) view.findViewById(R.id.selectedCheckbox);
                checkbox.setChecked(false);
            }
        }
    }

}

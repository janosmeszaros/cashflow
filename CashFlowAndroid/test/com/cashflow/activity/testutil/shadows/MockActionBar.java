package com.cashflow.activity.testutil.shadows;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;

public class MockActionBar extends ActionBar {

    private String title;
    private String subtitle;
    private View customView;
    private final Context realContext;
    private int mode;
    private final List<Tab> tabs = new ArrayList<ActionBar.Tab>();
    private int position;

    public MockActionBar(final Context context) {
        realContext = context;
    }

    @Override
    public void setCustomView(final View view) {
        setCustomView(view, null);
    }

    @Override
    public void setCustomView(final View view, final LayoutParams layoutParams) {
        this.customView = view;
    }

    @Override
    public void setCustomView(final int resId) {
    }

    @Override
    public void setIcon(final int resId) {
    }

    @Override
    public void setIcon(final Drawable icon) {
    }

    @Override
    public void setLogo(final int resId) {
    }

    @Override
    public void setLogo(final Drawable logo) {
    }

    @Override
    public void setListNavigationCallbacks(final SpinnerAdapter adapter, final OnNavigationListener callback) {
    }

    @Override
    public void setSelectedNavigationItem(final int position) {
        this.position = position;

    }

    @Override
    public int getSelectedNavigationIndex() {
        return position;
    }

    @Override
    public int getNavigationItemCount() {
        return 0;
    }

    @Override
    public void setTitle(final CharSequence title) {
        this.title = (String) title;
    }

    @Override
    public void setTitle(final int resId) {
        title = realContext.getString(resId);
    }

    @Override
    public void setSubtitle(final CharSequence newSubtitle) {
        subtitle = (String) newSubtitle;
    }

    @Override
    public void setSubtitle(final int resId) {
        subtitle = realContext.getString(resId);
    }

    @Override
    public void setDisplayOptions(final int options) {
    }

    @Override
    public void setDisplayOptions(final int options, final int mask) {
    }

    @Override
    public void setDisplayUseLogoEnabled(final boolean useLogo) {
    }

    @Override
    public void setDisplayShowHomeEnabled(final boolean showHome) {
    }

    @Override
    public void setDisplayHomeAsUpEnabled(final boolean showHomeAsUp) {
    }

    @Override
    public void setDisplayShowTitleEnabled(final boolean showTitle) {
    }

    @Override
    public void setDisplayShowCustomEnabled(final boolean showCustom) {
    }

    @Override
    public void setBackgroundDrawable(final Drawable d) {
    }

    @Override
    public View getCustomView() {
        return customView;
    }

    @Override
    public CharSequence getTitle() {
        return title;
    }

    @Override
    public CharSequence getSubtitle() {
        return subtitle;
    }

    @Override
    public int getNavigationMode() {
        return mode;
    }

    @Override
    public void setNavigationMode(final int mode) {
        this.mode = mode;

    }

    @Override
    public int getDisplayOptions() {
        return 0;
    }

    @Override
    public Tab newTab() {
        return new MockTab();
    }

    @Override
    public void addTab(final Tab tab) {

        tabs.add(tab);
    }

    @Override
    public void addTab(final Tab tab, final boolean setSelected) {
    }

    @Override
    public void addTab(final Tab tab, final int position) {
    }

    @Override
    public void addTab(final Tab tab, final int position, final boolean setSelected) {
    }

    @Override
    public void removeTab(final Tab tab) {
    }

    @Override
    public void removeTabAt(final int position) {
    }

    @Override
    public void removeAllTabs() {
    }

    @Override
    public void selectTab(final Tab tab) {
    }

    @Override
    public Tab getSelectedTab() {
        return null;
    }

    @Override
    public Tab getTabAt(final int index) {
        return tabs.get(index);
    }

    @Override
    public int getTabCount() {
        return tabs.size();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public void addOnMenuVisibilityListener(final OnMenuVisibilityListener listener) {
    }

    @Override
    public void removeOnMenuVisibilityListener(final OnMenuVisibilityListener listener) {
    }

    public class MockTab extends Tab {
        private TabListener tabListener;
        private CharSequence text;

        public TabListener getTabListener() {
            return tabListener;
        }

        @Override
        public CharSequence getContentDescription() {
            return null;
        }

        @Override
        public View getCustomView() {
            return null;
        }

        @Override
        public Drawable getIcon() {
            return null;
        }

        @Override
        public int getPosition() {
            return tabs.indexOf(this);
        }

        @Override
        public Object getTag() {
            return null;
        }

        @Override
        public CharSequence getText() {
            return text;
        }

        @Override
        public void select() {

        }

        @Override
        public Tab setContentDescription(final int arg0) {
            return null;
        }

        @Override
        public Tab setContentDescription(final CharSequence arg0) {
            return null;
        }

        @Override
        public Tab setCustomView(final View arg0) {
            return null;
        }

        @Override
        public Tab setCustomView(final int arg0) {
            return null;
        }

        @Override
        public Tab setIcon(final Drawable arg0) {
            return null;
        }

        @Override
        public Tab setIcon(final int arg0) {
            return null;
        }

        @Override
        public Tab setTabListener(final TabListener arg0) {
            this.tabListener = arg0;
            return this;
        }

        @Override
        public Tab setTag(final Object arg0) {
            return null;
        }

        @Override
        public Tab setText(final CharSequence arg0) {
            this.text = arg0;
            return this;
        }

        @Override
        public Tab setText(final int arg0) {
            return null;
        }

    }
}

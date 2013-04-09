package com.cashflow.activity.components;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.cashflow.R;

/**
 * {@link OnClickListener} class for the recurring {@link CheckBox} on add and edit statement dialog.
 * @author Janos_Gyula_Meszaros
 *
 */
public class RecurringCheckBoxOnClickListener implements OnClickListener {

    private static final int DURATION_MILLIS = 1000;

    private LinearLayout checkBoxLayout;

    /**
     * CheckBox clicked event handler.
     * @param view 
     *            needed by event handler.
     */
    @Override
    @SuppressLint("NewApi")
    public void onClick(final View view) {
        final boolean checked = ((CheckBox) view).isChecked();
        final Activity activity = (Activity) view.getContext();
        getCheckbox(activity);

        if (checked) {
            startInAnimationForCheckboxArea(activity);
        } else {
            startOutAnimationForCheckboxArea(activity);
        }
    }

    private void getCheckbox(final Activity activity) {
        checkBoxLayout = (LinearLayout) activity.findViewById(R.id.recurring_checkbox_area_bill);

        if (checkBoxLayout == null) {
            checkBoxLayout = (LinearLayout) activity.findViewById(R.id.recurring_checkbox_area_income);
        }
    }

    private void startInAnimationForCheckboxArea(final Activity context) {
        final Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        slideIn.setDuration(DURATION_MILLIS);
        slideIn.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, VISIBLE));
        checkBoxLayout.startAnimation(slideIn);
    }

    private void startOutAnimationForCheckboxArea(final Activity context) {
        final Animation slideOut = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        slideOut.setDuration(DURATION_MILLIS);
        slideOut.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, INVISIBLE));
        checkBoxLayout.startAnimation(slideOut);
    }

    /**
     * Animation listener to animate the fade in and out the recurring details.
     * @author Janos_Gyula_Meszaros
     *
     */
    private final class AnimationListenerImplementation implements AnimationListener {
        private final LinearLayout layout;
        private final int visibilityResult;

        private AnimationListenerImplementation(final LinearLayout layout, final int onAnimationEnd) {
            this.layout = layout;
            visibilityResult = onAnimationEnd;
        }

        @Override
        public void onAnimationEnd(final Animation animation) {
            layout.setVisibility(visibilityResult);
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {
        }

        @Override
        public void onAnimationStart(final Animation animation) {
        }
    }

}

package com.cashflow.activity.components;

import static android.view.View.GONE;
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
        Activity activity = (Activity) view.getContext();
        checkBoxLayout = (LinearLayout) activity.findViewById(R.id.recurring_checkbox_area);

        if (checked) {
            startInAnimationForCheckboxArea(activity);
        } else {
            startOutAnimationForCheckboxArea(activity);
        }
    }

    private void startInAnimationForCheckboxArea(final Activity context) {
        final Animation in = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        in.setDuration(DURATION_MILLIS);
        in.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, VISIBLE));
        checkBoxLayout.setAnimation(in);
        checkBoxLayout.startLayoutAnimation();
    }

    private void startOutAnimationForCheckboxArea(final Activity context) {
        final Animation out = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        out.setDuration(DURATION_MILLIS);
        out.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, GONE));
        checkBoxLayout.setAnimation(out);
        checkBoxLayout.startLayoutAnimation();
    }

    /**
     * Animation listener to animate the fade in and out the recurring details.
     * @author Janos_Gyula_Meszaros
     *
     */
    private final class AnimationListenerImplementation implements AnimationListener {
        private final LinearLayout layout;
        private final int onAnimationEnd;

        private AnimationListenerImplementation(LinearLayout layout, int onAnimationEnd) {
            this.layout = layout;
            this.onAnimationEnd = onAnimationEnd;
        }

        @Override
        public void onAnimationEnd(final Animation animation) {
            layout.setVisibility(onAnimationEnd);
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {
        }

        @Override
        public void onAnimationStart(final Animation animation) {
            layout.setVisibility(INVISIBLE);
        }
    }

}

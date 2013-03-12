package com.cashflow.activity.listeners;

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
 * Onclick listener class for the recurring checkbox on add and edit statement dialog.
 * @author Janos_Gyula_Meszaros
 *
 */
public class RecurringCheckBoxOnClickListener implements OnClickListener {
    private static final int DURATION_MILLIS = 1000;

    /**
     * Checkbox clicked event handler.
     * @param view 
     *            needed by event handler.
     */
    @Override
    @SuppressLint("NewApi")
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            startInAnimationForCheckboxArea((Activity) view.getContext());
        } else {
            startOutAnimationForCheckboxArea((Activity) view.getContext());
        }
    }

    private void startOutAnimationForCheckboxArea(Activity context) {
        LinearLayout checkBoxLayout = (LinearLayout) context.findViewById(R.id.recurring_checkbox_area);

        Animation out = AnimationUtils.makeOutAnimation(context, true);
        out.setDuration(DURATION_MILLIS);
        out.setAnimationListener(new AnimationListenerImplementation(checkBoxLayout, GONE));
        checkBoxLayout.setAnimation(out);
        checkBoxLayout.startLayoutAnimation();
    }

    private void startInAnimationForCheckboxArea(Activity context) {
        LinearLayout checkBoxLayout = (LinearLayout) context.findViewById(R.id.recurring_checkbox_area);

        Animation in = AnimationUtils.makeInChildBottomAnimation(context);
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

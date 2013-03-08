package com.cashflow.activity.util;

import com.cashflow.activity.CreateCategoryActivity;
import com.google.inject.Provider;

/**
 * {@link CreateCategoryActivity} {@link Provider}.
 * @author Kornel_Refi
 *
 */
public class CreateCategoryActivityProvider implements Provider<CreateCategoryActivity> {

    @Override
    public CreateCategoryActivity get() {
        return new CreateCategoryActivity();
    }
}

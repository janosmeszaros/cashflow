package com.cashflow.activity.testutil;

import com.cashflow.bill.activity.AddBillFragment;
import com.google.inject.Provider;

/**
 * Provider for {@link AddBillFragment} class.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddBillActivityProvider implements Provider<AddBillFragment> {

    @Override
    public AddBillFragment get() {
        return new AddBillFragment();
    }

}

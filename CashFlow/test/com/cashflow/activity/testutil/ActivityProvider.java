package com.cashflow.activity.testutil;

import com.cashflow.bill.activity.AddBillActivity;
import com.google.inject.Provider;

/**
 * Provider for {@link AddBillActivity} class.
 * @author Janos_Gyula_Meszaros
 *
 */
public class AddBillActivityProvider implements Provider<AddBillActivity> {

    @Override
    public AddBillActivity get() {
        return new AddBillActivity();
    }

}

package com.cashflow.category.database;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * {@link AndroidCategoryDAO} test.
 * @author Kornel_Refi
 *
 */
@RunWith(RobolectricTestRunner.class)
public class CategoryDaoTest {

    @Test(expected = IllegalArgumentException.class)
    public void testWhenConstructorGetsNullThenThrowsIllegalArgumentException() {
        new AndroidCategoryDAO(null);
    }

}

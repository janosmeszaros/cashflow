package com.cashflow.activity.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.cashflow.components.DatePickerFragment;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DateButtonOnClickListenerTest {

    private DateButtonOnClickListener underTest;
    @Mock
    private DatePickerFragment datePickerFragmentMock;
    @Mock
    private View view;
    @Mock
    private FragmentActivity fragmentActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new DateButtonOnClickListener(datePickerFragmentMock);
    }

    @Test
    public void testOnClickShouldShowDatePickerFragment() {
        Mockito.when(view.getContext()).thenReturn(fragmentActivity);

        underTest.onClick(view);

        verify(datePickerFragmentMock).show(any(FragmentManager.class), Mockito.anyString());
    }
}

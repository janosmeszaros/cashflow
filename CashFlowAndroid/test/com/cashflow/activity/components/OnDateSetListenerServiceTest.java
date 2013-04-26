package com.cashflow.activity.components;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OnDateSetListenerServiceTest {

    private static final int DAY = 28;
    private static final int MONTH = 3;
    private static final int YEAR = 2013;
    private OnDateSetListenerService underTest;
    private Button dateButton;
    private final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private Date date;
    @Mock
    private DatePicker datePickerMock;
    @Mock
    private Activity activity;
    @Mock
    private ViewPager pager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpMocks();
        createDate();

        underTest = new OnDateSetListenerService(activity);
        dateButton = new Button(null);
    }

    private void createDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR, MONTH, DAY);
        date = calendar.getTime();
    }

    private void setUpMocks() {
        when(datePickerMock.getYear()).thenReturn(YEAR);
        when(datePickerMock.getMonth()).thenReturn(MONTH);
        when(datePickerMock.getDayOfMonth()).thenReturn(DAY);
        when(activity.findViewById(R.id.pager)).thenReturn(pager);
    }

    @Test
    public void testOnDateSetWhenCurrentItemIsZeroThenIncomeDateButtonIsTheButton() {
        when(pager.getCurrentItem()).thenReturn(0);
        when(activity.findViewById(R.id.incomeDateButton)).thenReturn(dateButton);

        underTest.onDateSet(datePickerMock, YEAR, MONTH, DAY);

        Assert.assertThat(dateButton.getText().toString(), equalTo(dateFormatter.format(date)));
        verify(activity).findViewById(R.id.incomeDateButton);
    }

    @Test
    public void testOnDateSetWhenCurrentItemIsOneThenExpenseDateButtonIsTheButton() {
        when(pager.getCurrentItem()).thenReturn(1);
        when(activity.findViewById(R.id.expenseDateButton)).thenReturn(dateButton);

        underTest.onDateSet(datePickerMock, YEAR, MONTH, DAY);

        Assert.assertThat(dateButton.getText().toString(), equalTo(dateFormatter.format(date)));
        verify(activity).findViewById(R.id.expenseDateButton);
    }

    @Test
    public void testOnDateSetWhenCurrentItemIsOneThenBillDateButtonIsTheButton() {
        when(pager.getCurrentItem()).thenReturn(2);
        when(activity.findViewById(R.id.billDateButton)).thenReturn(dateButton);

        underTest.onDateSet(datePickerMock, YEAR, MONTH, DAY);

        Assert.assertThat(dateButton.getText().toString(), equalTo(dateFormatter.format(date)));
        verify(activity).findViewById(R.id.billDateButton);
    }

}

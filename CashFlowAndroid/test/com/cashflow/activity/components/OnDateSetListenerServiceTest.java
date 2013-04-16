package com.cashflow.activity.components;

import static org.hamcrest.Matchers.equalTo;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.widget.Button;
import android.widget.DatePicker;

import com.cashflow.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OnDateSetListenerServiceTest {

    private OnDateSetListenerService underTest;
    private Button dateButton;
    @Mock
    private DatePicker datePickerMock;
    @Mock
    private Activity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        underTest = new OnDateSetListenerService(activity);
        dateButton = new Button(null);
    }

    @Test
    public void testOnDateSet() {
        Mockito.when(datePickerMock.getYear()).thenReturn(2013);
        Mockito.when(datePickerMock.getMonth()).thenReturn(3);
        Mockito.when(datePickerMock.getDayOfMonth()).thenReturn(28);
        Mockito.when(activity.findViewById(R.id.dateButton)).thenReturn(dateButton);
        final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 3, 28);
        final Date date = calendar.getTime();

        underTest.onDateSet(datePickerMock, 2013, 3, 28);

        Assert.assertThat(dateButton.getText().toString(), equalTo(dateFormatter.format(date)));
    }

}

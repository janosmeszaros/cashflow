package com.cashflow.activity.components;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.widget.Button;
import android.widget.DatePicker;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OnDateSetServiceTest {

    private OnDateSetService underTest;
    private Button dateButton;
    @Mock
    private DatePicker datePickerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OnDateSetService();
        dateButton = new Button(null);
    }

    @Test
    public void testOnDateSet() {
        Mockito.when(datePickerMock.getYear()).thenReturn(2013);
        Mockito.when(datePickerMock.getMonth()).thenReturn(03);
        Mockito.when(datePickerMock.getDayOfMonth()).thenReturn(28);

        underTest.onDateSet(dateButton, datePickerMock);

        Assert.assertThat(dateButton.getText().toString(), equalTo("2013.04.28."));
    }
}

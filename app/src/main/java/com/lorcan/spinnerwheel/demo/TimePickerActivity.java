package com.lorcan.spinnerwheel.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TimePicker;
import com.lorcan.spinnerwheel.AbstractWheel;
import com.lorcan.spinnerwheel.OnWheelChangedListener;
import com.lorcan.spinnerwheel.OnWheelClickedListener;
import com.lorcan.spinnerwheel.OnWheelScrollListener;
import com.lorcan.spinnerwheel.adapters.NumericWheelAdapter;
import java.util.Calendar;

public class TimePickerActivity extends Activity {
    // Time changed flag
    private boolean timeChanged = false;

    // Time scrolled flag
    private boolean timeScrolled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.time_picker);

        final AbstractWheel hours = (AbstractWheel) findViewById(R.id.hour);
        hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
        hours.setCyclic(true);

        final AbstractWheel mins = (AbstractWheel) findViewById(R.id.mins);
        mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
        mins.setCyclic(true);

        final TimePicker picker = (TimePicker) findViewById(R.id.time);
        picker.setIs24HourView(true);

        // set current time
        Calendar c = Calendar.getInstance();
        int curHours = c.get(Calendar.HOUR_OF_DAY);
        int curMinutes = c.get(Calendar.MINUTE);

        hours.setCurrentItem(curHours);
        mins.setCurrentItem(curMinutes);

        picker.setCurrentHour(curHours);
        picker.setCurrentMinute(curMinutes);

        // add listeners
        addChangingListener(mins, "min");
        addChangingListener(hours, "hour");

        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                if (!timeScrolled) {
                    timeChanged = true;
                    picker.setCurrentHour(hours.getCurrentItem());
                    picker.setCurrentMinute(mins.getCurrentItem());
                    timeChanged = false;
                }
            }
        };
        hours.addChangingListener(wheelListener);
        mins.addChangingListener(wheelListener);

        OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(AbstractWheel wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                timeScrolled = true;
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                timeScrolled = false;
                timeChanged = true;
                picker.setCurrentHour(hours.getCurrentItem());
                picker.setCurrentMinute(mins.getCurrentItem());
                timeChanged = false;
            }
        };

        hours.addScrollingListener(scrollListener);
        mins.addScrollingListener(scrollListener);

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (!timeChanged) {
                    hours.setCurrentItem(hourOfDay, true);
                    mins.setCurrentItem(minute, true);
                }
            }
        });
    }

    /**
     * Adds changing listener for spinnerwheel that updates the spinnerwheel label
     * @param wheel the spinnerwheel
     * @param label the spinnerwheel label
     */
    private void addChangingListener(final AbstractWheel wheel, final String label) {
        wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                //spinnerwheel.setLabel(newValue != 1 ? label + "s" : label);
            }
        });
    }
}

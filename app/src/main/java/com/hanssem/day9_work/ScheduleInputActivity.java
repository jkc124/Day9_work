package com.hanssem.day9_work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleInputActivity extends Activity {
    EditText messageInput;
    Button timeButton;

    public static final int DIALOG_TIME = 1101;

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH시 mm분");

    Date selectedDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_input);

        /*setTitle("일정 추가");

        messageInput = (EditText) findViewById(R.id.messageInput);

        timeButton = (Button) findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });*/

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                /*String messageStr = messageInput.getText().toString();
                String timeStr = timeButton.getText().toString();*/

                Intent intent = new Intent();
                /*intent.putExtra("time", timeStr);
                intent.putExtra("message", messageStr);*/

                setResult(RESULT_OK, intent);

                finish();
            }
        });

        Button closeButton = (Button) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}

      /*  // set selected date using current date
        Date curDate = new Date();
        setSelectedDate(curDate);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_TIME:
                String timeStr = timeButton.getText().toString();

                Calendar calendar = Calendar.getInstance();
                Date curDate = new Date();
                try {
                    curDate = timeFormat.parse(timeStr);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }

                calendar.setTime(curDate);

                int curHour = calendar.get(Calendar.HOUR_OF_DAY);
                int curMinute = calendar.get(Calendar.MINUTE);

                return new TimePickerDialog(this,  timeSetListener,  curHour, curMinute, false);
            default:
                break;

        }

        return null;
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedCalendar.set(Calendar.MINUTE, minute);

            Date curDate = selectedCalendar.getTime();
            setSelectedDate(curDate);
        }
    };

    private void setSelectedDate(Date curDate) {
        selectedDate = curDate;

        String selectedTimeStr = timeFormat.format(curDate);
        timeButton.setText(selectedTimeStr);
    }*/


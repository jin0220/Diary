package com.example.diary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;

import java.util.Calendar;

public class ScheduleWriteActivity extends AppCompatActivity {

    TimePicker timePicker, timePicker2;
    NumberPicker yearPicker, monthPicker, dayPicker, yearPicker2, monthPicker2, dayPicker2;
    int year, month, day;
    RelativeLayout start, end;
    LinearLayout startDateSet, endDateSet;
    int start_count = 0, end_count = 0;
    TextView startDate, endDate, startTime, endTime;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_write);

        timePicker = findViewById(R.id.timePicker);
        timePicker2 = findViewById(R.id.timePicker2);
        yearPicker = findViewById(R.id.year);
        monthPicker = findViewById(R.id.month);
        dayPicker = findViewById(R.id.day);
        yearPicker2 = findViewById(R.id.year2);
        monthPicker2 = findViewById(R.id.month2);
        dayPicker2 = findViewById(R.id.day2);

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        //현재 날짜
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        startDate();
        endDate();

        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        startDateSet = findViewById(R.id.startDateSet);
        endDateSet = findViewById(R.id.endDateSet);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_count == 0) {
                    startDateSet.setVisibility(View.VISIBLE);
                    start_count++;
                }else{
                    startDateSet.setVisibility(View.GONE);
                    start_count--;
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(end_count == 0) {
                    endDateSet.setVisibility(View.VISIBLE);
                    end_count++;
                }else{
                    endDateSet.setVisibility(View.GONE);
                    end_count--;
                }
            }
        });

        startDate.setText(yearPicker.getValue() + "년 " + monthPicker.getValue() +"월 " + dayPicker.getValue() + "일");
        endDate.setText(yearPicker2.getValue() + "년 " + monthPicker2.getValue() +"월 " + dayPicker2.getValue() + "일");

        if(timePicker.getCurrentHour() < 12) {
            startTime.setText("오전 " + timePicker.getCurrentHour() + ":" + String.format("%02d", timePicker.getCurrentMinute()));
        }
        else if(timePicker.getCurrentHour() == 12) {
            startTime.setText("오후 " + timePicker.getCurrentHour() + ":" + String.format("%02d", timePicker.getCurrentMinute()));
        }
        else{
            startTime.setText("오후 " + (timePicker.getCurrentHour() - 12) + ":" + String.format("%02d", timePicker.getCurrentMinute()));
        }

        if(timePicker2.getCurrentHour() < 12) {
            endTime.setText("오전 " + timePicker2.getCurrentHour() + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
        }
        else if(timePicker2.getCurrentHour() == 12) {
            endTime.setText("오후 " + timePicker2.getCurrentHour() + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
        }
        else{
            endTime.setText("오후 " + (timePicker2.getCurrentHour() - 12) + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int oldVal, int newVal) {
                if(timePicker.getCurrentHour() < 12) {
                    startTime.setText("오전 " + timePicker.getCurrentHour() + ":" + String.format("%02d", timePicker.getCurrentMinute()));
                }
                else if(timePicker.getCurrentHour() == 12) {
                    startTime.setText("오후 " + timePicker.getCurrentHour() + ":" + String.format("%02d", timePicker.getCurrentMinute()));
                }
                else{
                    startTime.setText("오후 " + (timePicker.getCurrentHour() - 12) + ":" + String.format("%02d", timePicker.getCurrentMinute()));
                }
            }
        });

        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int oldVal, int newVal) {
                if(timePicker2.getCurrentHour() < 12) {
                    endTime.setText("오전 " + timePicker2.getCurrentHour() + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
                }
                else if(timePicker2.getCurrentHour() == 12) {
                    endTime.setText("오후 " + timePicker2.getCurrentHour() + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
                }
                else{
                    endTime.setText("오후 " + (timePicker2.getCurrentHour() - 12) + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
                }
            }
        });

    }

    private void startDate() {
        yearPicker.setMinValue(1950);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(year);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                startDate.setText(yearPicker.getValue() + "년 " + monthPicker.getValue() +"월 " + dayPicker.getValue() + "일");
            }
        });

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month + 1);
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if(oldVal == 1 && newVal == 12){
                    yearPicker.setValue(yearPicker.getValue() - 1);
                }
                if(oldVal == 12 && newVal == 1){
                    yearPicker.setValue(yearPicker.getValue() + 1);
                }
                startDate.setText(yearPicker.getValue() + "년 " + monthPicker.getValue() +"월 " + dayPicker.getValue() + "일");
            }
        });

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //해당 월별로 마지막 날짜 셋팅 고치기
        dayPicker.setValue(day);
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if(oldVal == 1 && newVal == 31){
                    monthPicker.setValue(monthPicker.getValue() - 1);
                }
                if(oldVal == 31 && newVal == 1){
                    monthPicker.setValue(monthPicker.getValue() + 1);
                }
                startDate.setText(yearPicker.getValue() + "년 " + monthPicker.getValue() +"월 " + dayPicker.getValue() + "일");
            }
        });
    }

    private void endDate() {
        yearPicker2.setMinValue(1950);
        yearPicker2.setMaxValue(2100);
        yearPicker2.setValue(year);
        yearPicker2.setWrapSelectorWheel(false);
        yearPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                endDate.setText(yearPicker2.getValue() + "년 " + monthPicker2.getValue() +"월 " + dayPicker2.getValue() + "일");
            }
        });

        monthPicker2.setMinValue(1);
        monthPicker2.setMaxValue(12);
        monthPicker2.setValue(month + 1);
        monthPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if(oldVal == 1 && newVal == 12){
                    yearPicker2.setValue(yearPicker2.getValue() - 1);
                }
                if(oldVal == 12 && newVal == 1){
                    yearPicker2.setValue(yearPicker2.getValue() + 1);
                }
                endDate.setText(yearPicker2.getValue() + "년 " + monthPicker2.getValue() +"월 " + dayPicker2.getValue() + "일");
            }
        });

        dayPicker2.setMinValue(1);
        dayPicker2.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //해당 월별로 마지막 날짜 셋팅 고치기
        dayPicker2.setValue(day);
        dayPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if(oldVal == 1 && newVal == 31){
                    monthPicker2.setValue(monthPicker2.getValue() - 1);
                }
                if(oldVal == 31 && newVal == 1){
                    monthPicker2.setValue(monthPicker2.getValue() + 1);
                }
                endDate.setText(yearPicker2.getValue() + "년 " + monthPicker2.getValue() +"월 " + dayPicker2.getValue() + "일");
            }
        });
    }
}
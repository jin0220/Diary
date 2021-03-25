package com.example.diary.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.fragment.CalendarFragment;

import java.util.Calendar;

public class ScheduleWriteActivity extends AppCompatActivity {

    TimePicker timePicker, timePicker2;
    NumberPicker yearPicker, monthPicker, dayPicker, yearPicker2, monthPicker2, dayPicker2;
    int year, month, day, e_year, e_month, e_day;
    RelativeLayout start, end;
    LinearLayout startDateSet, endDateSet;
    int start_count = 0, end_count = 0;
    TextView startDate, endDate, startTime, endTime;
    Calendar calendar;
    Button store, cancel;
    DiaryDBHelper diaryDBHelper;
    EditText title, memo;
    String date, _id;
    boolean modify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_write);

        diaryDBHelper = new DiaryDBHelper(this);

        init();

        calendar = Calendar.getInstance();

        Intent intent = getIntent();
        modify = intent.getExtras().getBoolean("modify");


        if (modify == false) {
            //선택된 날짜 가져오기
            date = intent.getExtras().getString("date");

            year = Integer.parseInt(date.split(" ")[0].split("년")[0]);
            month = Integer.parseInt(date.split(" ")[1].split("월")[0]);
            day = Integer.parseInt(date.split(" ")[2].split("일")[0]);

            e_year = year;
            e_month = month;
            e_day = day;
        }
        else {
            _id = intent.getExtras().getString("_id");

            String sql = "select * from " + diaryDBHelper.TABLE_SCHEDULE + " where _id = '" + _id + "'";

            Cursor cursor = diaryDBHelper.select_sql(sql);
            if (cursor.moveToNext()){
                String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
                String m = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.MEMO));
                String s_d = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.START_DATE));
                String e_d = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.END_DATE));

                String s_t = s_d.split(" ")[3] + " " + s_d.split(" ")[4];
                String e_t = e_d.split(" ")[3] + " " + e_d.split(" ")[4];

                year = Integer.parseInt(s_d.split(" ")[0].split("년")[0]);
                month = Integer.parseInt(s_d.split(" ")[1].split("월")[0]);
                day = Integer.parseInt(s_d.split(" ")[2].split("일")[0]);

                e_year = Integer.parseInt(e_d.split(" ")[0].split("년")[0]);
                e_month = Integer.parseInt(e_d.split(" ")[1].split("월")[0]);
                e_day = Integer.parseInt(e_d.split(" ")[2].split("일")[0]);

                title.setText(t);
                memo.setText(m);
                startTime.setText(s_t);
                endTime.setText(e_t);

                int s_hour = Integer.parseInt(s_d.split(" ")[4].split(":")[0]);
                int e_hour = Integer.parseInt(e_d.split(" ")[4].split(":")[0]);
                if (s_d.split(" ")[3].equals("오후")){
                    s_hour += 12;
                }
                if (e_d.split(" ")[3].equals("오후")){
                    e_hour += 12;
                }

                timePicker.setCurrentHour(s_hour); //수정할 시간 설정
                timePicker.setCurrentMinute(Integer.parseInt(s_d.split(" ")[4].split(":")[1]));
                timePicker2.setCurrentHour(e_hour);
                timePicker2.setCurrentMinute(Integer.parseInt(e_d.split(" ")[4].split(":")[1]));
            }
        }

        startDate();
        endDate();

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

        if(modify == false) {
            int current_hour = timePicker.getCurrentHour();

            if (timePicker.getCurrentHour() < 12) {
                startTime.setText("오전 " + current_hour + ":" + String.format("%02d", timePicker.getCurrentMinute()));
            } else if (timePicker.getCurrentHour() == 12) {
                startTime.setText("오후 " + current_hour + ":" + String.format("%02d", timePicker.getCurrentMinute()));
            } else {
                startTime.setText("오후 " + (current_hour - 12) + ":" + String.format("%02d", timePicker.getCurrentMinute()));
            }

            if (timePicker2.getCurrentHour() < 12) {
                endTime.setText("오전 " + (current_hour + 1) + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
            } else if (timePicker2.getCurrentHour() == 12) {
                endTime.setText("오후 " + (current_hour + 1) + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
            } else {
                endTime.setText("오후 " + ((current_hour + 1) - 12) + ":" + String.format("%02d", timePicker2.getCurrentMinute()));
            }

            timePicker2.setCurrentHour(current_hour + 1); //timepicker 시간 설정
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


        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_date = startDate.getText().toString() + " " + startTime.getText().toString();
                String end_date = endDate.getText().toString() + " " + endTime.getText().toString();
                String t = title.getText().toString();
                String m = memo.getText().toString();

                if (modify == false) {
                    if (t.equals("") && m.equals("")) {
                        Toast.makeText(getApplicationContext(), "입력한 정보가 없어 저장되지 않았습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        diaryDBHelper.schedule_insert(start_date, end_date, t, m);

                        Intent intent = new Intent(getApplicationContext(), CalendarFragment.class);
                        intent.putExtra("title", t);
                        intent.putExtra("time", startTime.getText().toString() + " ~ " + endTime.getText().toString());
                        setResult(RESULT_OK, intent);
                    }
                }
                else {
                    if (t.equals("") && m.equals("")) {
                        Toast.makeText(getApplicationContext(), "입력한 정보가 없어 변경되지 않았습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        diaryDBHelper.schedule_update(_id, start_date, end_date, t, m);

                        Intent intent = new Intent(getApplicationContext(), ScheduleViewActivity.class);
                        intent.putExtra("title", t);
                        intent.putExtra("start_date", start_date);
                        intent.putExtra("end_date", end_date);
                        intent.putExtra("memo", m);
                        setResult(RESULT_OK, intent);
                    }
                }

                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void init(){
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

        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        startDateSet = findViewById(R.id.startDateSet);
        endDateSet = findViewById(R.id.endDateSet);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        store = findViewById(R.id.store);
        title = findViewById(R.id.title);
        memo = findViewById(R.id.memo);

        cancel = findViewById(R.id.cancel);
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
        monthPicker.setValue(month);
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
        yearPicker2.setValue(e_year);
        yearPicker2.setWrapSelectorWheel(false);
        yearPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                endDate.setText(yearPicker2.getValue() + "년 " + monthPicker2.getValue() +"월 " + dayPicker2.getValue() + "일");
            }
        });

        monthPicker2.setMinValue(1);
        monthPicker2.setMaxValue(12);
        monthPicker2.setValue(e_month);
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
        dayPicker2.setValue(e_day);
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
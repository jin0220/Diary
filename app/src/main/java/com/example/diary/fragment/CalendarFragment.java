package com.example.diary.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diary.R;
import com.example.diary.activity.ScheduleWriteActivity;
import com.example.diary.adapter.CalendarListAdapter;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.decorator.EventDecorator;
import com.example.diary.decorator.SaturdayDecorator;
import com.example.diary.decorator.SundayDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    TextView schedule;
    MaterialCalendarView calendar;
    DiaryDBHelper diaryDBHelper;
    LinearLayout schedule_page;
    CalendarListAdapter calendarListAdapter;
    ListView schedule_list;
    FloatingActionButton floatingActionButton;
    String click_date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_calendar, container, false);

        calendar = rootView.findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());

        click_date = Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일";

        schedule = rootView.findViewById(R.id.schedule);

        diaryDBHelper = new DiaryDBHelper(getActivity());
//        Cursor cursor = diaryDBHelper.select();
//        ArrayList<CalendarDay> calendarDayArrayList = new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//            String date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
//            String[] d = date.split(" ");
//            String year = d[0].split("년")[0];
//            String month = d[1].split("월")[0];
//            String day = d[2].split("일")[0];
//
//            CalendarDay calendarDay = CalendarDay.from(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
//            calendarDayArrayList.add(calendarDay);
//        }

        Cursor cursor3 = diaryDBHelper.select_sql("select * from schedule_table");
        ArrayList<CalendarDay> scheduleArrayList = new ArrayList<>();

        while (cursor3.moveToNext()) {
            String date = cursor3.getString(cursor3.getColumnIndexOrThrow(diaryDBHelper.START_DATE));
            String[] d = date.split(" ");
            String year = d[0].split("년")[0];
            String month = d[1].split("월")[0];
            String day = d[2].split("일")[0];

            CalendarDay calendarDay = CalendarDay.from(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
            scheduleArrayList.add(calendarDay);
        }

        schedule.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일의 일정");

        calendar.addDecorators(new SaturdayDecorator(),
                new SundayDecorator(),
//                                new SelectorDecorator(getActivity()),
                new EventDecorator(Color.BLUE, scheduleArrayList));

        //달력 년/월 표시
        calendar.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월");
                String monthAndYear = simpleDateFormat.format(day.getDate());

                return monthAndYear;
            }
        });

        // 날짜가 변경될 때 이벤트를 받기 위한 리스너
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                schedule.setText(date.getDay() + "일의 일정");
                click_date = date.getYear() + "년 " + (date.getMonth() + 1) + "월 " + date.getDay() + "일";
                schedule_db();
            }

        });

        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            }
        });

        schedule_page = rootView.findViewById(R.id.schedule_view);
        schedule_list = rootView.findViewById(R.id.schedule_list);

        schedule_db();

        SlidingUpPanelLayout slidingUpPanelLayout = rootView.findViewById(R.id.slidinguppanel);
        slidingUpPanelLayout.setDragView(rootView.findViewById(R.id.schedule)); //패널 열기위해 드래그하는 위치

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleWriteActivity.class);
                startActivity(intent);
            }
        });

        return rootView;

    }

    private void schedule_db() {
        calendarListAdapter = new CalendarListAdapter();
        schedule_list.setAdapter(calendarListAdapter);

        String sql = "select * from " + diaryDBHelper.TABLE_SCHEDULE + " where start_date like " + "'" + click_date + "%'" + " order by start_date";

        Cursor cursor2 = diaryDBHelper.select_sql(sql);
        while (cursor2.moveToNext()) {
            String t = cursor2.getString(cursor2.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String s = cursor2.getString(cursor2.getColumnIndexOrThrow(diaryDBHelper.START_DATE)).split(" ")[3] + " "
                    + cursor2.getString(cursor2.getColumnIndexOrThrow(diaryDBHelper.START_DATE)).split(" ")[4] + " ~ "
                    + cursor2.getString(cursor2.getColumnIndexOrThrow(diaryDBHelper.END_DATE)).split(" ")[3] + " "
                    + cursor2.getString(cursor2.getColumnIndexOrThrow(diaryDBHelper.END_DATE)).split(" ")[4];

            calendarListAdapter.addData(t, s);
        }
    }

}

package com.example.diary.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.decorator.EventDecorator;
import com.example.diary.decorator.SaturdayDecorator;
import com.example.diary.decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    TextView schedule;
    MaterialCalendarView calendar;
    DiaryDBHelper diaryDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_calendar, container, false);

        calendar = rootView.findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());

        schedule = rootView.findViewById(R.id.schedule);

        diaryDBHelper = new DiaryDBHelper(getActivity());
        Cursor cursor = diaryDBHelper.select();
        ArrayList<CalendarDay> calendarDayArrayList = new ArrayList<>();

        while (cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
            String[] d = date.split(" ");
            String year = d[0].split("년")[0];
            String month = d[1].split("월")[0];
            String day = d[2].split("일")[0];

            CalendarDay calendarDay = CalendarDay.from(Integer.valueOf(year),Integer.valueOf(month) - 1,Integer.valueOf(day));
            calendarDayArrayList.add(calendarDay);
        }


        schedule.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일의 일정");

        calendar.addDecorators(new SaturdayDecorator(),
                                new SundayDecorator(),
//                                new SelectorDecorator(getActivity()),
                                new EventDecorator(Color.GREEN, calendarDayArrayList));

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
            }

        });

        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            }
        });
        return rootView;

    }
}

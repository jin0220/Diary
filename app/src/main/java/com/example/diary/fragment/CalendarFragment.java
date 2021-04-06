package com.example.diary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diary.R;
import com.example.diary.activity.ScheduleViewActivity;
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
    int ADD_REQUEST = 1;
    int VIEW_REQUEST = 2;
    int select_list;
    EventDecorator eventDecorator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_calendar, container, false);

        calendar = rootView.findViewById(R.id.calendar);
        calendar.setSelectedDate(CalendarDay.today());

        click_date = Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일";

        schedule = rootView.findViewById(R.id.schedule);

        diaryDBHelper = new DiaryDBHelper(getActivity());

        schedule_page = rootView.findViewById(R.id.schedule_view);
        schedule_list = rootView.findViewById(R.id.schedule_list);


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

        schedule_dot(); //달력에 일정 표시

        schedule.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일의 일정");

        calendar.addDecorators(new SaturdayDecorator(),
                new SundayDecorator()
//                                new SelectorDecorator(getActivity()),
                );

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

        schedule_db();

        schedule_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = view.findViewById(R.id.schedule_title);
                Intent intent = new Intent(getActivity(), ScheduleViewActivity.class);
                intent.putExtra("date", click_date);
                intent.putExtra("title", title.getText());
                select_list = (int)calendarListAdapter.getItemId(i); //일정 수정 및 삭제하기 위해 해당 아이템 position 저장
                startActivityForResult(intent, VIEW_REQUEST);
            }
        });

        schedule_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String items[] = {"삭제", "수정"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("선택 목록").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int itemIndex) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

        final SlidingUpPanelLayout slidingUpPanelLayout = rootView.findViewById(R.id.slidinguppanel);
        slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED); //패널 외부를 클릭하면 패널이 숨겨짐.
            }
        });
        slidingUpPanelLayout.setDragView(rootView.findViewById(R.id.schedule)); //패널 열기위해 드래그하는 위치

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleWriteActivity.class);
                intent.putExtra("date", click_date);
                intent.putExtra("modify", false);
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        return rootView;

    }



    public void schedule_dot(){
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
        eventDecorator = new EventDecorator(Color.BLUE, scheduleArrayList);

        calendar.addDecorator(eventDecorator);
    }

    public void schedule_db() {
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

    public void schedule_remove(){
        calendarListAdapter.remove(select_list); //일정 삭제하기
        if(calendarListAdapter.isEmpty()) {
            calendar.removeDecorator(eventDecorator); //처음 그려졌던 dot을 없애고
            schedule_dot(); //새로운 dot을 그림.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(requestCode == ADD_REQUEST && resultCode == Activity.RESULT_OK){
            String title = intent.getExtras().get("title").toString();
            String time = intent.getExtras().get("time").toString();
            calendarListAdapter.addData(title, time);
            calendarListAdapter.notifyDataSetChanged(); //리스트뷰 갱신
            if(calendarListAdapter.getCount() == 1)
                schedule_dot(); //일정이 없는 날 일정이 하나가 생성되면 dot 그림.
        }
        else if(requestCode == VIEW_REQUEST && resultCode == Activity.RESULT_OK){
            if(intent.getExtras().getInt("state") == 1) {
                schedule_remove();
            }
            else if(intent.getExtras().getInt("state") == 2){
                String title = intent.getExtras().get("title").toString();
                String time = intent.getExtras().get("time").toString();
                calendarListAdapter.modify(select_list, title, time);
            }
        }
    }
}

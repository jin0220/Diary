package com.example.diary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.data.CalendarListData;

import java.util.ArrayList;

public class CalendarListAdapter extends BaseAdapter {

    TextView schedule_title, schedule_time;

    ArrayList<CalendarListData> items = new ArrayList<>();

    public CalendarListAdapter() {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.calendar_listview, viewGroup, false);
        }
        schedule_title = view.findViewById(R.id.schedule_title);
        schedule_time = view.findViewById(R.id.schedule_time);

        CalendarListData calendarListData = items.get(position);

        schedule_title.setText(calendarListData.getText());
        schedule_time.setText(calendarListData.getTime());

        return view;
    }

    public void addData(String text, String time) {
        CalendarListData calendarListData = new CalendarListData();

        calendarListData.setText(text);
        calendarListData.setTime(time);

        items.add(calendarListData);
    }
}

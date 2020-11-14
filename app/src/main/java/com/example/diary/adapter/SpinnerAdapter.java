package com.example.diary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.diary.R;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> Data;
    String date;
    LayoutInflater Inflater;

    public SpinnerAdapter (Context context, ArrayList<String> data, String date){
        this.mContext = context;
        this.Data = data;
        this.date = date;
        Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(Data!=null) return Data.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        return Data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //스피너를 열기 전 뷰
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = Inflater.inflate(R.layout.spinner_custom, viewGroup, false);
        }

        if(Data!=null){
//            String text = Data.get(i);
            ((TextView)view.findViewById(R.id.spinnerText)).setText(date);
        }

        return view;
    }

    //스피너를 열었을 때 나오는 뷰
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = Inflater.inflate(R.layout.spinner_dropdown, parent, false);
        }

        String text = Data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerText)).setText(text);

        return convertView;
    }
}

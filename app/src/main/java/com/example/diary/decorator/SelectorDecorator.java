package com.example.diary.decorator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.example.diary.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class SelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private CalendarDay date;

    public SelectorDecorator (Activity context){
        drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
        date = CalendarDay.today();
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        Log.d("확인", day.toString());

        return day.equals(date) && date != null;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }

    public void setDate(Date date){
        this.date = CalendarDay.from(date);
    }
}

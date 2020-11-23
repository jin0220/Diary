package com.example.diary.decorator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.example.diary.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public SelectorDecorator (Activity context){
        drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        Log.d("확인", day.toString());

        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }
}

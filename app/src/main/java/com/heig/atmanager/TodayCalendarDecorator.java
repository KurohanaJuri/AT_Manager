package com.heig.atmanager;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import androidx.core.content.ContextCompat;

/**
 * Author : Stéphane Bottin
 * Date   : 22.03.2020
 *
 * Decorator for today's calendar notification
 */
public class TodayCalendarDecorator implements DayViewDecorator {

    private CalendarDay today;
    private int color;

    public TodayCalendarDecorator(CalendarDay today, int color) {
        this.today = today;
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // We decorate all dates with notifications
        return day.equals(today);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CalendarDayToday(color));
    }
}

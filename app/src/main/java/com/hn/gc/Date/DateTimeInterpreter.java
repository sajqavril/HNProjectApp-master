package com.hn.gc.Date;

import java.util.Calendar;

public interface DateTimeInterpreter {
    String interpretDate(Calendar date);

    String interpretTime(int hour);

    String interpretWeek(int date);
}
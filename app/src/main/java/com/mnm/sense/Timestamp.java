package com.mnm.sense;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timestamp
{
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();

    private Timestamp()
    {
    }

    private Timestamp(int year, int month, int day, int hour, int minute, int second)
    {
        calendar.set(year, month, day, hour, minute, second);
    }

    public long millis()
    {
        return calendar.getTimeInMillis();
    }

    public String date()
    {
        return formatter.format(calendar.getTime());
    }

    public String time()
    {
        return calendar.getTime().toString();
    }

    public Timestamp set(int what, int when)
    {
        calendar.set(what, when);

        return this;
    }

    public Timestamp advance(int what, int value)
    {
        calendar.add(what, value);

        return this;
    }

    public static Timestamp now()
    {
        return new Timestamp();
    }

    public static Timestamp startOfToday()
    {
        Timestamp today = new Timestamp();

        today.set(Calendar.HOUR_OF_DAY, 0)
                .set(Calendar.MINUTE, 0)
                .set(Calendar.SECOND, 0);

        return today;
    }

    public static Timestamp endOfToday()
    {
        Timestamp today = new Timestamp();

        today.set(Calendar.HOUR_OF_DAY, 23)
                .set(Calendar.MINUTE, 59)
                .set(Calendar.SECOND, 59);

        return today;
    }

    public static Timestamp startOf(int year, int month, int day)
    {
        return new Timestamp(year, month, day, 0, 0, 0);
    }

    public static Timestamp endOf(int year, int month, int day)
    {
        return new Timestamp(year, month, day, 23, 59, 59);
    }
}

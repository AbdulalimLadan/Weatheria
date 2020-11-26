package com.z4rtx.weatheria.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String getTodayDate()
    {
        //Return today's date in dd/MM/yyyy format
        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH);
        return ft.format(dNow);
    }

    public String toDate(long timeStamp)
    {
        return new SimpleDateFormat("E dd", Locale.ENGLISH)
                .format(new Date(timeStamp * 1000L));
    }

}

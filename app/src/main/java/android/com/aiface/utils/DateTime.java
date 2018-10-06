package android.com.aiface.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {
    private final static String TAG = DateTime.class.getSimpleName();
    private static DateTime mDateTime;

    private DateTime() {

    }

    public static synchronized DateTime getDTInstance() {
        if(mDateTime == null) {
            mDateTime = new DateTime();
        }
        return mDateTime;
    }

    public long getSystemTime() {
        String systemTime;
        long time = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        systemTime = simpleDateFormat.format(date);
        try {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(systemTime).getTime();
            Log.d(TAG, "getSystemTime Time = " + time);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public String convertTimeToString(long times) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String timeString = formater.format(times);
        Log.d(TAG, "convertTimeToString times = " + times + ", string = " + timeString);
        return timeString;
    }

    public boolean isTheSameDay(String datetime) {
        boolean sameDay = false;
        Date nowDate, date, tempDate;
        String nowDateString;
        if(datetime != null) {
            date = timeStringToDate(datetime, "yyyy-MM-dd");

            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            tempDate = new Date(System.currentTimeMillis());
            nowDateString = formater.format(tempDate);
            nowDate = timeStringToDate(nowDateString, "yyyy-MM-dd");

            Log.d(TAG, "date compare = " + date.compareTo(nowDate));
            if(date.compareTo(nowDate) == 0) {
                sameDay = true;
            }else {
                sameDay = false;
            }
            Log.d(TAG, "is The Same Day" + sameDay);
        }
        return sameDay;
    }

    private Date timeStringToDate(String time, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Date date = null;
        try{
            date = formater.parse(time);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}

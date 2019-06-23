package demo.pratiked.twitterhash.util;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static String getRelativeTime(String time){

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        try {
            Date date = simpleDateFormat.parse(time);

            long currentTime = System.currentTimeMillis();

            return DateUtils.getRelativeTimeSpanString(date.getTime(),
                    currentTime, DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}

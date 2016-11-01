package pe.mplescano.mobile.myapp.poc03.support;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mplescano on 05/10/2016.
 */
public class Utils {

    public static Date parseDate(String dateUtc, String format) {
        Date dateResult = null;
        if (!TextUtils.isEmpty(dateUtc) && !TextUtils.isEmpty(format)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getDefault());
            try {
                return dateFormat.parse(dateUtc);
            } catch (Exception e) {
                Log.e("Utils.parseDate", e.getMessage(), e);
            }
        }
        return dateResult;
    }

    public static String formatDate(Date date, String format) {
        String resultFormat = null;
        if (date != null && !TextUtils.isEmpty(format)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getDefault());
            resultFormat = dateFormat.format(date);
        }

        return resultFormat;
    }

    public static String formatDateUtc(Date date, String format) {
        String resultFormat = null;
        if (date != null && !TextUtils.isEmpty(format)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            resultFormat = dateFormat.format(date);
        }

        return resultFormat;
    }

    public static String formatDateUtc(Date date) {
        return formatDateUtc(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static Date parseDateUtc(String dateUtc) {
        Date dateResult = null;
        if (!TextUtils.isEmpty(dateUtc)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return dateFormat.parse(dateUtc);
            } catch (Exception e) {
                Log.e("Utils.parseDateUtc", e.getMessage(), e);
            }
        }
        return dateResult;
    }

    public static void main(String[] args){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        System.out.println("date: " + date.toString());
        System.out.println("dateformat: " + dateFormat.format(date));
        System.out.println("dateformat-utc: " + formatDateUtc(date));

    }

    public static boolean isTwoPane(Resources resources, @StringRes int screenSize) {
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if ("small".equals(resources.getString(screenSize))) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }


}

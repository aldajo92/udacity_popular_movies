package com.android.aldajo92.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    public static final String MOVIE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String READABLE_DATE_FORMAT = "dd MMM yyyy";

    public static String changeDateFormat(String StringDate, String originalFormat, String newFormat) {
        String result = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(originalFormat, Locale.getDefault());
        try {
            Date date = dateFormat.parse(StringDate);
            dateFormat.applyPattern(newFormat);
            result = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}

package br.android.com.lembretes.uteis;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    private static final SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getInstance();
    private static String defaultDatePattern = "MM/dd/yyyy";

    public static Date strToDateTime (String data) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static synchronized String format(Date date, String pattern) {
        sdf.applyPattern(pattern == null ? defaultDatePattern : pattern);
        return sdf.format(date);
    }
}
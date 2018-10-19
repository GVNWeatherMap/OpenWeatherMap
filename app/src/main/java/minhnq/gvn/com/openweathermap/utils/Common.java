package minhnq.gvn.com.openweathermap.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "56757f8c2e7f418d9cfb2dd63c961001";

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String day = simpleDateFormat.format(date);
        return day;
    }
}

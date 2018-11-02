package minhnq.gvn.com.openweathermap.utils;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Common {
    public static final String APP_ID = "56757f8c2e7f418d9cfb2dd63c961001";

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String day = simpleDateFormat.format(date);
        return day;
    }

    public static String removeAccent(String s) {

        String cityName = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String nameNoAccent = pattern.matcher(cityName).replaceAll("").trim();
        return nameNoAccent.replaceAll(" ", "%20");
    }
}

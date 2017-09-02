package ekalaya.id.speedinvid.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Femmy on 8/27/2017.
 */

public class Helper {

    public static String formatTime(int duration){
        long seconds = (long) Math.ceil((double) duration/1000);
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if(seconds > 3600){
            return String.format("%d:%02d:%02d", h,m,s);
        } else {
            return String.format("%02d:%02d", m,s);
        }
    }

    public static int timeToInt(String timestampStr){
        //String timestampStr = "14:35:06";
        String[] tokens = timestampStr.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);
        int duration = 3600 * hours + 60 * minutes + seconds;
        return duration;
    }

    public static String ellipsis(final String text, int length){
        String last;
        if (text == null || text.length() < length) {
            last = text;
        } else {
            last = "..."+text.substring(text.length() - length);
        }
        return last;
    }


}

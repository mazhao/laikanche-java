package utils;

import play.Logger;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by mazhao on 15/1/11.
 */
public class Tools {


    public static String brandAndSeries(Long brandId, Long seriesId) {
        return brandId + "-" + seriesId;
    }

    public static Long brand(String brandAndSeries) {

        String[] items = brandAndSeries.split(Constants.SEPARATOR);
        if (items.length == 2 && items[0] != null) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("items[0]:" + items[0]);
                Logger.debug("items[1]:" + items[1]);

            }
            return new Long(items[0].trim());
        } else {
            if (Logger.isWarnEnabled()) {
                Logger.warn("brand and series format error: " + brandAndSeries);
            }
            return 0L;
        }
    }

    public static Long series(String brandAndSeries) {
        String[] items = brandAndSeries.split(Constants.SEPARATOR);

        if (items.length == 2 && items[1] != null) {
            if (Logger.isDebugEnabled()) {
                Logger.debug("items[0]:" + items[0]);
                Logger.debug("items[1]:" + items[1]);
            }

            return new Long(items[1].trim());
        } else {
            if (Logger.isWarnEnabled()) {
                Logger.warn("brand and series format error: " + brandAndSeries);
            }
            return 0L;
        }
    }


    private static final java.text.DateFormat dfYYYYMMDDHHMMSS = new java.text.SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_HHMMSS);

    public static String formatYYYYMMDDHHMMSS(Date date) {

        return dfYYYYMMDDHHMMSS.format(date);
    }

    public static Date parseYYYYMMDDHHMMSS(String date) {
        try {
            return dfYYYYMMDDHHMMSS.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }


    public static void main(String[] args) {
        Logger.debug("brand:" + Tools.brand("1-1"));
        Logger.debug("series:" + Tools.series("1-1"));

    }

}

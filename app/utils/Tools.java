package utils;

import org.springframework.util.FileSystemUtils;
import play.Logger;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

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


    public static final String getFileStorePath() {
        return Play.application().configuration().getString("laikanche.file.store");
    }

    public static final String generateFullFilePathInStore(String filename) {
        String folderPath = Tools.getFileStorePath();
        String fullFileName = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf('.'));
        if(!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }

        return folderPath + fullFileName;
    }

    public static final String getFullFilePathInStore(String fileName) {
        String folderPath = Tools.getFileStorePath();
        if(!folderPath.endsWith("/")) {
            folderPath = folderPath + "/";
        }

        return folderPath + fileName;
    }

    public static final String removePath(String fullpath) {
        return fullpath.substring(getFileStorePath().length() + 1);
    }


    public static String saveFileToStore(File file, String name) {
        String fullFilePathInStore = Tools.generateFullFilePathInStore(name);
        try {
            FileSystemUtils.copyRecursively(file, new File(fullFilePathInStore));
        } catch (IOException e) {
            Logger.error("上传文件保存失败", e);
            return "";
        }

        return fullFilePathInStore;
    }

    public static File getFileFromStore(String name) {
        String fullFilePathInStore = Tools.getFullFilePathInStore(name);
        return new File(fullFilePathInStore);
    }

    public static void main(String[] args) {
        Logger.debug("brand:" + Tools.brand("1-1"));
        Logger.debug("series:" + Tools.series("1-1"));

    }

}

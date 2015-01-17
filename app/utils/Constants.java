package utils;

import play.Play;

import java.util.UUID;

/**
 * Created by mazhao on 15/1/4.
 */
public class Constants {

    // bad performance but good for read
    public static final String OP_READ = "read";
    public static final String OP_CREATE =  "create";
    public static final String OP_UPDATE =  "update";
    public static final String OP_DELETE =  "delete";

    public static final String OP_ERROR = "error";


    // separator

    public static final String SEPARATOR = "\\-";


    // date format:yyyy-MM-dd HH:mm:ss
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";



    private Constants() {}
}

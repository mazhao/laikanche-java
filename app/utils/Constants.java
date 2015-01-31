package utils;

import play.Play;

import java.util.UUID;

/**
 * Created by mazhao on 15/1/4.
 */
public class Constants {

    public static final int COUNT_PER_PAGE = 12;

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
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMDD";

    public static final String ADMIN_URL_PREFIX = "/admin/";


    public static final String SESSION_ADMIN_NAME = "admin.name";
    public static final String SESSION_ADMIN_LOGIN = "admin.login";
    public static final String SESSION_ADMIN_LOGIN_TRUE = "true";

    private Constants() {}
}

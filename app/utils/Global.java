package utils;

import play.GlobalSettings;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

/**
 * Created by mazhao on 15/1/25.
 */
public class Global extends GlobalSettings {

    public Action onRequest(Http.Request request, Method actionMethod) {
        System.out.println("before each request..." + request.toString());


        return super.onRequest(request, actionMethod);
    }

}

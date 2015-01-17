package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.Tools;

/**
 * Created by mazhao on 15/1/17.
 */
public class CommonFileController extends Controller {
    public static Result index(String filename) {
        response().setHeader("Content-Disposition", "inline;fileName=" + filename);
        return ok(Tools.getFileFromStore(filename) );
    }
}

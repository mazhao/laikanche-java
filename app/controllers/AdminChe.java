package controllers;

import play.*;
import play.mvc.*;

/**
 * Created by mazhao on 15/1/1.
 */
public class AdminChe extends Controller {


    public static Result index() {
        return ok(views.html.adminChe.render("admin - che"));
    }


    public static Result read(Long id) {
        return ok("");
    }

    public static Result edit(Long id) {
        return ok("");
    }

    public static Result save() {

        return ok("");

    }

    public static Result delete(Long id) {
        return ok("");
    }


    // goto

    public  static Result gotoAdd() {
        return ok(views.html.adminCheAdd.render("添加新车评"));
    }

}

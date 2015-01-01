package controllers;

import play.*;
import play.mvc.*;  // java -> play.mvc; scala -> play.api.*


import views.html.*; // files in views folder

/**
 * Created by mazhao on 15/1/1.
 */
public class Admin extends Controller {

    public static Result index() {
        return ok(admin.render(""));
    }

}

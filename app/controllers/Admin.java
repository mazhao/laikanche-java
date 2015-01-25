package controllers;

import dtos.AdminLoginDTO;
import play.*;
import play.data.Form;
import play.mvc.*;  // java -> play.mvc; scala -> play.api.*


import views.html.*; // files in views folder

/**
 * Created by mazhao on 15/1/1.
 */
public class Admin extends Controller {

    private static Form<AdminLoginDTO> adminLoginDTOFormFactory = Form.form(AdminLoginDTO.class);


    public static Result index() {

        return ok(admin.render(""));
    }


    public static Result gotoLogin() {

        return ok(
                views.html.adminLogin.render(adminLoginDTOFormFactory.fill(new AdminLoginDTO()))
        );

    }


    public static Result login() {

        AdminLoginDTO adminLoginDTO = adminLoginDTOFormFactory.bindFromRequest().get();

        if(Logger.isDebugEnabled()) {
            Logger.debug("input name:" + adminLoginDTO.name);
            Logger.debug("input password:" + adminLoginDTO.password);
        }

        return ok(
               views.html.admin.render("ok")
        );

    }
}

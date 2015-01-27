package controllers;

import dtos.AdminLoginDTO;
import play.*;
import play.data.Form;
import play.mvc.*;  // java -> play.mvc; scala -> play.api.*


import utils.Constants;
import utils.Tools;
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

        if(Tools.isAdmin(adminLoginDTO.name, adminLoginDTO.password)) {
            Tools.setAdminLogin(session(), adminLoginDTO.name);
            return ok(
                    views.html.admin.render("ok")
            );
        } else {
            return ok(
                    views.html.adminLogin.render(adminLoginDTOFormFactory.fill(adminLoginDTO))
            );
        }
    }


    public static Result logout() {
        Tools.clearAdminLogin(session());
        return ok(
                views.html.adminLogin.render(adminLoginDTOFormFactory.fill(new AdminLoginDTO()))
        );
    }
}

package actions;

import dtos.AdminLoginDTO;
import play.Logger;
import play.data.Form;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.Tools;

/**
 * Created by mazhao on 15/1/27.
 */
public class AdminAuthAction extends Action.Simple {
    private static Form<AdminLoginDTO> adminLoginDTOFormFactory = Form.form(AdminLoginDTO.class);

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {

        if (Logger.isDebugEnabled()) {
            Logger.debug("enter admin auth action for " + context.request().uri());
        }

        if (Tools.isAdminUri(context.request().uri()) && !Tools.hasAdminLogin(context.session())) {
            // admin uri without administrator then goto login
            if (Logger.isDebugEnabled()) {
                Logger.debug("admin uri without administrator then goto login");
            }
            return F.Promise.pure(super.ok(views.html.adminLogin.render(adminLoginDTOFormFactory.fill(new AdminLoginDTO()))));

        }


        return delegate.call(context);
    }
}

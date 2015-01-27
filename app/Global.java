import dtos.AdminLoginDTO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.data.Form;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Results;
import utils.Tools;

import java.lang.reflect.Method;

/**
 * Created by mazhao on 15/1/25.
 */
public class Global extends GlobalSettings {
    public void onStart(Application app) {
        if(Logger.isDebugEnabled()) {
            Logger.debug("on start");
        }
    }

    public void onStop(Application app) {
        if(Logger.isDebugEnabled()) {
            Logger.debug("on stop");
        }
    }

    private static Form<AdminLoginDTO> adminLoginDTOFormFactory = Form.form(AdminLoginDTO.class);

    public Action onRequest(Http.Request request, Method actionMethod) {

        if (Logger.isDebugEnabled()) {

            Logger.debug("before each request..." + request.toString());
        }


//        if(Tools.isAdminUri(request.uri()) &&
//                !Tools.hasAdminLogin(session)  ) { // admin uri && not login as administrator
//
//            if(Logger.isErrorEnabled()) {
//                Logger.error("admin uri without administrator~");
//            }
//
//            return super.onRequest(request, actionMethod);
//
//        } else { // general uri
//            return super.onRequest(request, actionMethod);
//        }

        return super.onRequest(request, actionMethod);

    }

}

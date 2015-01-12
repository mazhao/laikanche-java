package controllers;

import com.avaje.ebean.Ebean;
import models.CarVideo;
import play.*;
import play.mvc.*;

import views.html.*;

import java.util.List;

public class Application extends Controller {

    public static Result index() {

        List<CarVideo> carVideoList = CarVideo.find.all();



        return ok(index.render(carVideoList));
    }

}

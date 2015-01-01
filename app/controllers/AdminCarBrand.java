package controllers;

import models.CarBrand;
import play.*;
import play.mvc.*;

import java.util.List;

/**
 * Created by mazhao on 15/1/1.
 */
public class AdminCarBrand  extends Controller{

    public static Result index () {

        List<CarBrand> carBrandList = CarBrand.find.all();


        return ok(views.html.adminCarBrand.render(carBrandList));
    }

}

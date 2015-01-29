package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import models.CarBrand;
import models.CarVideo;
import play.*;
import play.mvc.*;

import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    public static Result index(Long brandId) {

        if(Logger.isDebugEnabled()) {
            Logger.debug("enter brand id:" + brandId);
        }

        // @todo add cache here

        // step 1 get car brand list
        List<CarBrand> carBrandList = CarBrand.find.all();

        // step 2 get car video list
        Query<CarVideo> queryObject = null;
        if (brandId == 0) {
            queryObject =  CarVideo.find.orderBy().desc("createDate");
        } else {
            String query = "find carVideo where carSeries.carBrand.id = :brandId";
            queryObject = CarVideo.find.setQuery(query).setParameter("brandId", brandId).orderBy().desc("createDate");
        }

        List<CarVideo> carVideoList = queryObject.findList();

        return ok( views.html.index.render(carVideoList, carBrandList, brandId));
    }

}

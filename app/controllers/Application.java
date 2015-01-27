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

        List<CarVideo> carVideoList = null;
        List<CarBrand> carBrandList = CarBrand.find.all();


        if (brandId == 0) {
            carVideoList = CarVideo.find.all();
        } else {
            String query = "find carVideo where carSeries.carBrand.id = :brandId";


            Query<CarVideo> queryCarVideo = CarVideo.find.setQuery(query).setParameter("brandId", brandId);

//            if(Logger.isDebugEnabled()) {
//                Logger.debug("generate sql:" + queryCarVideo.getGeneratedSql());
//            }


            carVideoList = queryCarVideo.findList();

        }

        return ok( views.html.index.render(carVideoList, carBrandList, brandId));
    }

}

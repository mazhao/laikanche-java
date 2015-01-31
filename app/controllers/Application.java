package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import models.CarBrand;
import models.CarVideo;
import play.*;
import play.mvc.*;

import utils.Constants;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    public static Result index(Long brandId) {

        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

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

        // paging
        PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);

        int totalPageCount = carVideoPagingList.getTotalPageCount();

        int currentPage = page;

        List<CarVideo> carVideoList = carVideoPagingList.getPage(currentPage).getList();

        return ok( views.html.index.render(carVideoList, carBrandList, brandId, totalPageCount, currentPage));
    }

}

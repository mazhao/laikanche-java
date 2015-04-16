package controllers;

import com.avaje.ebean.*;
import models.CarBrand;
import models.CarItem;
import models.CarVideo;
import models.CarVideoTag;
import play.*;
import play.mvc.*;

import utils.Constants;
import utils.Tools;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {


    public static Result index(Long currentBrandId) {

        if (Logger.isDebugEnabled()) {
            Logger.debug("enter brand id:" + currentBrandId);
        }

        int currentPage = 0;
        try {
            currentPage = Integer.parseInt(getQueryParameter("page"));
        } catch (Exception ex) {
            currentPage = 0;
        }

        Long currentTagId = 0l;
        try {
            currentTagId = Long.parseLong(getQueryParameter("tag"));
        } catch (Exception ex) {
            currentTagId = 0l;
        }

        // @todo add cache here

        // step 1 get car brand list
        List<CarBrand> carBrandList = CarBrand.find.order().asc("name").findList();
        List<CarVideoTag> carVideoTagList = CarVideoTag.find.order().asc("name").findList();


        // step 2 get car video list
        //Query<CarVideo> queryObject = null;
        List<CarVideo> carVideoList = null;
        int totalPageCount = 0;

        if (currentBrandId == 0 && currentTagId != 0) {

            // only tag
            String queryStr = "select v.id, v.title, v.from_web, v.url, v.reporter, v.create_date, v.count_good, v.count_bad  from \n" +
                    "car_video v left outer join \n" +
                    "tag_video tv  on v.id = tv.video_id\n" +
                    "where \n" +
                    "    tv.tag_id = " + currentTagId;


            Query<CarVideo> queryObject = Ebean.find(CarVideo.class);
            queryObject.setRawSql(RawSqlBuilder.parse(queryStr).create()).orderBy().desc("createDate");
            PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);

            carVideoList = carVideoPagingList.getPage(currentPage).getList();
            totalPageCount = Tools.getRawSqlResultPageCount(queryStr);

        } else if (currentBrandId != 0 && currentTagId == 0) {
            // only brand
            String query = "find carVideo where carSeries.carBrand.id = :brandId";
            Query<CarVideo> queryObject = CarVideo.find.setQuery(query).setParameter("brandId", currentBrandId).orderBy().desc("createDate");
            PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);

            carVideoList = carVideoPagingList.getPage(currentPage).getList();
            totalPageCount = carVideoPagingList.getTotalPageCount();

        } else if (currentBrandId != 0 && currentTagId != 0) {
            // tag & brand
            String queryStr = "select v.id, v.title, v.from_web, v.url, v.reporter, v.create_date, v.count_good, v.count_bad   from \n" +
                    "car_video v,\n" +
                    "car_series s,\n" +
                    "tag_video tv\n" +
                    "where\n" +
                    "v.car_series_id = s.id\n" +
                    "and \n" +
                    "v.id = tv.video_id\n" +
                    "and\n" +
                    "s.car_brand_id =  " + currentBrandId + " \n" +
                    "and \n" +
                    "tv.tag_id = " + currentTagId;
            Query<CarVideo> queryObject = Ebean.find(CarVideo.class);
            queryObject.setRawSql(RawSqlBuilder.parse(queryStr).create()).orderBy().desc("createDate");
            PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);

            carVideoList = carVideoPagingList.getPage(currentPage).getList();
            totalPageCount = Tools.getRawSqlResultPageCount(queryStr);
        } else if (currentBrandId == 0 && currentTagId == 0) {
            // nothing to be filtered
            Query<CarVideo> queryObject = CarVideo.find.orderBy().desc("createDate");
            PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);

            carVideoList = carVideoPagingList.getPage(currentPage).getList();
            totalPageCount = carVideoPagingList.getTotalPageCount();
        } else {
            // nothing to return
        }

        return ok(views.html.index.render(carVideoList, carBrandList, currentBrandId, carVideoTagList, currentTagId, totalPageCount, currentPage));
    }

    public static Result evaluate(Long cvid, int type) {

        if (Logger.isDebugEnabled()) {
            Logger.debug("input car video id:" + cvid + " type:" + type);
        }

        if (cvid != null) {

            CarVideo carVideo = Ebean.find(CarVideo.class, cvid);

            if (Constants.EVALUATION_TYPE_GOOD == type) {
                carVideo.countGood++;
            } else if (Constants.EVALUATION_TYPE_BAD == type) {
                carVideo.countBad++;
            } else {
                // do nothing
            }

            Ebean.save(carVideo);
            if (Logger.isDebugEnabled()) {
                Logger.debug("evaluation saved!");
            }
        }
        return ok("ok");
    }


    public static Result item() {

        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        PagingList<CarItem> carItemPagingList = Ebean.find(CarItem.class).order().desc("createDate").findPagingList(Constants.COUNT_PER_PAGE);

        List<CarItem> carItemList = carItemPagingList.getPage(page).getList();


        int totalPageCount = 0;
        if (carItemList != null && carItemList.size() > 0) {
            totalPageCount = carItemPagingList.getTotalPageCount();
        }


        Logger.debug("after get total page count");

        int currentPage = page;

        return ok(views.html.item.render(carItemList, totalPageCount, currentPage));

    }

    /**
     *
     * @param param
     * @return
     */
    private static String getQueryParameter(String param) {
        String paramStr = request().getQueryString(param);
        if (paramStr == null) {
            paramStr = "";
        }
        return paramStr;
    }
}
package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSqlBuilder;
import models.CarBrand;
import models.CarVideo;
import models.CarVideoTag;
import play.*;
import play.mvc.*;

import utils.Constants;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {


    public static Result index(Long brandId) {

        // parse page
        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("enter brand id:" + brandId);
        }

        // parse tag query parameter
        String tagStr = request().getQueryString("tag");
        Long tagId = 0L;
        if (tagStr != null) {
            tagId = new Long(tagStr);
        }
        if (Logger.isDebugEnabled()) {
            Logger.debug("enter tag id:" + tagId);
        }


        // @todo add cache here

        // step 1 get car brand list
        List<CarBrand> carBrandList = CarBrand.find.all();

        //Long currentTagId = tag;

        List<CarVideoTag> carVideoTagList = CarVideoTag.find.all();


        // step 2 get car video list
        Query<CarVideo> queryObject = null;

        if(brandId == 0 && tagId != 0) {
            // only tag
           String queryStr = "select v.id, v.title, v.from_web, v.url, v.reporter, v.create_date, v.count_good, v.count_bad  from \n" +
                   "car_video v left outer join \n" +
                   "tag_video tv  on v.id = tv.video_id\n" +
                   "where \n" +
                   "    tv.tag_id = " + tagId;
            queryObject = Ebean.find(CarVideo.class);
            queryObject.setRawSql(RawSqlBuilder.parse(queryStr).create());

        } else if(brandId !=0 && tagId == 0) {
            // only brand
            String query = "find carVideo where carSeries.carBrand.id = :brandId";
            queryObject = CarVideo.find.setQuery(query).setParameter("brandId", brandId).orderBy().desc("createDate");

        } else if(brandId != 0 && tagId != 0) {
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
                    "s.car_brand_id =  " + brandId+ " \n" +
                    "and \n" +
                    "tv.tag_id = " + tagId;
            queryObject = Ebean.find(CarVideo.class);
            queryObject.setRawSql(RawSqlBuilder.parse(queryStr).create());

        } else if(brandId == 0 && tagId == 0) {
            // nothing to be filtered
            queryObject = CarVideo.find.orderBy().desc("createDate");
        } else {
            // nothing to return
        }


//
//        if (brandId == 0) {
//            queryObject = CarVideo.find.orderBy().desc("createDate");
//        } else {
//            String query = "find carVideo where carSeries.carBrand.id = :brandId";
//            queryObject = CarVideo.find.setQuery(query).setParameter("brandId", brandId).orderBy().desc("createDate");
//        }



        // paging
        PagingList<CarVideo> carVideoPagingList = queryObject.findPagingList(Constants.COUNT_PER_PAGE);



        int currentPage = page;
        List<CarVideo> carVideoList = carVideoPagingList.getPage(currentPage).getList();

        int totalPageCount = 0;
        if(carVideoList !=null && carVideoList.size() > 0) {
            totalPageCount = carVideoPagingList.getTotalPageCount();
        }
        return ok(views.html.index.render(carVideoList, carBrandList, brandId, carVideoTagList, tagId, totalPageCount, currentPage));
    }


    public static Result evaluate(Long cvid, int type) {

        if(Logger.isDebugEnabled()) {
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
            if(Logger.isDebugEnabled()) {
                Logger.debug("evaluation saved!");
            }
        }
        return ok("ok");
    }
}
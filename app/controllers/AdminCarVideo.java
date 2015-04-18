package controllers;

import actions.AdminAuthAction;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import dtos.CarVideoDTO;
import dtos.CarVideoTagDTO;
import models.CarSeries;
import models.CarVideo;
import models.CarVideoTag;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import utils.Constants;
import utils.Tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mazhao on 15/1/10.
 */
@With(AdminAuthAction.class)
public class AdminCarVideo extends Controller {

    private static Form<CarVideoDTO> carVideoFormFactory = Form.form(CarVideoDTO.class);


    public static Result index() {

        // 设置当前页码
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }
        PagingList<CarVideo> carVideoPagingList = CarVideo.find.order().desc("createDate").findPagingList(Constants.COUNT_PER_PAGE);


        int totalPageCount = carVideoPagingList.getTotalPageCount();
        int currentPage = page;


        List<CarVideo> carVideoList = carVideoPagingList.getPage(currentPage).getList();



        return ok(views.html.adminCarVideo.render(carVideoList, totalPageCount, currentPage));
    }

    public static Result read(Long id, String operation) {

        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        CarVideoDTO carVideoDTO = new CarVideoDTO();

        // prepare for dropdownload list
        carVideoDTO.carSeriesList = CarSeries.find.order().asc("carBrand.name").order().asc("name").findList();

        // show all tags here
        List<CarVideoTag> carVideoTagList = Ebean.find(CarVideoTag.class).findList();




        for(int i = 0;i < carVideoTagList.size(); i++) {
            CarVideoTag carVideoTag = carVideoTagList.get(i);

            CarVideoTagDTO carVideoTagDTO = new CarVideoTagDTO();
            carVideoTagDTO.id = carVideoTag.id;
            carVideoTagDTO.name = carVideoTag.name;
            carVideoTagDTO.description = carVideoTag.description;

            carVideoDTO.carVideoTagDTOList.add(carVideoTagDTO);
        }


        if (Constants.OP_DELETE.equalsIgnoreCase(operation) ||
                Constants.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constants.OP_READ.equalsIgnoreCase(operation)
                ) {
            CarVideo carVideo = Ebean.find(CarVideo.class, id);

            Long carSeriesId = carVideo.carSeries.id;
            Long carBrandId = carVideo.carSeries.carBrand.id;
            carVideoDTO.carBrandIdAndSeriesId = Tools.brandAndSeries(carBrandId, carSeriesId);

            carVideoDTO.countBad = carVideo.countBad;
            carVideoDTO.countGood = carVideo.countGood;
            carVideoDTO.createDate = Tools.formatYYYYMMDDHHMMSS(  carVideo.createDate );
            carVideoDTO.fromWeb = carVideo.fromWeb;
            carVideoDTO.title = carVideo.title;
            carVideoDTO.url = carVideo.url;
            carVideoDTO.reporter = carVideo.reporter;
            carVideoDTO.id = carVideo.id;
            carVideoDTO.screenFileName = carVideo.screenFileName;

            // add checked tags
            for(int i = 0; i < carVideo.tags.size(); i++) {
                CarVideoTag carVideoTag = carVideo.tags.get(i);
                carVideoDTO.tags.add(carVideoTag.id);
            }




            carVideoDTO.operationCode = operation;

        } else if(Constants.OP_CREATE.equalsIgnoreCase(operation)) {
            carVideoDTO.operationCode = Constants.OP_CREATE;
        } else {
            return redirect(routes.Admin.index()); // error to admin index
        }

        return ok(views.html.adminCarVideoRead.render(carVideoFormFactory.fill(carVideoDTO)));
    }

    public static Result saveOrUpdateOrDelete() {

        CarVideoDTO carVideoDTO = carVideoFormFactory.bindFromRequest().get();

        if(Constants.OP_CREATE.equalsIgnoreCase(carVideoDTO.operationCode)) {
            CarVideo carVideo = new CarVideo();

            carVideo.title = carVideoDTO.title;
            carVideo.fromWeb = carVideoDTO.fromWeb;
            carVideo.url = carVideoDTO.url;

            if(Logger.isDebugEnabled()) {
                Logger.debug("car brand and series:" + carVideoDTO.carBrandIdAndSeriesId);
            }

            Long seriesId = Tools.series(carVideoDTO.carBrandIdAndSeriesId);

            if(Logger.isDebugEnabled()) {
                Logger.debug("series id:" + seriesId);
                Logger.debug("car series object:" + Ebean.find(CarSeries.class, seriesId));
            }

            // 汽车视频截图保留位置
            Http.MultipartFormData.FilePart filePart = request().body().asMultipartFormData().getFile("screen");
            if(filePart != null && filePart.getFile()!= null) {
                String uniqueFilePath = Tools.saveFileToStore(filePart.getFile(), filePart.getFilename());
                carVideo.screenFileName = Tools.removePath(uniqueFilePath);
                carVideo.screenFileContentType = filePart.getContentType();
            }

            // add tags
            List<CarVideoTag> carVideoTagList = new ArrayList<CarVideoTag>();
            for (int i = 0; i < carVideoDTO.tags.size(); i++) {

                if(Logger.isDebugEnabled()) {
                    Logger.debug("tag id:" + carVideoDTO.tags.get(i));
                }

                if(carVideoDTO.tags.get(i) != null){

                    CarVideoTag carVideoTag = Ebean.find(CarVideoTag.class, carVideoDTO.tags.get(i));
                    carVideoTagList.add(carVideoTag);
                }
            }
            carVideo.tags = carVideoTagList;

            carVideo.carSeries = Ebean.find(CarSeries.class, seriesId);

            // init
            carVideo.countBad = 0L;
            carVideo.countGood = 0L;
            carVideo.createDate = new Date();
            carVideo.reporter = "拖拉机"; // @todo set user by session

            Ebean.save(carVideo);

        } else if(Constants.OP_UPDATE.equalsIgnoreCase(carVideoDTO.operationCode)) {

            CarVideo carVideo = new CarVideo();

            carVideo.id = carVideoDTO.id;
            carVideo.title = carVideoDTO.title;
            carVideo.fromWeb = carVideoDTO.fromWeb;
            carVideo.url = carVideoDTO.url;


            Long seriesId = Tools.series(carVideoDTO.carBrandIdAndSeriesId);
            carVideo.carSeries = Ebean.find(CarSeries.class, seriesId);

            // init
            carVideo.countBad = carVideoDTO.countBad;
            carVideo.countGood = carVideoDTO.countGood;
            carVideo.createDate = Tools.parseYYYYMMDDHHMMSS(  carVideoDTO.createDate );
            carVideo.reporter = "拖拉机"; // @todo set user by session

            // 汽车视频截图保留位置
            Http.MultipartFormData.FilePart filePart = request().body().asMultipartFormData().getFile("screen");
            if(filePart != null && filePart.getFile()!= null) {
                String uniqueFilePath = Tools.saveFileToStore(filePart.getFile(), filePart.getFilename());
                carVideo.screenFileName = Tools.removePath(uniqueFilePath);
                carVideo.screenFileContentType = filePart.getContentType();
            }
            carVideo.carSeries = Ebean.find(CarSeries.class, seriesId);


            // update all the tags
            // @todo bug add tags to create method
            List<CarVideoTag> carVideoTagList = new ArrayList<CarVideoTag>();
            for (int i = 0; i < carVideoDTO.tags.size(); i++) {

                if(Logger.isDebugEnabled()) {
                    Logger.debug("tag id:" + carVideoDTO.tags.get(i));
                }

                if(carVideoDTO.tags.get(i) != null){

                    CarVideoTag carVideoTag = Ebean.find(CarVideoTag.class, carVideoDTO.tags.get(i));
                    carVideoTagList.add(carVideoTag);
                }
            }
            carVideo.tags = carVideoTagList;

            Ebean.update(carVideo);

        } else if(Constants.OP_DELETE.equalsIgnoreCase(carVideoDTO.operationCode)) {
            Ebean.delete(CarVideo.class, carVideoDTO.id);
        }
        PagingList<CarVideo> carVideoPagingList = CarVideo.find.order().desc("createDate").findPagingList(Constants.COUNT_PER_PAGE);


        int totalPageCount = carVideoPagingList.getTotalPageCount();
        int currentPage = 0;

        List<CarVideo> carVideoList = carVideoPagingList.getPage(currentPage).getList();

        return ok(views.html.adminCarVideo.render(carVideoList, totalPageCount, currentPage));
    }
}

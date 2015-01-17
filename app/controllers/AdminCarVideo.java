package controllers;

import com.avaje.ebean.Ebean;
import dtos.CarVideoDTO;
import models.CarSeries;
import models.CarVideo;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Constants;
import utils.Tools;

import java.util.Date;

/**
 * Created by mazhao on 15/1/10.
 */
public class AdminCarVideo extends Controller {

    private static Form<CarVideoDTO> carVideoFormFactory = Form.form(CarVideoDTO.class);


    public static Result index() {
        return ok(views.html.adminCarVideo.render(CarVideo.find.all()));
    }

    public static Result read(Long id, String operation) {

        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        CarVideoDTO carVideoDTO = new CarVideoDTO();

        // prepare for dropdownload list
        carVideoDTO.carSeriesList = CarSeries.find.all();


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


            Ebean.update(carVideo);

        } else if(Constants.OP_DELETE.equalsIgnoreCase(carVideoDTO.operationCode)) {
            Ebean.delete(CarVideo.class, carVideoDTO.id);
        }

        return ok(views.html.adminCarVideo.render(CarVideo.find.all()));
    }
}

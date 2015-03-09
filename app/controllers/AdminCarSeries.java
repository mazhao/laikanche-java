package controllers;

import actions.AdminAuthAction;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import dtos.CarSeriesDTO;
import models.CarBrand;
import models.CarSeries;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Constants;

import java.util.List;

/**
 * Created by mazhao on 15/1/10.
 */
@With(AdminAuthAction.class)
public class AdminCarSeries extends Controller {

    private static Form<CarSeriesDTO> carSeriesFormFactory = Form.form(CarSeriesDTO.class);

    public static Result index() {

        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }


//        List<CarSeries> carSeriesList = CarSeries.find.order().desc("carBrand.name").order().desc("name").findList();


        PagingList<CarSeries> carSeriesPagingList = CarSeries.find.order().desc("carBrand.name").order().desc("name").findPagingList(Constants.COUNT_PER_PAGE);


        int totalPageCount = carSeriesPagingList.getTotalPageCount();
        int currentPage = page;


        List<CarSeries> carSeriesList = carSeriesPagingList.getPage(currentPage).getList();


        return ok(views.html.adminCarSeries.render(carSeriesList, totalPageCount, currentPage));



    }

    public static Result read(Long id, String operation) {
        if (operation == null) {
            operation = Constants.OP_ERROR;
        }



        CarSeriesDTO carSeriesDto = new CarSeriesDTO();

        // set car brand drop download list

        // @todo cache to improve performance
        List<CarBrand> carBrandList = Ebean.find(CarBrand.class).findList();
        carSeriesDto.carBrandList = carBrandList;
        carSeriesDto.operationCode = operation;

        // init
        if (Constants.OP_DELETE.equalsIgnoreCase(operation) ||
                Constants.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constants.OP_READ.equalsIgnoreCase(operation)
                ) {
            CarSeries carSeries = Ebean.find(CarSeries.class, id);
            carSeriesDto.carBrandId = carSeries.carBrand.id;
            carSeriesDto.id = carSeries.id;
            carSeriesDto.name = carSeries.name;
            carSeriesDto.description = carSeries.description;


        } else  if (Constants.OP_CREATE.equalsIgnoreCase(operation)) { // create
            carSeriesDto.operationCode = Constants.OP_CREATE;
        } else {
            return redirect(routes.AdminCarSeries.index());
        }

        return ok(views.html.adminCarSeriesRead.render( carSeriesFormFactory.fill(carSeriesDto)));

    }

    public static Result saveOrUpdateOrDelete() {


        CarSeriesDTO carSeriesForm = carSeriesFormFactory.bindFromRequest().get();

        if (Constants.OP_CREATE.equalsIgnoreCase(carSeriesForm.operationCode)) {
            // create
            // form validation goes here

            CarSeries carSeries = new CarSeries();
            carSeries.name = carSeriesForm.name;
            carSeries.description = carSeriesForm.description;
            CarBrand carBrand = Ebean.find(CarBrand.class, carSeriesForm.carBrandId);
            carSeries.carBrand = carBrand;

            Logger.debug(carSeries.toString());

            Ebean.save(carSeries);


        } else if (Constants.OP_UPDATE.equalsIgnoreCase(carSeriesForm.operationCode)) {
            CarSeries carSeries = new CarSeries();
            carSeries.id = carSeriesForm.id;
            carSeries.name = carSeriesForm.name;
            carSeries.description = carSeriesForm.description;
            CarBrand carBrand = Ebean.find(CarBrand.class, carSeriesForm.carBrandId);
            carSeries.carBrand = carBrand;
            Ebean.update(carSeries);
        } else if (Constants.OP_DELETE.equalsIgnoreCase(carSeriesForm.operationCode)) {
            Ebean.delete(CarSeries.class, carSeriesForm.id);
            //@todo check dependencies before delete
        }


        PagingList<CarSeries> carSeriesPagingList = CarSeries.find.order().desc("carBrand.name").order().desc("name").findPagingList(Constants.COUNT_PER_PAGE);


        int totalPageCount = carSeriesPagingList.getTotalPageCount();
        int currentPage = 0;


        List<CarSeries> carSeriesList = carSeriesPagingList.getPage(currentPage).getList();


        return ok(views.html.adminCarSeries.render(carSeriesList, totalPageCount, currentPage));
    }

}

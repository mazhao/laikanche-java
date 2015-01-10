package controllers;

import com.avaje.ebean.Ebean;
import dtos.CarSeriesDTO;
import models.CarBrand;
import models.CarSeries;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;

import java.util.List;

/**
 * Created by mazhao on 15/1/10.
 */
public class AdminCarSeries extends Controller {

    private static Form<CarSeriesDTO> carSeriesFormFactory = Form.form(CarSeriesDTO.class);

    public static Result index() {

        List<CarSeries> carSeriesList = CarSeries.find.all();

        return ok(views.html.adminCarSeries.render(carSeriesList));
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

        List<CarSeries> carSeriesList = CarSeries.find.all();
        return ok(views.html.adminCarSeries.render(carSeriesList));
    }

}

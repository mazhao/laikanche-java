package controllers;

import com.avaje.ebean.Ebean;
import forms.CarBrandForm;
import models.*;
import play.data.Form;
import play.mvc.*;
import utils.Constants;

import java.util.List;

/**
 * Created by mazhao on 15/1/1.
 */
public class AdminCarBrand extends Controller {
    private static Form<CarBrandForm> carBrandFormFactory = Form.form(CarBrandForm.class);


    public static Result index() {
        List<CarBrand> carBrandList = CarBrand.find.all();
        return ok(views.html.adminCarBrand.render(carBrandList));
    }


    // one action for save, update and delete
    public static Result read(Long id, String operation) {

        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        // 用原值建造Form
        CarBrandForm cbf = new CarBrandForm();

        if (Constants.OP_DELETE.equalsIgnoreCase(operation) ||
                Constants.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constants.OP_READ.equalsIgnoreCase(operation)
                ) { // update, delete, read
            // 从数据库查询原值
            CarBrand carBrand = Ebean.find(CarBrand.class, id);

            cbf.id = id;
            cbf.name = carBrand.name;
            cbf.description = carBrand.description;

            cbf.operationCode = operation;
        } else if (operation.equalsIgnoreCase(Constants.OP_CREATE)) { // create
            cbf.operationCode = Constants.OP_CREATE;
        } else {
            return redirect(routes.AdminCarBrand.index()); // @todo with error message
        }

        return ok(views.html.adminCarBrandRead.render(carBrandFormFactory.fill(cbf)));
    }

    public static Result saveOrUpdateOrDelete() {

        CarBrandForm carBrandForm = carBrandFormFactory.bindFromRequest().get();

        if (Constants.OP_CREATE.equalsIgnoreCase(carBrandForm.operationCode)) {
            // create
            // form validation goes here

            CarBrand carBrand = new CarBrand();
            carBrand.name = carBrandForm.name;
            carBrand.description = carBrandForm.description;

            Ebean.save(carBrand);

        } else if (Constants.OP_DELETE.equalsIgnoreCase(carBrandForm.operationCode)) {

            // @todo check sub modules not exist
            Ebean.delete(CarBrand.class, carBrandForm.id);

        } else if (Constants.OP_UPDATE.equalsIgnoreCase(carBrandForm.operationCode)) {
            CarBrand carBrand = Ebean.find(CarBrand.class, carBrandForm.id);
            carBrand.name = carBrandForm.name;
            carBrand.description = carBrandForm.description;

            Ebean.update(carBrand);
        } else {
           // @todo with error message
        }

        return redirect(routes.AdminCarBrand.index());

    }

}

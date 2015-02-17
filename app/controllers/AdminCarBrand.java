package controllers;

import actions.AdminAuthAction;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import controllers.routes;
import dtos.CarBrandDTO;
import models.*;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Constants;

import java.util.List;

/**
 * Created by mazhao on 15/1/1.
 */

@With(AdminAuthAction.class)
public class AdminCarBrand extends Controller {
    private static Form<CarBrandDTO> carBrandFormFactory = Form.form(CarBrandDTO.class);


    public static Result index() {
//        List<CarBrand> carBrandList = CarBrand.find.all();

        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        PagingList<CarBrand> carBrandPagingList = CarBrand.find.orderBy().desc("name").findPagingList(Constants.COUNT_PER_PAGE);

        int totalPageCount = carBrandPagingList.getTotalPageCount();
        int currentPage = page;

        List<CarBrand> carBrandList = carBrandPagingList.getPage(currentPage).getList();


//        List<CarBrand> carBrandList = CarBrand.find.orderBy().desc("name").findList();




        return ok(views.html.adminCarBrand.render(carBrandList, totalPageCount, currentPage));




    }


    // one action for save, update and delete
    public static Result read(Long id, String operation) {

        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        // 用原值建造Form
        CarBrandDTO cbf = new CarBrandDTO();

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

        return ok(views.html.adminCarBrandRead.render( carBrandFormFactory.fill(cbf)));
    }

    public static Result saveOrUpdateOrDelete() {

        CarBrandDTO carBrandForm = carBrandFormFactory.bindFromRequest().get();

        if (Constants.OP_CREATE.equalsIgnoreCase(carBrandForm.operationCode)) {
            // create
            // form validation goes here

            CarBrand carBrand = new CarBrand();
            carBrand.name = carBrandForm.name;
            carBrand.description = carBrandForm.description;

            Ebean.save(carBrand);

        } else if (Constants.OP_DELETE.equalsIgnoreCase(carBrandForm.operationCode)) {
            //@todo check dependencies before delete
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

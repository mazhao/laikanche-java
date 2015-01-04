package controllers;

import com.avaje.ebean.Ebean;
import forms.CarBrandForm;
import models.*;
import play.*;
import play.data.Form;
import play.mvc.*;
import utils.Constraints;

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


    public static Result gotoAdd() {
        return ok(views.html.adminCarBrandAdd.render(carBrandFormFactory));
    }

    public static Result add() {
        // 1. 添加一个新的车品牌


        CarBrandForm newCarBrandForm = carBrandFormFactory.bindFromRequest().get();

        // form validation goes here

        CarBrand carBrand = new CarBrand();
        carBrand.name = newCarBrandForm.name;
        carBrand.description = newCarBrandForm.description;

        Ebean.save(carBrand);

        // 2. 重新回到列表页
        return redirect(routes.AdminCarBrand.index());

    }


    public static Result gotoEdit(Long id) {

        // 从数据库查询原值
        CarBrand carBrand = Ebean.find(CarBrand.class, id);

        // 用原值建造Form
        CarBrandForm cbf = new CarBrandForm();
        cbf.id = id;
        cbf.name = carBrand.name;
        cbf.description = carBrand.description;

        // 把Form展现到页面。
        return ok(views.html.adminCarBrandEdit.render(carBrandFormFactory.fill(cbf)));
    }

    public static Result edit(Long id) {

        // 使用表单中的数据更新数据库中的车品牌。
        CarBrandForm carBrandForm = carBrandFormFactory.bindFromRequest().get();

        CarBrand carBrand = Ebean.find(CarBrand.class, id);
        carBrand.name = carBrandForm.name;
        carBrand.description = carBrandForm.description;

        Ebean.update(carBrand);

        // 2. 重新回到列表页
        return redirect(routes.AdminCarBrand.index());

    }


    public static Result detail(Long id) {

        // 从数据库查询原值
        CarBrand carBrand = Ebean.find(CarBrand.class, id);

        // 用原值建造Form
        CarBrandForm cbf = new CarBrandForm();
        cbf.id = id;
        cbf.name = carBrand.name;
        cbf.description = carBrand.description;

        // 把Form展现到页面。
        return ok(views.html.adminCarBrandDetail.render(carBrandFormFactory.fill(cbf)));
    }

    public static Result delete(Long id) {

        Ebean.delete(CarBrand.class, id);

        return redirect(routes.AdminCarBrand.index());
    }


    // one action for save, update and delete
    public static Result read(Long id, String operation) {

        if (operation == null) {
            operation = Constraints.OP_ERROR;
        }

        // 用原值建造Form
        CarBrandForm cbf = new CarBrandForm();

        if (Constraints.OP_DELETE.equalsIgnoreCase(operation) ||
                Constraints.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constraints.OP_READ.equalsIgnoreCase(operation)
                ) { // update, delete, read
            // 从数据库查询原值
            CarBrand carBrand = Ebean.find(CarBrand.class, id);

            cbf.id = id;
            cbf.name = carBrand.name;
            cbf.description = carBrand.description;

            cbf.operationCode = operation;
        } else if (operation.equalsIgnoreCase(Constraints.OP_CREATE)) { // create
            cbf.operationCode = Constraints.OP_CREATE;
        } else {
            return redirect(routes.AdminCarBrand.index()); // @todo with error message
        }

        return ok(views.html.adminCarBrandRead.render(carBrandFormFactory.fill(cbf)));
    }

    public static Result saveOrUpdateOrDelete() {

        CarBrandForm carBrandForm = carBrandFormFactory.bindFromRequest().get();

        if (Constraints.OP_CREATE.equalsIgnoreCase(carBrandForm.operationCode)) {
            // create
            // form validation goes here

            CarBrand carBrand = new CarBrand();
            carBrand.name = carBrandForm.name;
            carBrand.description = carBrandForm.description;

            Ebean.save(carBrand);

        } else if (Constraints.OP_DELETE.equalsIgnoreCase(carBrandForm.operationCode)) {
            Ebean.delete(CarBrand.class, carBrandForm.id);

        } else if (Constraints.OP_UPDATE.equalsIgnoreCase(carBrandForm.operationCode)) {
            CarBrand carBrand = Ebean.find(CarBrand.class, carBrandForm.id);
            carBrand.name = carBrandForm.name;
            carBrand.description = carBrandForm.description;

            Ebean.update(carBrand);
        } else {
            // error
            return redirect(routes.AdminCarBrand.index()); // @todo with error message

        }

        return redirect(routes.AdminCarBrand.index());

    }

}

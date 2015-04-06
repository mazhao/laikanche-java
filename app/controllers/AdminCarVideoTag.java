package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import dtos.CarVideoTagDTO;
import models.CarVideoTag;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.Tools;

import java.util.List;

/**
 * Created by mazhao on 15/4/6.
 */
public class AdminCarVideoTag extends Controller {

    private static Form<CarVideoTagDTO> carVideoTagFormFactory = Form.form(CarVideoTagDTO.class);


    public static Result index() {

        // 设置当前页码
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }
        PagingList<CarVideoTag> carVideoTagPagingList = CarVideoTag.find.findPagingList(Constants.COUNT_PER_PAGE);


        int totalPageCount = carVideoTagPagingList.getTotalPageCount();
        int currentPage = page;


        List<CarVideoTag> carVideoTagList = carVideoTagPagingList.getPage(currentPage).getList();



        return ok(views.html.adminCarVideoTag.render(carVideoTagList, totalPageCount, currentPage, ""));

    }


    public static Result read(Long id, String operation) {
        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        CarVideoTagDTO carVideoTagDTO = new CarVideoTagDTO();


        if (Constants.OP_DELETE.equalsIgnoreCase(operation) ||
                Constants.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constants.OP_READ.equalsIgnoreCase(operation)
                ) {

            CarVideoTag carVideoTag = Ebean.find(CarVideoTag.class, id);

            carVideoTagDTO.id = carVideoTag.id;
            carVideoTagDTO.name = carVideoTag.name;
            carVideoTagDTO.description = carVideoTag.description;
            carVideoTagDTO.operationCode = operation;

        } else if(Constants.OP_CREATE.equalsIgnoreCase(operation)) {
            carVideoTagDTO.operationCode = Constants.OP_CREATE;
        } else {
            return redirect(routes.Admin.index()); // error to admin index
        }

        return ok(views.html.adminCarVideoTagRead.render(carVideoTagFormFactory.fill(carVideoTagDTO)));
    }
    public static Result saveOrUpdateOrDelete() {

        CarVideoTagDTO carVideoTagForm = carVideoTagFormFactory.bindFromRequest().get();
        String message = "";

        if (Constants.OP_CREATE.equalsIgnoreCase(carVideoTagForm.operationCode)) {
            // create
            // form validation goes here

            CarVideoTag carVideoTag = new CarVideoTag();
            carVideoTag.name = carVideoTagForm.name;
            carVideoTag.description = carVideoTagForm.description;

            Ebean.save(carVideoTag);

        } else if (Constants.OP_DELETE.equalsIgnoreCase(carVideoTagForm.operationCode)) {
            // @todo: check video existences before delete tag
//            List<CarSeries> carSeriesList = Ebean.find(CarSeries.class).where().eq("carBrand.id", carBrandForm.id).findList();
//            if(carSeriesList != null && carSeriesList.size() > 0) {
//                message = "不能删除车品牌：" + carBrandForm.name + "\t 原因：不能删除已经关联了车系的车品牌，请首先删除所有车系";
//
//            } else {
                Ebean.delete(CarVideoTag.class, carVideoTagForm.id);
//            }
        } else if (Constants.OP_UPDATE.equalsIgnoreCase(carVideoTagForm.operationCode)) {
            CarVideoTag carVideoTag = Ebean.find(CarVideoTag.class, carVideoTagForm.id);
            carVideoTag.name = carVideoTagForm.name;
            carVideoTag.description = carVideoTagForm.description;

            Ebean.update(carVideoTag);
        } else {
            // @todo with error message
        }

//        return redirect(routes.AdminCarBrand.index());
        PagingList<CarVideoTag> carVideoTagPagingList = CarVideoTag.find.orderBy().desc("name").findPagingList(Constants.COUNT_PER_PAGE);

        int totalPageCount = carVideoTagPagingList.getTotalPageCount();
        int currentPage = 0;
        List<CarVideoTag> carVideoTagList = carVideoTagPagingList.getPage(currentPage).getList();

        return ok(views.html.adminCarVideoTag.render(carVideoTagList, totalPageCount, currentPage, message));

    }
}

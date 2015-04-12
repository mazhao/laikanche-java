package controllers;

import actions.AdminAuthAction;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import dtos.CarItemDTO;
import models.CarItem;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import utils.Constants;
import utils.Tools;

import java.util.Date;
import java.util.List;

/**
 * Created by mazhao on 15/4/12.
 */

@With(AdminAuthAction.class)
public class AdminCarItem extends Controller {
    private static Form<CarItemDTO> carItemFormFactory = Form.form(CarItemDTO.class);

    public static Result index() {


        // get current page if not then ist page
        String pageStr = request().getQueryString("page");
        int page = 0;
        if(pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        PagingList<CarItem> carItemPagingList = CarItem.find.orderBy().desc("title").findPagingList(Constants.COUNT_PER_PAGE);

        int totalPageCount = carItemPagingList.getTotalPageCount();
        int currentPage = page;

        List<CarItem> carItemList = carItemPagingList.getPage(currentPage).getList();

        return ok(views.html.adminCarItem.render(carItemList, totalPageCount, currentPage, ""));

    }

    public static Result read(Long id, String operation) {
        if (operation == null) {
            operation = Constants.OP_ERROR;
        }

        // 用原值建造Form
        CarItemDTO cif = new CarItemDTO();

        if (Constants.OP_DELETE.equalsIgnoreCase(operation) ||
                Constants.OP_UPDATE.equalsIgnoreCase(operation) ||
                Constants.OP_READ.equalsIgnoreCase(operation)
                ) { // update, delete, read
            // 从数据库查询原值
            CarItem carItem = Ebean.find(CarItem.class, id);

            cif.id = id;
            cif.title = carItem.title;
            cif.buyUrl = carItem.buyUrl;
            cif.description = carItem.description;
            cif.fromWeb = carItem.fromWeb;
            cif.keywords = carItem.keywords;
            cif.originalPrice= carItem.originalPrice;
            cif.promotionPrice = carItem.promotionPrice;

            cif.operationCode = operation;
        } else if (operation.equalsIgnoreCase(Constants.OP_CREATE)) { // create
            cif.operationCode = Constants.OP_CREATE;
        } else {
            return redirect(routes.AdminCarItem.index()); // @todo with error message
        }

        return ok(views.html.adminCarItemRead.render( carItemFormFactory.fill(cif)));    }

    public static Result saveOrUpdateOrDelete() {

        CarItemDTO carItemForm = carItemFormFactory.bindFromRequest().get();
        String message = "";
        if (Constants.OP_CREATE.equalsIgnoreCase(carItemForm.operationCode)) {
            // create
            // form validation goes here

            CarItem carItem = new CarItem();
            carItem.title = carItemForm.title;
            carItem.description = carItemForm.description;
            carItem.fromWeb = carItemForm.fromWeb;
            carItem.keywords = carItemForm.keywords;
            carItem.promotionPrice = carItemForm.promotionPrice;
            carItem.originalPrice = carItemForm.originalPrice;
            carItem.buyUrl = carItemForm.buyUrl;
            carItem.reporter = "拖拉机";
            carItem.createDate = new Date();

            // 商品主图
            Http.MultipartFormData.FilePart filePart = request().body().asMultipartFormData().getFile("screen");
            if(filePart != null && filePart.getFile()!= null) {
                String uniqueFilePath = Tools.saveFileToStore(filePart.getFile(), filePart.getFilename());
                carItem.screenFileName = Tools.removePath(uniqueFilePath);
                carItem.screenFileContentType = filePart.getContentType();
            }

            Ebean.save(carItem);

        } else if (Constants.OP_DELETE.equalsIgnoreCase(carItemForm.operationCode)) {

           Ebean.delete(CarItem.class, carItemForm.id);

        } else if (Constants.OP_UPDATE.equalsIgnoreCase(carItemForm.operationCode)) {
            CarItem carItem = Ebean.find(CarItem.class, carItemForm.id);

            carItem.title = carItemForm.title;
            carItem.description = carItemForm.description;
            carItem.fromWeb = carItemForm.fromWeb;
            carItem.keywords = carItemForm.keywords;
            carItem.promotionPrice = carItemForm.promotionPrice;
            carItem.originalPrice = carItemForm.originalPrice;
            carItem.buyUrl = carItemForm.buyUrl;

            // 商品主图
            Http.MultipartFormData.FilePart filePart = request().body().asMultipartFormData().getFile("screen");
            if(filePart != null && filePart.getFile()!= null) {
                String uniqueFilePath = Tools.saveFileToStore(filePart.getFile(), filePart.getFilename());
                carItem.screenFileName = Tools.removePath(uniqueFilePath);
                carItem.screenFileContentType = filePart.getContentType();
            }

            Ebean.update(carItem);
        } else {
            // @todo with error message
        }

        PagingList<CarItem> carItemPagingList = CarItem.find.orderBy().desc("createDate").findPagingList(Constants.COUNT_PER_PAGE);

        int totalPageCount = carItemPagingList.getTotalPageCount();
        int currentPage = 0;
        List<CarItem> carItemList = carItemPagingList.getPage(currentPage).getList();

        return ok(views.html.adminCarItem.render(carItemList, totalPageCount, currentPage, message));    }
}

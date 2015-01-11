package dtos;

import models.CarBrand;
import models.CarSeries;

import java.util.Date;
import java.util.List;

/**
 * Created by mazhao on 15/1/10.
 */
public class CarVideoDTO {

    // fields for dropdown list
    public List<CarSeries> carSeriesList;

    // --

    public Long id;

    public String carBrandIdAndSeriesId = "0-0"; // brandId-seriesId


    public String title; // 标题：加辣“小牛蛙”，保时捷Macan Turbo测试视频

    public String fromWeb; // 来源网站：xinchenping.com

    public String url; // 实际URL：http://v.youku.com/v_show/id_XNzk5MzIyMjU2.html

    public String reporter; // 谁添加的：拖拉机

    public String createDate; // 添加时间：2015年1月10日

    public Long countGood; // 好评数：1000

    public Long countBad;  // 差评数：20

    // -- operation code --
    public String operationCode;

}

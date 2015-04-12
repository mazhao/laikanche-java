package dtos;

import java.util.Date;

/**
 * Created by mazhao on 15/4/12.
 */
public class CarItemDTO {

    public Long id;

    // 标题、促销价格、原价、折扣
    // 关键字、更新时间
    // 图片、简单说明
    // 值、不值、购买链接

    public String title;
    public String fromWeb;


    public Double promotionPrice;
    public Double originalPrice;


    public String keywords; /* separated by comma */

    public String screenFileContentType;
    public String screenFileName;

    public String description;

    public String reporter; // 谁添加的：拖拉机
    public Date createDate; // 添加时间：2015年1月10日

    public Long countGood; // 好评数：1000
    public Long countBad;  // 差评数：20

    public String buyUrl;


    public String operationCode;

}

package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by mazhao on 15/4/11.
 */
@Entity
public class CarItem {
    @Id()
    public Long id;

    // 标题、来源、促销价格、原价、折扣
    // 关键字、更新时间
    // 图片、简单说明
    // 值、不值、购买链接

    @Constraints.Required
    public String title;

    @Constraints.Required
    public String fromWeb; /* 京东、聚划算等 */


    @Constraints.Required
    public Double promotionPrice;

    @Constraints.Required
    public Double originalPrice;


    @Constraints.Required
    public String keywords; /* separated by comma */

    @Constraints.Required
    public String screenFileContentType;

    @Constraints.Required
    public String screenFileName;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public String reporter; // 谁添加的：拖拉机

    @Constraints.Required
    public Date createDate; // 添加时间：2015年1月10日

    @Constraints.Required
    public Long countGood; // 好评数：1000

    @Constraints.Required
    public Long countBad;  // 差评数：20

    @Constraints.Required
    public String buyUrl;

    public static Model.Finder<Long,CarItem> find = new Model.Finder<Long,CarItem>(
            Long.class, CarItem.class
    );

}

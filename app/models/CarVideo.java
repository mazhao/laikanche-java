package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by mazhao on 15/1/10.
 * <p>
 * 1. 加辣“小牛蛙”，保时捷Macan Turbo测试视频 by xinchenping.com
 * 赞（1000） 踩（20） 2015年1月10日 by 拖拉机 添加
 * <p>
 * 看视频          -> http://v.youku.com/v_show/id_XNzk5MzIyMjU2.html
 * 踩、赞、评论     -> /video/999/comment
 */

@Entity
public class CarVideo {

    @Id()
    public Long id;

    @ManyToOne
    public CarSeries carSeries; // belong to which series of which brand

    @Constraints.Required
    public String title; // 标题：加辣“小牛蛙”，保时捷Macan Turbo测试视频

    @Constraints.Required
    public String fromWeb; // 来源网站：xinchenping.com

    @Constraints.Required
    public String url; // 实际URL：http://v.youku.com/v_show/id_XNzk5MzIyMjU2.html

    @Constraints.Required
    public String reporter; // 谁添加的：拖拉机

    @Constraints.Required
    public Date createDate; // 添加时间：2015年1月10日

    @Constraints.Required
    public Long countGood; // 好评数：1000

    @Constraints.Required
    public Long countBad;  // 差评数：20

    @Constraints.Required
    public String screenFileContentType;

    @Constraints.Required
    public String screenFileName;

    public static Model.Finder<Long, CarVideo> find = new Model.Finder<Long, CarVideo>(
            Long.class, CarVideo.class
    );
}

package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * Created by mazhao on 15/4/6.
 */
@Entity
public class CarVideoTag {

    @Id()
    public Long id;

    @Constraints.Required()
    public String name;

    @Constraints.Required()
    public String description;


    public static Model.Finder<Long, CarVideoTag> find = new Model.Finder<Long, CarVideoTag>(
            Long.class, CarVideoTag.class
    );
}

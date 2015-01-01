package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by mazhao on 15/1/2.
 */

@Entity
public class CarBrand extends Model {


    @Id()
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    public static Finder<Long,CarBrand> find = new Finder<Long,CarBrand>(
            Long.class, CarBrand.class
    );
}

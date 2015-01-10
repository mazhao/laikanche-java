package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by mazhao on 15/1/10.
 */
@Entity
public class CarSeries extends Model {

    @Id()
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    // object relationship

    @ManyToOne
    public CarBrand carBrand;

    public static Finder<Long,CarSeries> find = new Finder<Long,CarSeries>(
            Long.class, CarSeries.class
    );
}

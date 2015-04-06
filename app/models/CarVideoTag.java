package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "tag_video",
            joinColumns = @JoinColumn(name="tag_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id")

    )
    public List<CarVideo> videos;



    public static Model.Finder<Long, CarVideoTag> find = new Model.Finder<Long, CarVideoTag>(
            Long.class, CarVideoTag.class
    );
}

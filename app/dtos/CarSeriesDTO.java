package dtos;

import models.CarBrand;
import play.data.validation.Constraints;

import java.util.List;

/**
 * Created by mazhao on 15/1/10.
 */
public class CarSeriesDTO {

    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    public String operationCode;

    public Long carBrandId;

    public List<CarBrand> carBrandList;

}

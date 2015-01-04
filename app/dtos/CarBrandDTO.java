package dtos;


import play.data.validation.Constraints;

/**
 * Created by mazhao on 15/1/2.
 */
public class CarBrandDTO {
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    public String operationCode;


}

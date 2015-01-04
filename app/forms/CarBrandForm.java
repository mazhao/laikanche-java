package forms;


import play.data.validation.Constraints;

/**
 * Created by mazhao on 15/1/2.
 */
public class CarBrandForm {
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    public String operationCode;


}

package com.perfect.electronic.store.Validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface  ImageNameValid {
    //error msg
    String message() default "Invalid Image Name";
//Represent Group of Constraint
    Class<?>[] groups() default { };
//Additional information About Annotation
    Class<? extends Payload>[] payload() default { };
}

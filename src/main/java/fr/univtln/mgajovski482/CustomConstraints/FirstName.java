package fr.univtln.mgajovski482.CustomConstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@NotNull(message = "{personalInformations.firstName.null}")
@Size(min = 4, max = 50, message = "{personalInformations.firstName.size}")
@Target({FIELD, PARAMETER, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface FirstName {
    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
package fr.univtln.mgajovski482.CustomConstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Maxime on 30/03/2016.
 */

@NotNull(message = "{birthDate.null}")
@Past(message = "{birthDate.past")
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface BirthDate {
    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
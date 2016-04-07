package fr.univtln.mgajovski482.CustomConstraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Maxime on 30/03/2016.
 */

@NotNull(message = "{nickName.null}")
@Size(min = 3, max = 15, message = "{nickName.size")
//@Pattern(regexp = "[a-z0-9_-]{3,15}",message = "{nickName.invalidPattern}")
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface NickName {
    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
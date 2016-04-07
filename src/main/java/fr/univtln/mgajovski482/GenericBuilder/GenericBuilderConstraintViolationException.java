package fr.univtln.mgajovski482.GenericBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Maxime on 26/03/2016.
 */
public class GenericBuilderConstraintViolationException extends ConstraintViolationException {

    public GenericBuilderConstraintViolationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    public GenericBuilderConstraintViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
        Iterator<? extends ConstraintViolation<?>> iterator = constraintViolations.iterator();
            while(iterator.hasNext()){
                ConstraintViolation<?> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getSimpleName()+"."+cv.getPropertyPath() + " " +cv.getMessage());

            }
    }
}

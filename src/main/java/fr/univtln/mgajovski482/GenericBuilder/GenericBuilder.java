package fr.univtln.mgajovski482.GenericBuilder;

/**
 * Created by Maxime on 26/03/2016.
 */

import com.google.common.testing.ArbitraryInstances;

import javax.validation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class GenericBuilder<T>
{

    private final Class<?> clazz;
    private Map<String, Object> map;
    private static Validator validator;
    private static ValidatorFactory validatorFactory;


    public GenericBuilder(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
        this.map = new HashMap<>();
    }

    public static GenericBuilder<?> start(Class<?> clazz)
    {
        System.out.println("Yup" + validatorFactory);

        return new GenericBuilder<>(clazz);
    }

    public GenericBuilder<T> with(String name, Object value)
    {
        map.put(name, value);
        return this;
    }

    /**
     * Attempts to find the setter for the given property name
     *
     * @param instance
     * @param name
     *            the property
     * @param value
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private GenericBuilder<T> setProperty(T instance, String name, Object value)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException
    {
        try
        {
            if (value != null)
            {
                invoke(instance, name, value, value.getClass());
            }
            else
            {
                findMethodAndInvoke(instance, name, value);
            }
        }
        catch (NoSuchMethodException nm)
        {
            if (value.getClass() == java.lang.Integer.class)
            {
                invoke(instance, name, value, int.class);
            }
            else if (value.getClass() == java.lang.Long.class)
            {
                invoke(instance, name, value, long.class);
            }
            else if (value.getClass() == java.lang.Float.class)
            {
                invoke(instance, name, value, float.class);
            }
            else if (value.getClass() == java.lang.Double.class)
            {
                invoke(instance, name, value, double.class);
            }
            else if (value.getClass() == java.lang.Boolean.class)
            {
                invoke(instance, name, value, boolean.class);
            }
            else
            {
                findMethodAndInvoke(instance, name, value);
            }
        }
        return this;
    }

    /**
     * Iterates through all methods on the class to find the setter
     *
     * @param instance
     * @param name
     * @param value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodError
     */
    private void findMethodAndInvoke(T instance, String name, Object value)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodError
    {
        Method[] methods = clazz.getMethods();
        String setterName = getSetterName(name);
        boolean invoked = false;
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            if (method.getName().equals(setterName))
            {
                method.invoke(instance, value);
                invoked = true;
            }
        }
        if (!invoked)
        {
            throw new NoSuchMethodError(
                    "Cannot find method with name " + setterName);
        }
    }

    private String getSetterName(String name)
    {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void invoke(T instance, String name, Object value, Class<?> claz)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {


        Method method = clazz.getMethod(getSetterName(name), claz);

        method.invoke(instance, value);

    }

    public T build()
    {

        @SuppressWarnings("unchecked")
        T instance = (T) ArbitraryInstances.get(clazz);
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        try
        {
            for (Entry<String, Object> val : map.entrySet())
            {
              setProperty(instance, val.getKey(), val.getValue());

            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to set value with builder", e);
        }

        /**
         * Contrôle des attributs de l'instance
         */

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(instance);
        if (!constraintViolations.isEmpty()) {
            throw new GenericBuilderConstraintViolationException (new HashSet<>(constraintViolations));
        }
//            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
//            while(iterator.hasNext()){
//                ConstraintViolation<T> cv = iterator.next();
//                System.err.println(cv.getRootBeanClass().getSimpleName()+"."+cv.getPropertyPath() + " " +cv.getMessage());
//
//            }}

        return instance;
    }
}
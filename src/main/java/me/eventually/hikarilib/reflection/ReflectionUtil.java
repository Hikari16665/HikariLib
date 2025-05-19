package me.eventually.hikarilib.reflection;

import me.eventually.hikarilib.exception.ReflectionFailedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for reflection.
 * @author Eventually
 */
@SuppressWarnings("unused")
public class ReflectionUtil {
    private ReflectionUtil() {}
    /**
     * Sets a static field in a class.
     * @param clazz Target class.
     * @param fieldName Target field name.
     * @param value Value to set.
     * @throws ReflectionFailedException throws when reflection fails.
     */
    public static void setStaticField(Class<?> clazz, String fieldName, Object value) throws ReflectionFailedException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to set static field " + fieldName + " in " + clazz.getName(), e);
        }
    }
    /**
     * Sets a field in an object.
     * @param object Target object.
     * @param fieldName Target field name.
     * @param value Object to set.
     * @throws ReflectionFailedException throws when reflection fails.
     */
    public static void setField(Object object, String fieldName, Object value) throws ReflectionFailedException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to set field " + fieldName + " in " + object.getClass().getName(), e);
        }
    }
    /**
     * Gets a static field in a class.
     * @param clazz Target class.
     * @param fieldName Target field name.
     * @return Value of the field.
     * @throws ReflectionFailedException throws when reflection fails.
     */
    public static Object getStaticField(Class<?> clazz, String fieldName) throws ReflectionFailedException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to get static field " + fieldName + " in " + clazz.getName(), e);
        }
    }
    /**
     * Gets a field in an object.
     * @param object Target object.
     * @param fieldName Target field name.
     * @return Value of the field.
     * @throws ReflectionFailedException throws when reflection fails.
     */
    public static Object getField(Object object, String fieldName) throws ReflectionFailedException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to get field " + fieldName + " in " + object.getClass().getName(), e);
        }
    }
    /**
     * Call a static method in a class.
     * @param <T> Return type of the method.
     * @param clazz Target class.
     * @param methodName Target method name.
     * @param args Arguments to pass to the method.
     * @return The result of the method call, cast to type T.
     * @throws ReflectionFailedException if reflection or type casting fails.
     */
    public static <T> T callStaticMethod(Class<T> returnType, Class<?> clazz, String methodName, Object... args) throws ReflectionFailedException {
        try {
            Class<?>[] argClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            Object result = method.invoke(null, args);
            return returnType.cast(result);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionFailedException("Failed to call static method " + methodName + " in " + clazz.getName(), e);
        } catch (ClassCastException e) {
            throw new ReflectionFailedException("Failed to cast result of static method " + methodName + " in " + clazz.getName() + " to " + returnType.getName(), e);
        }
    }
    /**
     * Call a method in an object.
     * @param <T> Return type of the method.
     * @param object Target object.
     * @param methodName Target method name.
     * @param args Arguments to pass to the method.
     * @return The result of the method call, cast to type T.
     * @throws ReflectionFailedException if reflection or type casting fails.
     */
    public static <T> T callMethod(Class<T> returnType, Object object, String methodName, Object... args) throws ReflectionFailedException {
        try {
            Class<?>[] argClasses = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            Method method = object.getClass().getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            Object result = method.invoke(object, args);
            return returnType.cast(result);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionFailedException("Failed to call method " + methodName + " in " + object.getClass().getName(), e);
        } catch (ClassCastException e) {
            throw new ReflectionFailedException("Failed to cast result of method " + methodName + " in " + object.getClass().getName() + " to " + returnType.getName(), e);
        }
    }
}

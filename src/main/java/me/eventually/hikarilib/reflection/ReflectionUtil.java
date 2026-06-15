/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:50
 * @FilePath: src/main/java/me/eventually/hikarilib/reflection/ReflectionUtil.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.reflection;

import me.eventually.hikarilib.exception.ReflectionFailedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Java 反射工具类，提供便捷的反射操作封装。
 *
 * <pre>{@code
 * // 设置私有静态字段
 * ReflectionUtil.setStaticField(SomeClass.class, "fieldName", newValue);
 *
 * // 调用私有方法
 * String result = ReflectionUtil.callMethod(String.class, obj, "methodName", arg1, arg2);
 * }</pre>
 */
@SuppressWarnings("unused")
public class ReflectionUtil {
    private ReflectionUtil() {}

    /**
     * 设置类的静态字段值。
     *
     * @param clazz     目标类
     * @param fieldName 字段名
     * @param value     要设置的值，传 null 可将字段置为 null
     * @throws ReflectionFailedException 反射操作失败时抛出
     */
    public static void setStaticField(@NotNull Class<?> clazz, @NotNull String fieldName, @Nullable Object value) throws ReflectionFailedException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to set static field " + fieldName + " in " + clazz.getName(), e);
        }
    }
    /**
     * 设置对象的实例字段值。
     *
     * @param object    目标对象
     * @param fieldName 字段名
     * @param value     要设置的值，传 null 可将字段置为 null
     * @throws ReflectionFailedException 反射操作失败时抛出
     */
    public static void setField(@NotNull Object object, @NotNull String fieldName, @Nullable Object value) throws ReflectionFailedException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to set field " + fieldName + " in " + object.getClass().getName(), e);
        }
    }
    /**
     * 获取类的静态字段值。
     *
     * @param clazz     目标类
     * @param fieldName 字段名
     * @return 字段值，可能为 null
     * @throws ReflectionFailedException 反射操作失败时抛出
     */
    public static @Nullable Object getStaticField(@NotNull Class<?> clazz, @NotNull String fieldName) throws ReflectionFailedException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to get static field " + fieldName + " in " + clazz.getName(), e);
        }
    }
    /**
     * 获取对象的实例字段值。
     *
     * @param object    目标对象
     * @param fieldName 字段名
     * @return 字段值，可能为 null
     * @throws ReflectionFailedException 反射操作失败时抛出
     */
    public static @Nullable Object getField(@NotNull Object object, @NotNull String fieldName) throws ReflectionFailedException {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new ReflectionFailedException("Failed to get field " + fieldName + " in " + object.getClass().getName(), e);
        }
    }
    /**
     * 调用类的静态方法。
     *
     * @param returnType 返回值类型
     * @param clazz      目标类
     * @param methodName 方法名
     * @param args       方法参数
     * @param <T>        返回值类型
     * @return 方法调用结果
     * @throws ReflectionFailedException 反射操作或类型转换失败时抛出
     */
    public static <T> @Nullable T callStaticMethod(@NotNull Class<T> returnType, @NotNull Class<?> clazz, @NotNull String methodName, Object @NotNull ... args) throws ReflectionFailedException {
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
     * 调用对象的实例方法。
     *
     * @param returnType 返回值类型
     * @param object     目标对象
     * @param methodName 方法名
     * @param args       方法参数
     * @param <T>        返回值类型
     * @return 方法调用结果
     * @throws ReflectionFailedException 反射操作或类型转换失败时抛出
     */
    public static <T> @Nullable T callMethod(@NotNull Class<T> returnType, @NotNull Object object, @NotNull String methodName, Object @NotNull ... args) throws ReflectionFailedException {
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

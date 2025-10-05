/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/exception/ReflectionFailedException.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.exception;

/**
 * An exception that is thrown when a reflection operation fails.
 * <p>
 * Use getCause() to get the Original Exception
 */
public class ReflectionFailedException extends RuntimeException {
    public ReflectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

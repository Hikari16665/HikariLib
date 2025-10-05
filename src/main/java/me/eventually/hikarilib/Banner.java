/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 13:37:24
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 11:37:46
 * @FilePath: src/main/java/me/eventually/hikarilib/Banner.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib;

import java.util.logging.Logger;

public class Banner {
    private static final String banner = """
             █████   █████  ███  █████                           ███\s
            ░░███   ░░███  ░░░  ░░███                           ░░░ \s
             ░███    ░███  ████  ░███ █████  ██████   ████████  ████\s
             ░███████████ ░░███  ░███░░███  ░░░░░███ ░░███░░███░░███\s
             ░███░░░░░███  ░███  ░██████░    ███████  ░███ ░░░  ░███\s
             ░███    ░███  ░███  ░███░░███  ███░░███  ░███      ░███\s
             █████   █████ █████ ████ █████░░████████ █████     █████
            ░░░░░   ░░░░░ ░░░░░ ░░░░ ░░░░░  ░░░░░░░░ ░░░░░     ░░░░░\s
                                                                    \s
                                                                    \s
                                                                    \s
             █████        ███  █████                                \s
            ░░███        ░░░  ░░███                                 \s
             ░███        ████  ░███████                             \s
             ░███       ░░███  ░███░░███                            \s
             ░███        ░███  ░███ ░███                            \s
             ░███      █ ░███  ░███ ░███                            \s
             ███████████ █████ ████████                             \s
            ░░░░░░░░░░░ ░░░░░ ░░░░░░░░                              \s
            """;

    public static void printBanner(Logger logger) {
        for (String line : banner.split("\n")) {
            logger.info(line);
        }
    }
}

package me.eventually.hikarilib;

import java.util.List;
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

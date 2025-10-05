/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/server/ServerEnvironment.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.server;

public class ServerEnvironment {
    private  ServerEnvironment() {}

    public static boolean isFolia() {
        boolean isFolia;
        try {
            // this class is only available in Folia, used for detection
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
        return isFolia;
    }
}

package me.eventually.hikarilib.server;

public class ServerEnvironment {
    private  ServerEnvironment() {}

    public static boolean isFolia() {
        boolean isFolia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
        return isFolia;
    }
}

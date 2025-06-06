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

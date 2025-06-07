package me.eventually.hikarilib.inventory;


@FunctionalInterface
public interface HikariMenuDrawer {
    void draw(HikariMenu menu);

    HikariMenuDrawer EMPTY = menu -> {};
}

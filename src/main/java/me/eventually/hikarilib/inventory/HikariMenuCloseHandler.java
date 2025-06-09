package me.eventually.hikarilib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

@FunctionalInterface
public interface HikariMenuCloseHandler {
    void onClose(InventoryCloseEvent event, Player player, HikariMenu menu);

    HikariMenuCloseHandler DEFAULT = (event, player, menuDrawer) -> {};
}

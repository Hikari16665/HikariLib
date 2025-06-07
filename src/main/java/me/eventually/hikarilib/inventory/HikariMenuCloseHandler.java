package me.eventually.hikarilib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.awt.*;

public interface HikariMenuCloseHandler {
    void onClose(InventoryCloseEvent event, Player player, HikariMenu menu);

    HikariMenuCloseHandler DEFAULT = (event, player, menuDrawer) -> {};
}

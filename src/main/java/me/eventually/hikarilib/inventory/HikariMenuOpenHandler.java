package me.eventually.hikarilib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.awt.*;

public interface HikariMenuOpenHandler {
    void onOpen(InventoryOpenEvent event, Player player, HikariMenu menu);

    HikariMenuOpenHandler DEFAULT = (event, player, menuDrawer) -> {};
}

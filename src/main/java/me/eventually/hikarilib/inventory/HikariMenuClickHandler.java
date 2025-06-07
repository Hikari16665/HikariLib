package me.eventually.hikarilib.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.awt.*;

public interface HikariMenuClickHandler {
    void onClick(InventoryClickEvent event, int slot, HikariMenu menu);

    HikariMenuClickHandler DEFAULT = (event, slot, menu) -> {};
}

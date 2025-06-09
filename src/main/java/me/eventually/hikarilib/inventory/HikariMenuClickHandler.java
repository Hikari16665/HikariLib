package me.eventually.hikarilib.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface HikariMenuClickHandler {
    void onClick(InventoryClickEvent event, int slot, HikariMenu menu);

    HikariMenuClickHandler DEFAULT = (event, slot, menu) -> {};
    HikariMenuClickHandler NONE = (event, slot, menu) -> {
        event.setCancelled(true);
    };
}

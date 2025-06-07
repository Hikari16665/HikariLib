package me.eventually.hikarilib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class HikariMenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof HikariMenu menu) {
            HikariMenuItemStack item = menu.getItem(event.getSlot());
            if (item != null) {
                item.getClickHandler().onClick(event, event.getSlot(), menu);
            }
        }
    }
    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getHolder() instanceof HikariMenu menu) {
            HikariMenuCloseHandler closeHandler = menu.getCloseHandler();
            if (closeHandler != null) {
                closeHandler.onClose(event, (Player) event.getPlayer(), menu);
            }
        }
    }
    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getHolder() instanceof HikariMenu menu) {
            HikariMenuOpenHandler openHandler = menu.getOpenHandler();
            if (openHandler != null) {
                openHandler.onOpen(event, (Player) event.getPlayer(), menu);
            }
        }
    }
}

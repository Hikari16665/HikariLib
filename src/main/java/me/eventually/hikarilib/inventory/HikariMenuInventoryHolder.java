package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class HikariMenuInventoryHolder implements InventoryHolder {
    public abstract void setItem(int slot, HikariMenuItemStack item);
    public abstract HikariMenuItemStack getItem(int slot);
}

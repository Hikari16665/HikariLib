package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义菜单的 InventoryHolder 基类。
 * <p>实现该类的菜单可直接作为 Bukkit Inventory 的 holder 使用。</p>
 */
public abstract class HikariMenuInventoryHolder implements InventoryHolder {
    public abstract void setItem(int slot, @Nullable HikariMenuItemStack item);
    public abstract @Nullable HikariMenuItemStack getItem(int slot);
}

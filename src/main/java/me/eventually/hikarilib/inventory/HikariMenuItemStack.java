package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;

public interface HikariMenuItemStack {
    void setItemStack(ItemStack item);
    ItemStack getItemStack();

    HikariMenuClickHandler getClickHandler();
    void setClickHandler(HikariMenuClickHandler clickHandler);
}

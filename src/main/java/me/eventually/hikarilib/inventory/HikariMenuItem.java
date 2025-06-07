package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;

public class HikariMenuItem implements HikariMenuItemStack {
    private ItemStack item;
    private HikariMenuClickHandler clickHandler;

    public HikariMenuItem(ItemStack itemStack, HikariMenuClickHandler hikariMenuClickHandler) {
        this.item = itemStack;
        this.clickHandler = hikariMenuClickHandler;
    }

    @Override
    public void setItemStack(ItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public HikariMenuClickHandler getClickHandler() {
        return clickHandler;
    }

    @Override
    public void setClickHandler(HikariMenuClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
}

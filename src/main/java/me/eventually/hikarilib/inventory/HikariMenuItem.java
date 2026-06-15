/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 13:01:37
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/inventory/HikariMenuItem.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 菜单物品栈的默认实现。
 */
public class HikariMenuItem implements HikariMenuItemStack {
    private ItemStack item;
    private HikariMenuClickHandler clickHandler;

    /**
     * @param itemStack         物品栈
     * @param hikariMenuClickHandler 点击处理器，可为 null
     */
    public HikariMenuItem(@NotNull ItemStack itemStack, @Nullable HikariMenuClickHandler hikariMenuClickHandler) {
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
    public @Nullable HikariMenuClickHandler getClickHandler() {
        return clickHandler;
    }

    @Override
    public void setClickHandler(@Nullable HikariMenuClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
}

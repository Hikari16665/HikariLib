package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 菜单物品栈接口，封装了物品与点击行为。
 */
public interface HikariMenuItemStack {
    void setItemStack(@NotNull ItemStack item);
    @NotNull ItemStack getItemStack();

    @Nullable HikariMenuClickHandler getClickHandler();
    void setClickHandler(@Nullable HikariMenuClickHandler clickHandler);
}

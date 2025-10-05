/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 13:03:29
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:50
 * @FilePath: src/main/java/me/eventually/hikarilib/inventory/HikariMenu.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.inventory;

import lombok.Getter;
import lombok.Setter;
import me.eventually.hikarilib.entity.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class HikariMenu extends HikariMenuInventoryHolder {
    private Inventory inventory;
    private String title = "";
    private int rows = -1;
    private HikariMenuDrawer drawer;
    private HikariMenuOpenHandler openHandler;
    private HikariMenuCloseHandler closeHandler;

    private Map<Integer, HikariMenuItemStack> items = new HashMap<>();

    public static void setupListeners(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new HikariMenuListener(), plugin);
    }


    public HikariMenu(String title, int rows, HikariMenuOpenHandler openHandler, HikariMenuCloseHandler closeHandler, @NotNull HikariMenuDrawer drawer) {
        this.title = title;
        this.rows = rows;
        this.openHandler = openHandler;
        this.closeHandler = closeHandler;
        this.drawer = drawer;
        initInventory();
        drawer.draw(this);
    }

    private void initInventory() {
        if (rows == -1) {
            rows = (int) Math.ceil(items.size() / 9.0d);
        }
        rows = Math.min(rows, 6);
        inventory = Bukkit.createInventory(this, rows * 9, title.replace("&", "§"));
    }

    @Override
    public void setItem(int slot, HikariMenuItemStack item) {
        checkInitInventory();
        items.put(slot, item);
        inventory.setItem(slot, item == null ? null : item.getItemStack());
        for (Player player : getViewers()) {
            EntityUtil.runAsEntity(player, () -> player.openInventory(inventory));
        }
    }

    private List<Player> getViewers() {
        checkInitInventory();

        return inventory.getViewers()
                .stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .toList();
    }

    private void checkInitInventory() {
        if (inventory == null) {
            initInventory();
        }
    }

    @Override
    public HikariMenuItemStack getItem(int slot) {
        return items.get(slot);
    }

    public void open(Player player) {
        EntityUtil.runAsEntity(player, () -> player.openInventory(inventory));
    }
}

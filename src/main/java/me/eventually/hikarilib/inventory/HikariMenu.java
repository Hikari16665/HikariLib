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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义箱子菜单。
 *
 * <pre>{@code
 * HikariMenu menu = new HikariMatrixDrawer.Builder()
 *     .withTitle("&a菜单标题")
 *     .withRows(3)
 *     .withDrawer(new HikariMatrixDrawer(9)
 *         .addLine("XXX  XXXX")
 *         .addExplain('X', new ItemStack(Material.STONE)))
 *     .build();
 * menu.open(player);
 * }</pre>
 */
@Getter @Setter
public class HikariMenu extends HikariMenuInventoryHolder {
    private Inventory inventory;
    private String title = "";
    private int rows = -1;
    private HikariMenuDrawer drawer;
    private HikariMenuOpenHandler openHandler;
    private HikariMenuCloseHandler closeHandler;

    private Map<Integer, HikariMenuItemStack> items = new HashMap<>();

    /**
     * 注册菜单相关事件监听器，由 HikariLib 自动调用。
     */
    public static void setupListeners(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new HikariMenuListener(), plugin);
    }


    /**
     * 创建菜单。
     *
     * @param title        菜单标题，支持 & 颜色代码
     * @param rows         菜单行数，-1 为自动计算
     * @param openHandler  打开回调，可为 null
     * @param closeHandler 关闭回调，可为 null
     * @param drawer       菜单绘制器
     */
    public HikariMenu(@NotNull String title, int rows, @Nullable HikariMenuOpenHandler openHandler, @Nullable HikariMenuCloseHandler closeHandler, @NotNull HikariMenuDrawer drawer) {
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
    public void setItem(int slot, @Nullable HikariMenuItemStack item) {
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
    public @Nullable HikariMenuItemStack getItem(int slot) {
        return items.get(slot);
    }

    /**
     * 向指定玩家打开此菜单。
     *
     * @param player 目标玩家
     */
    public void open(@NotNull Player player) {
        EntityUtil.runAsEntity(player, () -> player.openInventory(inventory));
    }
}

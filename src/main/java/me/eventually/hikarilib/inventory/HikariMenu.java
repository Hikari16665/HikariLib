package me.eventually.hikarilib.inventory;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.MenuListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HikariMenu extends HikariMenuInventoryHolder{
    private Inventory inventory;
    private String title = "";
    private int rows = -1;
    private HikariMenuDrawer drawer;
    private HikariMenuOpenHandler openHandler;
    private HikariMenuCloseHandler closeHandler;

    private Map<Integer, HikariMenuItemStack> items = new HashMap<>();

    public static void setupListeners(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new HikariMenuListener(), plugin);
    }

    private HikariMenu() {
        items = new HashMap<>();
        drawer.draw(this);
    }

    private HikariMenu(String title, int rows, HikariMenuOpenHandler openHandler, HikariMenuCloseHandler closeHandler, HikariMenuDrawer drawer) {
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
            player.updateInventory();
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
        return null;
    }

    @Builder
    public static class MenuBuilder {
        private String title;
        private int rows;
        private HikariMenuOpenHandler openHandler;
        private HikariMenuCloseHandler closeHandler;
        private HikariMenuDrawer drawer;

        public HikariMenu build() {
            return new HikariMenu(title, rows, openHandler, closeHandler, drawer);
        }
    }
}

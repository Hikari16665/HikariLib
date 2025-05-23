package me.eventually.hikarilib.itemstack;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Wrapper class for ItemStack
 * @author Eventually
 */
@Getter
public class HikariItemStack {
    private final ItemStack wrapped;
    public HikariItemStack(@NotNull Material material) {
        if (!material.isItem()) {
            throw new IllegalArgumentException("Material is not an item");
        }
        this.wrapped = new ItemStack(material);
    }
    public HikariItemStack(@NotNull Material material, int amount) {
        if (!material.isItem()) {
            throw new IllegalArgumentException("Material is not an item");
        }
        this.wrapped = new ItemStack(material, amount);
    }
    public HikariItemStack(ItemStack itemStack) {
        this.wrapped = itemStack;
    }
    public ItemStack getItem() {
        return wrapped;
    }
    public HikariItemStack setAmount(int amount) {
        wrapped.setAmount(amount);
        return this;
    }
    public HikariItemStack setType(Material material) {
        wrapped.setType(material);
        return this;
    }
    public ItemMeta getItemMeta() {
        return wrapped.getItemMeta();
    }
    public boolean hasItemMeta() {
        return wrapped.hasItemMeta();
    }
    public Optional<List<String>> getLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(meta.getLore());
    }
    public HikariItemStack setLore(List<String> lore) {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.setLore(lore);
        wrapped.setItemMeta(meta);
        return this;
    }
    public HikariItemStack setItemMeta(ItemMeta itemMeta) {
        wrapped.setItemMeta(itemMeta);
        return this;
    }
    public Optional<String> getName() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.of(meta.getDisplayName());
    }
    public Optional<Integer> getCustomModelData() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.of(meta.getCustomModelData());
    }
    public HikariItemStack setName(String name) {
        return editItemMeta(meta -> {
            meta.setDisplayName(name);
            return meta;
        });
    }
    public HikariItemStack setCustomModelData(int data) {
        return editItemMeta(meta -> {
            meta.setCustomModelData(data);
            return meta;
        });
    }
    public HikariItemStack editItemMeta(Function<ItemMeta, ItemMeta> function) {
        ItemMeta itemMeta = wrapped.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        wrapped.setItemMeta(function.apply(itemMeta));
        return this;
    }
    public HikariItemStack editLore(Function<List<String>, List<String>> function) {
        return editItemMeta(meta -> {
            meta.setLore(function.apply(meta.getLore()));
            return meta;
        });
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HikariItemStack that)) return false;
        return wrapped.isSimilar(that.wrapped);
    }
    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }
    public static class Builder {
        private final Material material;
        private final int amount;
        private final String name;
        private final List<String> lore;

        public Builder(Material material, int amount, String name, List<String> lore) {
            this.material = material;
            this.amount = amount;
            this.name = name;
            this.lore = lore;
        }
        public HikariItemStack build() {
            return new HikariItemStack(material, amount).setName(name).setLore(lore);
        }
    }
    public static HikariItemStack getSkull(String uuid, String name, List<String> lore) {
        HikariItemStack stack = new Builder(Material.PLAYER_HEAD, 1, name, lore).build();
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile("HikariLib");
        try {
            profile.getTextures().setSkin(
                    new URL("http://textures.minecraft.net/texture/" + uuid)
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        meta.setOwnerProfile(profile);
        return stack.setItemMeta(meta);
    }
}

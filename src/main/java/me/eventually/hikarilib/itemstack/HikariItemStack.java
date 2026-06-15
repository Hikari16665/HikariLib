/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-05-28 22:35:13
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/itemstack/HikariItemStack.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.itemstack;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * ItemStack 包装类，提供链式调用的物品构建 API。
 *
 * <pre>{@code
 * HikariItemStack item = new HikariItemStack(Material.DIAMOND_SWORD)
 *     .setName("&6传说之剑")
 *     .setLore(List.of("&7一把传说中的剑", "&e攻击力 +10"))
 *     .setCustomModelData(1001);
 * player.getInventory().addItem(item.getItem());
 * }</pre>
 */
@Getter
@SuppressWarnings("unused")
public class HikariItemStack {
    private final ItemStack wrapped;

    private static String SKULL_NAME = "HikariLib";
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

    public static void setSkullProfileName(String profileName) {
        SKULL_NAME = profileName;
    }
    public ItemStack getItem() {
        return wrapped;
    }
    public @NotNull HikariItemStack setAmount(int amount) {
        wrapped.setAmount(amount);
        return this;
    }
    public @NotNull HikariItemStack setType(@NotNull Material material) {
        wrapped.setType(material);
        return this;
    }
    public @Nullable ItemMeta getItemMeta() {
        return wrapped.getItemMeta();
    }
    public boolean hasItemMeta() {
        return wrapped.hasItemMeta();
    }
    public @NotNull Optional<List<String>> getLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(meta.getLore());
    }

    public @NotNull List<? extends Component> getComponentLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return new ArrayList<>();
        }
        return meta.lore();
    }

    @NotNull HikariItemStack setComponentLore(@NotNull List<? extends Component> lore) {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(lore);
        wrapped.setItemMeta(meta);
        return this;
    }

    @NotNull HikariItemStack setLore(@NotNull List<String> lore) {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(lore.stream().map(Component::text).toList());
        wrapped.setItemMeta(meta);
        return this;
    }

    public @NotNull HikariItemStack removeLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(new ArrayList<>());
        wrapped.setItemMeta(meta);
        return this;
    }

    public @NotNull HikariItemStack setItemMeta(@NotNull ItemMeta itemMeta) {
        wrapped.setItemMeta(itemMeta);
        return this;
    }
    public @NotNull Optional<String> getName() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.of(meta.getDisplayName());
    }

    public @NotNull Component getComponentName() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Component.empty();
        }
        return meta.displayName();
    }

    public @NotNull Optional<Integer> getCustomModelData() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }
        return Optional.of(meta.getCustomModelData());
    }
    public @NotNull HikariItemStack setName(@NotNull String name) {
        return editItemMeta(meta -> {
            meta.setDisplayName(name);
            return meta;
        });
    }
    public @NotNull HikariItemStack setComponentName(@NotNull Component name) {
        return editItemMeta(meta -> {
            meta.displayName(name);
            return meta;
        });
    }
    public @NotNull HikariItemStack setCustomModelData(int data) {
        return editItemMeta(meta -> {
            meta.setCustomModelData(data);
            return meta;
        });
    }
    public @NotNull HikariItemStack editItemMeta(@NotNull Function<ItemMeta, ItemMeta> function) {
        ItemMeta itemMeta = wrapped.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        wrapped.setItemMeta(function.apply(itemMeta));
        return this;
    }
    public @NotNull HikariItemStack editLore(@NotNull Function<List<String>, List<String>> function) {
        return editItemMeta(meta -> {
            meta.setLore(function.apply(meta.getLore()));
            return meta;
        });
    }
    public @NotNull HikariItemStack editComponentLore(@NotNull Function<List<? extends Component>, List<? extends Component>> function) {
        return editItemMeta(meta -> {
            meta.lore(function.apply(meta.lore()));
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
        private final List<TextComponent> components;

        public Builder(Material material, int amount, String name, List<String> lore) {
            this.material = material;
            this.amount = amount;
            this.name = name;
            this.components = lore.stream().map(Component::text).toList();
        }
        public HikariItemStack build() {
            return new HikariItemStack(material, amount).setName(name).setComponentLore(components);
        }
    }
    public static class ComponentBuilder {
        private final Material material;
        private final int amount;
        private final String name;
        private final List<? extends Component> components;

        public ComponentBuilder(Material material, int amount, String name, List<? extends Component> components) {
            this.material = material;
            this.amount = amount;
            this.name = name;
            this.components = components;
        }
        public HikariItemStack build() {
            return new HikariItemStack(material, amount).setName(name).setComponentLore(components);
        }
    }
    public static @NotNull HikariItemStack getSkull(@NotNull String uuid, @NotNull String name, @NotNull List<String> lore) {
        HikariItemStack stack = new Builder(Material.PLAYER_HEAD, 1, name, lore).build();
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(SKULL_NAME);
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

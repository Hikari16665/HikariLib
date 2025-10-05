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
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Wrapper class for ItemStack
 * @author Eventually
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

    public List<? extends Component> getComponentLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return new ArrayList<>();
        }
        return meta.lore();
    }

    HikariItemStack setComponentLore(List<? extends Component> lore) {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(lore);
        wrapped.setItemMeta(meta);
        return this;
    }

    HikariItemStack setLore(List<String> lore) {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(lore.stream().map(Component::text).toList());
        wrapped.setItemMeta(meta);
        return this;
    }

    public HikariItemStack removeLore() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.lore(new ArrayList<>());
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

    public Component getComponentName() {
        ItemMeta meta = wrapped.getItemMeta();
        if (meta == null) {
            return Component.empty();
        }
        return meta.displayName();
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
    public HikariItemStack setComponentName(Component name) {
        return editItemMeta(meta -> {
            meta.displayName(name);
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
    public HikariItemStack editComponentLore(Function<List<? extends Component>, List<? extends Component>> function) {
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
    public static HikariItemStack getSkull(String uuid, String name, List<String> lore) {
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

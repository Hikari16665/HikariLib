/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-07 13:32:52
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:50
 * @FilePath: src/main/java/me/eventually/hikarilib/inventory/HikariMatrixDrawer.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 矩阵布局菜单绘制器，使用字符矩阵定义物品分布。
 *
 * <pre>{@code
 * HikariMatrixDrawer drawer = new HikariMatrixDrawer(9)
 *     .addLine("XXX  XXXX")
 *     .addLine("  X  X  X")
 *     .addExplain('X', new ItemStack(Material.STONE), (event, slot, menu) -> {
 *         event.getWhoClicked().sendMessage("点击了物品！");
 *     });
 * }</pre>
 */
public class HikariMatrixDrawer implements HikariMenuDrawer {
    private final int size;
    private final Map<Character, ItemStack> characterMap = new HashMap<>();
    private final Map<Character, HikariMenuClickHandler> clickHandlerMap = new HashMap<>();
    private final List<String> matrix = new ArrayList<>();

    /**
     * @param size 矩阵每行的长度（即菜单列数，通常为 9）
     */
    public HikariMatrixDrawer(int size) {
        this.size = size;
    }
    public @NotNull HikariMatrixDrawer addLine(@NotNull String line) {
        matrix.add(line);
        return this;
    }
    /**
     * 添加字符到物品的映射。
     */
    public @NotNull HikariMatrixDrawer addExplain(char c, @NotNull ItemStack item) {
        characterMap.put(c, new ItemStack(item));
        return this;
    }
    public @NotNull HikariMatrixDrawer addExplain(@NotNull String c, @NotNull ItemStack item) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        return this;
    }
    /**
     * 添加字符到物品 + 点击处理器的映射。
     */
    public @NotNull HikariMatrixDrawer addExplain(char c, @NotNull ItemStack item, @Nullable HikariMenuClickHandler clickHandler) {
        characterMap.put(c, new ItemStack(item));
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public @NotNull HikariMatrixDrawer addExplain(@NotNull String c, @NotNull ItemStack item, @Nullable HikariMenuClickHandler clickHandler) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public @NotNull HikariMatrixDrawer addClickHandler(char c, @Nullable HikariMenuClickHandler clickHandler) {
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public @NotNull HikariMatrixDrawer addClickHandler(@NotNull String c, @Nullable HikariMenuClickHandler clickHandler) {
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public int[] getCharPositions(@NotNull String s) {
        return getCharPositions(s.charAt(0));
    }
    public int[] getCharPositions(char c) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == c) {
                    positions.add(i * size + j);
                }
            }
        }
        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }
    @Override
    public HikariMatrixDrawer clone() {
        HikariMatrixDrawer clone = new HikariMatrixDrawer(size);
        clone.matrix.addAll(matrix);
        clone.characterMap.putAll(characterMap);
        clone.clickHandlerMap.putAll(clickHandlerMap);
        return clone;
    }

    public void draw(HikariMenu menu) {
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (characterMap.containsKey(c)) {
                    menu.setItem(i * 9 + j, new HikariMenuItem(characterMap.get(c), clickHandlerMap.get(c)));
                } else {
                    menu.setItem(i * 9 + j, null);
                }
            }
        }
    }
    public static class Builder {
        private String title = "";
        private int rows = -1;
        private HikariMenuDrawer drawer;
        private HikariMenuOpenHandler openHandler = HikariMenuOpenHandler.DEFAULT;
        private HikariMenuCloseHandler closeHandler = HikariMenuCloseHandler.DEFAULT;

        public @NotNull Builder withTitle(@NotNull String title) {
            this.title = title;
            return this;
        }

        public @NotNull Builder withRows(int rows) {
            this.rows = rows;
            return this;
        }

        public @NotNull Builder withDrawer(@NotNull HikariMenuDrawer drawer) {
            this.drawer = drawer;
            return this;
        }

        public @NotNull Builder withOpenHandler(@Nullable HikariMenuOpenHandler openHandler) {
            this.openHandler = openHandler;
            return this;
        }

        public @NotNull Builder withCloseHandler(@Nullable HikariMenuCloseHandler closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        /**
         * 构建菜单实例。
         */
        public @NotNull HikariMenu build() {
            return new HikariMenu(title, rows, openHandler, closeHandler, drawer);
        }
    }
}

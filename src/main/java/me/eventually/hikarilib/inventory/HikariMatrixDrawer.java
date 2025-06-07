package me.eventually.hikarilib.inventory;

import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HikariMatrixDrawer implements HikariMenuDrawer {
    private final int size;
    private final Map<Character, ItemStack> characterMap = new HashMap<>();
    private final Map<Character, HikariMenuClickHandler> clickHandlerMap = new HashMap<>();
    private final List<String> matrix = new ArrayList<>();

    public HikariMatrixDrawer(int size) {
        this.size = size;
    }
    public HikariMatrixDrawer addLine(String line) {
        matrix.add(line);
        return this;
    }
    public HikariMatrixDrawer addExplain(char c, ItemStack item) {
        characterMap.put(c, new ItemStack(item));
        return this;
    }
    public HikariMatrixDrawer addExplain(String c, ItemStack item) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        return this;
    }
    public HikariMatrixDrawer addExplain(char c, ItemStack item, HikariMenuClickHandler clickHandler) {
        characterMap.put(c, new ItemStack(item));
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public HikariMatrixDrawer addExplain(String c, ItemStack item, HikariMenuClickHandler clickHandler) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public HikariMatrixDrawer addClickHandler(char c, HikariMenuClickHandler clickHandler) {
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public HikariMatrixDrawer addClickHandler(String c, HikariMenuClickHandler clickHandler) {
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public int[] getCharPositions(String s) {
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
}

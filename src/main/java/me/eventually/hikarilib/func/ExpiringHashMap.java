/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-06-28 20:12:49
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-04 00:19:51
 * @FilePath: src/main/java/me/eventually/hikarilib/func/ExpiringHashMap.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带过期时间的 HashMap 实现，当元素超过指定生存时间后将被自动移除。
 *
 * <pre>{@code
 * ExpiringHashMap<UUID, String> map = new ExpiringHashMap<>(60000); // 60 秒过期
 * map.put(playerId, "some data");
 * String value = map.get(playerId); // 未过期时返回数据，过期返回 null
 * }</pre>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class ExpiringHashMap<K, V> implements Map<K, V>, AutoCloseable {
    private final Map<K, V> map = new ConcurrentHashMap<>();
    private final Map<K, Long> expirationMap = new ConcurrentHashMap<>();
    private final long expirationTimeMillis;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger size = new AtomicInteger(0);

    /**
     * @param expirationTimeMillis 元素过期时间（毫秒），必须为正数
     * @throws IllegalArgumentException 如果 expirationTimeMillis 不大于 0
     */
    public ExpiringHashMap(long expirationTimeMillis) {
        if (expirationTimeMillis <= 0) {
            throw new IllegalArgumentException("expirationTimeMillis must be positive.");
        }
        this.expirationTimeMillis = expirationTimeMillis;
        scheduleCleanup();
    }

    private void scheduleCleanup() {
        scheduler.scheduleAtFixedRate(this::cleanup, expirationTimeMillis, expirationTimeMillis, TimeUnit.MILLISECONDS);
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<K, Long>> iterator = expirationMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Long> entry = iterator.next();
            if (now >= entry.getValue()) {
                map.remove(entry.getKey());
                iterator.remove();
                size.decrementAndGet();
            }
        }
    }

    private boolean isExpired(K key) {
        Long expireTime = expirationMap.get(key);
        if (expireTime == null) return true;

        if (System.currentTimeMillis() >= expireTime) {
            map.remove(key);
            expirationMap.remove(key);
            size.decrementAndGet();
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return size.get() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (isExpired((K) key)) return false;
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (K key : keySet()) {
            if (get(key).equals(value)) return true;
        }
        return false;
    }

    @Override
    public @Nullable V get(Object key) {
        K k = (K) key;
        if (isExpired(k)) return null;
        return map.get(k);
    }

    @Override
    public V put(K key, V value) {
        V oldValue = map.put(key, value);
        long expireTime = System.currentTimeMillis() + expirationTimeMillis;

        expirationMap.put(key, expireTime);
        size.incrementAndGet();

        return oldValue;
    }

    @Override
    public @Nullable V remove(Object key) {
        K k = (K) key;
        if (isExpired(k)) return null;

        expirationMap.remove(k);
        size.decrementAndGet();
        return map.remove(k);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        map.clear();
        expirationMap.clear();
        size.set(0);
    }

    @Override
    public @NotNull Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (K key : map.keySet()) {
            if (!isExpired(key)) {
                keys.add(key);
            }
        }
        return keys;
    }

    @Override
    public @NotNull Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (K key : keySet()) {
            values.add(get(key));
        }
        return values;
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (K key : keySet()) {
            entries.add(new AbstractMap.SimpleEntry<>(key, get(key)));
        }
        return entries;
    }

    /**
     * 关闭并停止后台清理线程。使用完毕后应当调用此方法以避免线程泄漏。
     */
    @Override
    public void close() {
        scheduler.shutdownNow();
    }
}

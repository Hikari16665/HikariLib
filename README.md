# HikariLib

Paper 插件开发中随手抽出来的工具库，省得每个插件都写一遍。

## 依赖

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>me.eventually</groupId>
    <artifactId>HikariLib</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

需要 Paper 1.21.1，Java 21。

## 能干什么

### 菜单系统

用字符矩阵画菜单布局，所见即所得：

```java
HikariMatrixDrawer drawer = new HikariMatrixDrawer(9)
    .addLine("XXX  XXXX")
    .addLine("  X  X  X")
    .addExplain('X', new ItemStack(Material.STONE), (event, slot, menu) -> {
        event.getWhoClicked().sendMessage("点了石头");
    });

HikariMenu menu = new HikariMatrixDrawer.Builder()
    .withTitle("&6示例菜单")
    .withRows(3)
    .withDrawer(drawer)
    .build();

menu.open(player);
```

也可以实现 `HikariMenuDrawer` 接口自由绘制。

### 聊天输入

弹个问题等玩家回复，支持自定义超时：

```java
// 30 秒默认超时
ChatAsk.ask(player, "请输入名字：", name -> {
    player.sendMessage("你好，" + name);
});

// 10 秒超时
ChatAsk.ask(player, "输入验证码：", code -> {
    // ...
}, 10000);
```

### 数据库

统一的 `DataBaseConnection` 接口，MySQL 和 SQLite 都内置了 HikariCP 连接池：

```java
DataBaseConnection db = new MySQLConnection("localhost", 3306, "mydb", "root", "pass", false);
// 或者 new SQLiteConnection("plugins/xxx/data.db")

db.query("SELECT * FROM users WHERE age > ?", 18);
db.insert("users", null, "Alice", 25);

db.transaction(() -> {
    db.execute("UPDATE accounts SET balance = balance - 100 WHERE id = ?", 1);
    db.execute("UPDATE accounts SET balance = balance + 100 WHERE id = ?", 2);
    return null;
});
```

### 调度器

自动适配 Bukkit / Folia，不用写两套代码：

```java
HikariScheduler.addTimedTask(20, () -> player.sendMessage("Hello"), false);
HikariScheduler.addRepeatingTask(20, () -> player.sendMessage("Tick"), false);
```

### ItemStack 包装

链式构建物品：

```java
HikariItemStack item = new HikariItemStack(Material.DIAMOND_SWORD)
    .setName("&6传说之剑")
    .setLore(List.of("&7一把传说中的剑"))
    .setCustomModelData(1001);

player.getInventory().addItem(item.getItem());
```

### 反射工具

一行搞定反射调用：

```java
ReflectionUtil.setField(obj, "someField", newValue);
String result = ReflectionUtil.callMethod(String.class, obj, "methodName", arg1, arg2);
```

### 位置与传送

```java
// 获取位置所在区块
Chunk chunk = LocationUtils.getChunk(location);

// 获取区块内的偏移位置（0-15）
Location loc = LocationUtils.getChunkOffsetLocation(chunk, 5, 64, 5);

// 传送（Folia 自动异步，Bukkit 可指定）
LocationUtils.teleportPlayer(player, targetLocation);       // 同步
LocationUtils.teleportPlayer(player, targetLocation, true);  // Bukkit 异步
```

### 实体工具

以实体身份调度任务，Folia 上会自动走实体调度器，保证线程安全：

```java
EntityUtil.runAsEntity(player, () -> {
    player.openInventory(menu);
});
```

### 带过期时间的 Map

Map 里的条目超过指定时间自动蒸发，不需要手动清理：

```java
// 60 秒过期
ExpiringHashMap<UUID, String> cache = new ExpiringHashMap<>(60000);
cache.put(playerId, "some data");

// 没到 60 秒，拿到数据
String data = cache.get(playerId);

// 过了 60 秒，返回 null
String expired = cache.get(playerId);

// 用完记得关掉后台清理线程
cache.close();
```

### 服务端环境

检测运行环境，统一下游代码的分支判断：

```java
// Folia 检测
if (ServerEnvironment.isFolia()) {
    // 用 Folia 专用的调度 API
} else {
    // 用 Bukkit 的
}

// 获取版本信息
String version = ServerEnvironment.getMinecraftVersion();
String bukkitVersion = ServerEnvironment.getBukkitVersion();
String name = ServerEnvironment.getName();

// 获取完整的服务器元信息
ServerEnvironment.ServerMeta meta = ServerEnvironment.getMeta();
System.out.println(meta.getMotd());
System.out.println(meta.getMaxPlayers());
```

## 构建

```bash
mvn clean package
```

## 安装到本地

```bash
mvn install
```

然后放到 Paper 的 `plugins/` 目录即可。

## 许可证

MIT

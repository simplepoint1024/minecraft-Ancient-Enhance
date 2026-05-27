# minecraft-Ancient-Enhance

## 中文说明

这是一个面向 **Minecraft 1.20.1 / Forge 47.4.20** 的神化（Apotheosis）补丁模组，用来把整合包里所有远古（Ancient）相关修补从外置数据包 / 资源包收敛到一个正常的 Forge 模组里。

### 当前覆盖内容

- 远古 rarity 数据覆盖
- 远古重铸配方
- 远古分解配方
- 末地 affix loot entries 提升到可掉落远古装备
- 末地 Boss 掉落远古装备
- 远古材料模型、贴图、语言修补
- 将 `apotheosis:ancient_material` 加入创造模式原版材料页和神化冒险页

### 远古平衡修补

- 远古装备会自动写入原版 `Unbreakable`，实现真正无限耐久
- 远古工具的范围词缀上限从 `7x7` 提升到 `10x10`
- 远古装备最多可同时生成 **4 条能力型词缀**
- 远古装备的宝石孔位上限提升到 **9 孔**
- 保留 `omnetic` 与 `radial` 的并存空间

### 自定义词缀

- `ascendant`
  - 远古胸甲专属
  - 提供 `attributeslib:creative_flight`
  - 并已调整 `winged`，避免远古创造飞行与鞘翅飞行词缀共存
- `elytra_boost`
  - 远古剑专属
  - 鞘翅滑翔时右键可直接触发烟花推进
  - 共有 **1~3 级**，对应三档烟花推进时长
- `auto_smelt`
  - 远古镐子 / 铲子专属
  - 挖掘掉落若存在熔炉配方，会自动转为熔炼结果
  - 会一并补发对应的熔炼经验

### 环境

- Minecraft: `1.20.1`
- Forge: `47.4.20`
- Java: `17`
- Apotheosis: `7.4.8`
- Apothic Attributes / AttributesLib: `1.3.7`

### 构建

```bash
./gradlew build
```

构建产物输出到：

```text
build/libs/
```

### 备注

- 本仓库不会修改官方 `Apotheosis` 本体，而是通过伴生补丁模组承载整合包定制逻辑。
- 旧的 `/function modpack:apotheosis/ancient_mainhand` 辅助函数已不再保留。

---

## English

This is a **Minecraft 1.20.1 / Forge 47.4.20** companion patch mod for Apotheosis. It moves all modpack-specific Ancient-rarity fixes out of external datapacks/resourcepacks and into a normal Forge mod jar.

### Current coverage

- Ancient rarity data overrides
- Ancient reforging recipe
- Ancient salvaging recipe
- End affix loot entries upgraded to Ancient gear
- End bosses upgraded to drop Ancient gear
- Ancient material model, texture, and localization fixes
- Adds `apotheosis:ancient_material` to the vanilla ingredients tab and the Apotheosis adventure tab

### Ancient balance patches

- Ancient affix gear is automatically marked `Unbreakable` for true infinite durability
- Ancient breaker radial affix cap raised from `7x7` to `10x10`
- Ancient gear can now roll up to **4 ability affixes** at the same time
- Ancient socket capacity raised to **9 sockets**
- `omnetic` and `radial` are intentionally allowed to coexist

### Custom affixes

- `ascendant`
  - Ancient chestplate only
  - Grants `attributeslib:creative_flight`
  - `winged` is also adjusted so Ancient creative flight does not coexist with elytra-flight affixes
- `elytra_boost`
  - Ancient sword only
  - Right click while gliding with elytra to trigger built-in firework boosting
  - Has **3 levels**, matching the 3 vanilla firework flight durations
- `auto_smelt`
  - Ancient pickaxe / shovel only
  - Automatically converts mineable drops into their furnace-smelted result when possible
  - Also awards the matching smelting experience

### Environment

- Minecraft: `1.20.1`
- Forge: `47.4.20`
- Java: `17`
- Apotheosis: `7.4.8`
- Apothic Attributes / AttributesLib: `1.3.7`

### Build

```bash
./gradlew build
```

Build outputs are written to:

```text
build/libs/
```

### Notes

- This repository keeps upstream `Apotheosis` untouched and applies modpack-specific behavior through a companion patch mod.
- The old `/function modpack:apotheosis/ancient_mainhand` helper is intentionally no longer included.

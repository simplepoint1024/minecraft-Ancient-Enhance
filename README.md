# minecraft-Ancient-Enhance

Forge 1.20.1 patch mod for this modpack's Apotheosis ancient-rarity fixes.

## Scope

This mod moves the pack's Apotheosis ancient-related patches out of external datapacks and resourcepacks and into a normal Forge mod jar.

Included fixes:

- Ancient rarity data overrides
- Ancient reforging recipe
- Ancient salvaging recipe
- End affix loot entries upgraded to ancient
- End bosses upgraded to drop ancient gear
- Ancient material model, texture, and localization fixes
- Ancient chestplate-only `ascendant` affix using `attributeslib:creative_flight`
- `winged` adjusted so elytra flight does not coexist with the ancient creative-flight affix
- Adds `apotheosis:ancient_material` to the creative ingredients tab and Apotheosis adventure tab

## Environment

- Minecraft: `1.20.1`
- Forge: `47.4.20`
- Java: `17`
- Apotheosis: `7.4.8`
- Apothic Attributes / AttributesLib: `1.3.7`

## Build

```bash
./gradlew build
```

Built jars are written to:

```text
build/libs/
```

## Notes

- This repository keeps the official `Apotheosis` mod untouched and applies the pack-specific fixes as a companion mod.
- The old `/function modpack:apotheosis/ancient_mainhand` helper is intentionally not preserved here.

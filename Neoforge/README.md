# Hookshot — NeoForge 1.21.1 port

The **NeoForge 1.21.1** port of [Hookshot](https://www.curseforge.com/minecraft/mc-mods/hookshot)
by **Cammie** (CammiePone) and **Up** (UpcraftLP), living in this repository alongside the
original Fabric build. All game design, art, sounds, and original code belong to the original
authors — see [LICENSE](LICENSE).

Ported from Fabric 1.20.1 by [@mtb-xt](https://github.com/mtb-xt) and contributed back with the
author's blessing (see [issue #47](https://github.com/CammiesCorner/Hookshot/issues/47)). It is
feature-complete and has **no dependencies beyond NeoForge**.

## Building

```sh
JAVA_HOME=/path/to/java-21 ./gradlew build
```

The jar lands in `build/libs/`. Drop it into your `mods/` folder — **no other mods required**
(NeoForge only).

## Testing in dev

```sh
./gradlew runClient   # or runServer
```

## Automated tests

The port ships a 16-test [GameTest suite](TESTING.md) covering the upgrade data component,
all recipes (including network codec round-trips), and hook entity behaviour:

```sh
./gradlew runGameTestServer       # headless; fails the build on test failure
```

CI runs it on every push that touches this folder (`.github/workflows/neoforge_build.yml` at the
repository root). In the dev client, `/test runall` runs the suite in-world. See
[TESTING.md](TESTING.md) for coverage, limitations, and how to add tests.

## What changed in the port

| 1.20.1 Fabric | 1.21.1 NeoForge |
|---|---|
| Fabric API + Sparkweave registration | `DeferredRegister` |
| Cardinal Components (`hook_owner` player component) | NeoForge data attachment + custom sync payload |
| Resourceful Config | NeoForge `ModConfigSpec` (STARTUP config: `config/hookshot-startup.toml`, changes need a restart) |
| Upgrades in ItemStack NBT (`hookshot.Upgrades`) | `hookshot:upgrades` data component |
| `ItemStack#getMaxDamage` mixin for the durability upgrade | `minecraft:max_damage` component set on upgrade |
| `Item#getRarity` override | `minecraft:rarity` component set on upgrade |
| Gson recipe serializers | MapCodec + StreamCodec |
| Fabric `c:` tags (`c:red_dyes`, `c:iron_ingots`, ...) | unified 1.21 `c:` tags (`c:dyes/red`, `c:ingots/iron`, ...) |
| EMI/JEI/ModMenu compat plugins | dropped (recipes still work in the smithing table / crafting grid) |
| datagen | dropped; generated JSONs migrated to 1.21 formats by hand |

No mixins remain. Behavioural notes:

- The durability upgrade now actually multiplies durability (×2 by default). Upstream 1.20.1 disabled
  durability entirely via `canBeDepleted() == false` (hookshots never took damage); this port restores
  the intended durability mechanic — set `default_durability` very high if you prefer them unbreakable.
- Upgrades are stored as a data component, so 1.20.1 items' NBT upgrades won't carry over into
  existing worlds (item IDs and recipes are unchanged).

## Items / features (unchanged)

- 16 dyeable hookshots (craft: arrows + chain + iron + redstone + piston; re-dye with any dye)
- Smithing-table upgrades (no template needed): Aquatic, Automatic, Bleed, Durability, Enderic
  (teleport), Range, Speed
- `#hookshot:unhookable` block tag to forbid hooking onto blocks

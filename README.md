# Exploding Nether Ores Reforged

### Overview
A reforged continuation of the Netherending Ores concept: ores for the Nether and the End, with the classic Nether ore hazards.

The internal mod ID is now `explodingnetherores`. Worlds, datapacks, and configs using the old `netherendingores` ID are not migrated.

This branch targets **Minecraft 1.21.1** with **NeoForge 21.1.259 or newer within the 21.1.x line** and **Java 21**. It is built against 21.1.259 so it remains compatible with every 21.1.x release from that version onward (including the latest 21.1.265).

### Ore Generation
Native Nether worldgen is included. All Nether ores target the `c:netherracks` block tag and are added to the `c:is_nether` biome tag in the `underground_ores` generation step.

The biome tag is important for compatibility: `c:is_nether` includes `minecraft:is_nether`, so Nether biomes added by Biomes O' Plenty and other mods receive these ores without hard dependencies. Mods that add netherrack variants can also opt into the `c:netherracks` block tag to have ores replace those variants.

Default placement values:

| Ore | Vein size | Count per chunk | Y range | Height distribution |
|---|---:|---:|---|---|
| Nether Coal | 16 | 8 | 10-118 | Uniform |
| Nether Copper | 8 | 8 | 10-118 | Uniform |
| Nether Diamond | 3 | 4 | 10-118 | Uniform |
| Nether Emerald | 2 | 3 | 10-118 | Uniform |
| Nether Iron | 8 | 8 | 10-118 | Uniform |
| Nether Lapis | 6 | 6 | 10-118 | Uniform |
| Nether Redstone | 8 | 6 | 10-118 | Uniform |
| Nether Lead | 6 | 6 | 10-118 | Uniform |
| Nether Nickel | 6 | 4 | 10-118 | Uniform |
| Nether Osmium | 7 | 8 | 10-118 | Uniform |
| Nether Ruby | 3 | 6 | 10-118 | Uniform |
| Nether Sapphire | 3 | 6 | 10-118 | Uniform |
| Nether Silver | 4 | 6 | 10-118 | Uniform |
| Nether Tin | 8 | 8 | 10-118 | Uniform |
| Nether Uranium | 2 | 3 | 10-118 | Uniform |
| Nether Yellorite | 2 | 3 | 10-118 | Uniform |
| Nether Zinc | 5 | 3 | 10-118 | Uniform |
| Nether Amethyst | 3 | 4 | 10-118 | Uniform |
| Nether Apatite | 6 | 6 | 10-118 | Uniform |
| Nether Cinnabar | 4 | 4 | 10-118 | Uniform |
| Nether Fluorite | 4 | 4 | 10-118 | Uniform |
| Nether Green Sapphire | 3 | 6 | 10-118 | Uniform |
| Nether Niter | 4 | 5 | 10-118 | Uniform |
| Nether Sulfur | 4 | 6 | 10-118 | Uniform |
| Nether Teslatite | 4 | 8 | 10-118 | Uniform |
| Nether Tungsten | 4 | 4 | 10-118 | Uniform |

These values come from the historical Netherending Ores CoFH World configs where available. Newer ores use values matched to their material family. The generated worldgen JSON is data-driven and can be overridden by datapacks.

### Conditional Mod Ores
Only the vanilla-material ores (coal, copper, diamond, emerald, iron, lapis, redstone) always generate. Every other Nether ore is gated by installed mods and/or matching common tags:

| Ore | Built-in provider checks |
|---|---|
| Nether Tin | Mekanism, NuclearCraft, Electrodynamics, IC2 / IC2 Classic / IC2: Nostalgia, Thermal Foundation |
| Nether Lead | Mekanism, NuclearCraft, Electrodynamics, IC2 / IC2 Classic / IC2: Nostalgia, Thermal Foundation, Immersive Engineering |
| Nether Silver | NuclearCraft, Electrodynamics, IC2 Classic / IC2: Nostalgia, Thermal Foundation, Immersive Engineering |
| Nether Nickel | Thermal Foundation, Immersive Engineering |
| Nether Osmium | Mekanism |
| Nether Uranium | Mekanism, NuclearCraft, Electrodynamics, IC2 / IC2 Classic / IC2: Nostalgia, Extreme Reactors, Immersive Engineering |
| Nether Zinc | Create, NuclearCraft, Thermal Foundation |
| Nether Yellorite | Extreme Reactors (Big Reactors) |
| Nether Apatite | Thermal Foundation, Forestry |
| Nether Cinnabar | Thermal Foundation, Thaumcraft |
| Nether Fluorite | Mekanism, NuclearCraft, Electrodynamics, Thermal Foundation |
| Nether Niter | Electrodynamics, Thermal Foundation |
| Nether Sulfur | Mekanism, NuclearCraft, Electrodynamics, Thermal Foundation |
| Nether Ruby / Sapphire / Green Sapphire | Project Red |
| Nether Tungsten | NuclearCraft |
| Nether Teslatite | Tag-only; no known 1.21.1 provider |
| Nether Amethyst | Tag-only; only when another mod adds an amethyst ore |

The gate also passes when a matching common item or block tag contains an entry from another mod. This means mods not listed above can opt in simply by adding their materials to tags such as `c:ores/tin`, `c:raw_materials/tin`, `c:gems/ruby`, `c:dusts/sulfur`, or `c:ingots/tungsten`. For example, any BuildCraft or IndustrialCraft version that registers common ore tags will enable the matching Nether ore automatically.

BuildCraft Community Edition 1.21.1 does not register metal ore tags, so no Nether ore is tied to it by default. IndustrialCraft ports that use a different mod ID or custom tags can be added to the checks once their item IDs are known.

Drop tables use the same compatibility approach: metals and tungsten prefer raw materials then ingots, sulfur/niter/cinnabar/fluorite prefer gems then dusts, green sapphire accepts peridot, and yellorite accepts yellorium.

The End still has no native generation; use a datapack or worldgen mod for End ores.

### Drops and Tags
Ores are added to the `c:ores` block/item tags, their material-specific `c:ores/<material>` tags, and a per-ore tag (`explodingnetherores:<ore>`).

Nether and End ores of vanilla materials drop the matching vanilla item. Modded material ores try the matching common material tags (raw materials, ingots, gems, and dusts as appropriate), including compatibility aliases such as peridot for green sapphire and yellorium for yellorite, and fall back to dropping the ore block when no mod supplies a matching tag.

### Nether Ore Hazards

Nether ores use the classic risk/reward mechanics. Silk Touch disables both explosion priming and Zombified Piglin anger, and safely defuses an ore that was already primed.

| Difficulty | Piglin anger radius | Explosion chance per adjacent ore | Explosion power | Chain reactions |
|---|---:|---:|---:|---|
| Peaceful | Disabled | Disabled | - | - |
| Easy | 12 blocks | 9% | 1 | No |
| Normal | 24 blocks | 7.5% | 2 | Yes |
| Hard | 32 blocks | 15% | 3 | Yes |

- Breaking a Nether ore without Silk Touch angers nearby Zombified Piglins, even through walls. Switching the world to Peaceful clears their anger; switching back does not restore it.
- Only a real survival player starts the hazard. Fake players used by machines/automation, Creative players, and Spectator players do not anger Piglins or arm ores.
- This anger is local and short-lived. It is cleared when affected Piglins are reloaded from a chunk or when the player rejoins, so stale anger does not follow you into a later session.
- Vanilla Nether gold ore, gilded blackstone, and bastion chests can still anger Piglins through vanilla mechanics; those are not controlled by this mod's ore gate.
- Regular Piglins and Piglin Brutes are separate vanilla mobs: regular Piglins attack players who are not wearing gold armor, and Brutes attack on sight.
- When this mod angers Piglins, it writes a log line containing the ore name and position, which helps distinguish it from vanilla anger.
- Every non-Silk-Touch break rolls the explosion chance for each of the 26 directly adjacent Nether ores. A primed ore plays the TNT fuse sound and explodes after 80 ticks (4 seconds), whether or not its block is still there.
- Mining a primed ore without Silk Touch gives a fake ore with the real ore's name and appearance. It cannot be placed or used, has a maximum stack size of 1, and will not stack with real ore. The explosion still happens.
- On Normal and Hard, explosions can prime further ores, producing chain reactions. On Easy, only the initial mining roll can prime ores.
- Nether ores use the classic low blast resistance so an explosion can destroy them and feed the next link in the chain.
- Switching the difficulty to Peaceful also defuses any armed ores.
- Some Nether ore textures intentionally blend into netherrack and are hard to spot.

### Recipes
Recipe generation is not enabled in this port yet. The previous recipe provider was disabled in the 1.18 branch and referenced a placeholder tag, so it has been removed rather than carried forward broken.

### Development

Requirements:
- Java 21
- Internet access for the first Gradle/NeoForge setup

Common commands:

```text
gradlew.bat build
gradlew.bat runClient
gradlew.bat runServer
gradlew.bat runData
```

`runData` regenerates the contents of `src/generated/resources` from the providers in `org.icannt.netherendingores.datagen`.

### Code License
Code license was MIT for mod versions less than 1.4 but has been changed to GNU LGPL v3.0 as classes and parts of classes from Nether Foundation is now included in the source.\
Used with permission. https://github.com/TehSeph/NetherFoundation/ \
One class in the mod is GNU LGPL v2.1 from https://github.com/slump64/IC2Magma/blob/master/src/main/java/com/lavx64/ic2magma/recipes/IC2RecipeInput.java

### Art Licensing
License for textures based off of third party assets are as per their originating licenses:\
Uses the "ore" portions only of Thermal Foundation, Extreme Reactors, Tinkers' Construct, Applied Energistics 2, Mekanism, Aether Legacy/II, Project Red & Matter Overdrive textures.\
Uranium texture is from scratch based off vanilla iron as Industrial Craft 2 is not open source.\
Zinc texture is based off Railcraft's ore and is intended to be used for intermod integration as per license.
Other art is per "LICENSE.md"

### Code Credits
Nether Foundation Mod Project - TehSeph\
ICannt.org Team Member - LiamEarle (PrinceCat)

### Translation Credits
Brazilian Portuguese - SAMUELPV\
Korean - mindy15963\
Russian - Mr_Krab\
Simplified Chinese - G2159687 (K W)

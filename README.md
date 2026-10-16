[![](https://cf.way2muchnoise.eu/title/550263.svg)![](http://cf.way2muchnoise.eu/versions/550263.svg)![](https://cf.way2muchnoise.eu/full_550263_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/unseen-world)[![](https://img.shields.io/badge/All--Rights--Reserved-blue)](https://github.com/SashaKYotoz/Unseen_World/blob/master/LICENSE)[![](https://img.shields.io/discord/1155188824360624148?color=Green&label=Discord&logo=Discord&style=flat-square)](https://discord.gg/vjyzphNywy)

Status: **Stable**

<hr>

# About:

"Unseen World" - dimension-adding Minecraft mod,that shows dark version of Minecraft's
overworld, where sun was hidden for ages.

Chimeric Darkness - mod's dimension, where water is dark, and hazards is everywhere, but even here you can find friends
and a _bunch of adventures on your head_

![](https://media.forgecdn.net/attachments/description/550263/description_19f8be2b-93bb-4109-9c68-b35e9152eac8.png)

<hr>

Wiki for mod can be found on [sashakyotoz_dev](https://sashakyotoz.github.io/sashakyotoz_dev/)


The mod is translated into 2 languages:

- English(original) and Ukrainian - by SashaKYotoz


# Unseen World v1.0 changelog:

### Worldgen
- Chimeric Darkness now consists of 32 biomes divided on categories: caves, oceans, rivers, lowlands, valleys, midlands, highlands and boss structures dedicated biomes
- Concept of throughout warm dimension was reviewed, now dimension contains either cold, warm and hot biomes
- Terrain now follows vanilla multi-noise placement with depth bands: surface biomes occupy the top layer, cave biomes (Shiny Caverns, Burlywood Hollows, Tanzanite Caves) start about 25 blocks below the surface and Deep Glaciemite Caves lie below them
- Valley pillars and frigid ocean spires are now part of the terrain itself, so they appear only inside their own biomes and stay continuous across chunk borders
- New biomes: Penumbral Steppe, Auric Heights, Burlywood Hollows, Frigid Spire Ocean, Gloomweed Plains, Ashen Marsh, Crimsonveil Mire, Glaciemite Tundra, Snowy Grizzly Thicket and Glaciemite Peaks
- Every boss now has its own domain: The Darkness for the Warrior, Penumbral Steppe for the Eclipse Sentinel and Auric Heights for the Auric Patriarch, each with ruins that hint at the arena
- Spiral caves were rewritten: they carve at the configured height, respect the lava level and are continuous across chunk borders
- Cold biomes now freeze dark water into rime ice and cover the ground with snow, the warm Dark Ocean grows umbral coral reefs
- Penumbral Steppe has circles of corona stone with corona blooms, Burlywood Hollows have moss patches, glowroot vines and upside-down taproot trees, Shiny Caverns have ice chandeliers, frozen pools and hoarfrost
- Gleamcarver nests were added to the three cave biomes, each nest is a small shelter with a glowing core that gleamcarvers return to
- Corona stone now generates in Tanzanite Caves and Shiny Caverns and carries its own ores
- Warrior Tower structure was remade into Warrior Fortress: a ragged glaciemite fortress with a courtyard, a cathedral keep, six ordeal spawners and four chests that need parkour, climbing or digging to reach
- Eclipse Core structure was remade into Sentinel Stronghold and placed on surface, the structure now generates reliably and has halls guarded by Eclipsed Acolytes
- Auric Patriarch Citadel was added: a tower with a spiral ramp that leads to an open sky arena on top
- Ruins of each boss domain can now be found before the arena itself

### Visuals
- Sky elements now always visible, boss locked sky elements feature was removed. Compatibility with shaders was hardcoded, may conflict with some shaders
- Bosses got new effects drawn without textures: shockwaves, charge orbs, rune circles, laser beams, landing telegraphs and death rays
- Boss bars change their frame and title colour when a boss enters a new phase
- Reaver and Aureate Vex gained a flat coloured outline
- Eclipse Sentinel shows its eclipse disc behind its back during laser, Rise, Hyper Burst and Eclipse Pulse
- Effigy statues are drawn in grey stone and shatter when the boss awakens

### Entities
- The last third boss for the Chimeric Darkness, Auric Patriarch now completes the trio
- Both Warrior of the Chimeric Darkness and Eclipse Sentinel were completely remade and remodeled
- Gloomwhale entities were completely remade and remodeled
- Aureate Vex, Dreadflap, Eclipsed Acolyte, Reaver, Rimehorn, Shimmer, Venomer and Vexal Beetle entities were added
- All three bosses now fight in phases with health locks, so burst damage cannot skip a phase, and a transition during which the boss is immune
- Warrior of the Chimeric Darkness: new Fissure and Gripcrystal Thornfield attacks, and a second phase without the shield, with Shatter, Hammer Throw and Quake Chain
- Eclipse Sentinel: new Eclipse Pulse against melee spam, and a second phase with Totality, Corona Ring, Afterimage Dash and Split Laser
- Auric Patriarch: tactics picked by weight with cooldowns, never the same one twice in a row, an Ascension Rite that takes him to the air, and three phases with Falling Sun, Solar Judgement, Gilded Chains, Sacrifice and Final Sunburst
- Eclipsed Acolyte guards the stronghold as a dormant statue, wakes when it sees you or is hit, and returns to its post when you leave
- Bosses can be killed through any body part, with loot and kill credit, and weak points take extra damage
- Vexal Beetle can now be tamed with Gripping Membrane, ridden and flown with a stamina meter
- Gloomwhale is peaceful and escorts boats, Gripping Gloomwhale hunts players in water and rams boats from below
- Gloomwhales no longer circle in place
- Shimmer now spawns above umbral reefs
- Gleamcarver was rewritten: it returns to its nest, its ore regrows and the Shiny Caverns variant gives glaciemite
- Tuskhogs can now breed with each other
- Convertible mobs now trust the player who converted them
- Mob spawn rules were moved into one place and fixed, spawn egg and command summons skip rarity checks
- Boss tracking range increased

### Items
- Gripping mana was completely removed, boss weapon mana abilities were replaced with usage of griptonite and gripping crystals
- Chimeric Rockbreaker Hammer: shield breaking, a charged slam and a ground fissure
- Shield of Chimeric Warrior: normal shield blocking plus a parry that knocks the attacker back
- Eclipsebane: a wider sweep, a dash and Eclipse Pulse that costs Granulated Griptonite
- Auric Arc is now a trident with its own thrown entity, full-charge throws fire golden bolts and leave a Weakness cloud
- Gripping Abyssal Bow: full draw applies Gripping, holding longer fires a piercing Abyssal Shot
- Gripping Gauntlet disarms a target from a distance and respects PvP and cooldown
- Gripping Bundle can now supply ammo to weapons
- Gripping Membrane was added as a Dreadflap drop
- All weapon abilities share one cooldown per item, are checked on the server and use no custom keybinds
- Armor sets were rewritten: effects trigger once from the worn set, only when every piece is worn, and repair works only on damaged pieces
- Boss keys: gripcrystal, abyssal and auric keys now come from ordeals and open Effigies

### Blocks
- Key handler stone and translocate were removed and replaced with Effigy block
- Effigy is a 3x3 multiblock with a statue of the boss, summoning needs two keys and a puzzle of lit engraved bricks, and it resets when the boss dies
- Ordeal Spawner was added: a normal ordeal gives supplies, a gripping ordeal needs 10 Gripping and gives the boss key
- Engraved bricks carry power from gripping stones along their arrows and turn when a player lands on them
- New decoration blocks: penumbral grass block, corona stone with bricks, polished corona stone, stairs, slabs and walls, gilded currantslate with the same family, rime ice and hoarfrost
- New plants: corona bloom, frostbloom, ashreed, glowroot vines with glowroot bulb and umbral coral with fans and dead variants
- Taproot trees hang from cave ceilings and sometimes hide a Burlywood Heart
- Corona stone chest was added alongside the dark currantslate and glaciemite chests
- Wooden buttons and pressure plates have vanilla behaviour: no collision, correct press time and sounds of their wood
- Doors, trapdoors and plates use the sound and type of their own wood, fence gates are no longer fences
- Wood fuel values, sign stack size and wood-from-logs recipe now match vanilla
- Every sign drops its item
- All stone families got wall recipes and stonecutter recipes
- Dark water no longer has a block item
- Umbral coral blocks die out of dark water and survive inside it
- Gripping immune entities are now set through an entity tag

### Technical
- Registration moved to SolLib holders, all block, item and entity ids are unchanged
- Boss code shares one framework for state, parts, effects and combat helpers
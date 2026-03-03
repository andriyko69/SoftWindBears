# SoftWindBears

SoftWindBears is a small NeoForge (MC 1.21.1) mod that adds two new bear mobs:

- **Grizzly Bear** (`softwindbears:grizzly_bear`)
- **Panda Bear (bear variant)** (`softwindbears:bear_panda`)

Originally made for the **SoftWind** modpack, but it can be used standalone as well.  
Both bears share the same model/sounds — only the texture & behavior differ.

## Spawning
This mod ships default biome spawn rules via **NeoForge biome modifiers (datapack JSON)**:
- Panda Bear spawns in **Jungles** (`#minecraft:is_jungle`)
- Grizzly Bear spawns in **Taiga**, excluding **snowy/cold** taiga (`#minecraft:is_taiga` AND NOT `#minecraft:is_snowy` AND NOT `#minecraft:is_cold`)

If you want different spawn biomes / weights / group sizes, **override the biome modifier JSON via a datapack**.  
(Spawn tinkering is not exposed as a config option.)

## Config
Config file: `config/softwindbears-common.toml`

### bear_settings
- `areFriends` — if `true`, bears won’t target players
- `bearHealth` — max health
- `bearFollowRange`
- `bearAttackDamage`
- `bearSpeed`
- `bearAngerMinDuration` (ticks)
- `bearAngerMaxDuration` (ticks)

## Notes
- Spawn eggs are included for both bears.
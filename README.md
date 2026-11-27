# WornHorseshoes

Horseshoes for Forge 1.20.1. The Forge 1.12.2 version is on the `main` branch.

By Nischhelm; textures by Artsy; 1.20.1 port by Flankrat. Same mod id (`wornhorseshoes`) and item ids as 1.12.2.

## Features

- **Horseshoes** (iron, gold, diamond) in a new slot in the horse inventory, under the armor slot. They raise movement
  speed and jump strength (configurable per item), can be enchanted with boot enchantments (Depth Strider lets the horse
  carry its rider underwater, Feather Falling and Protection work on the horse) and are drawn on the horse's hooves.
  Dispensers put them on horses. Horseshoes always drop when the horse dies.
- **Trampling** (off by default): a horseshoe enchantment that knocks back and hurts mobs a galloping or rearing ridden
  horse runs into.
- **Lucky horseshoes** from other mods (configurable list) can be worn too; they look like gold horseshoes and stop fall
  damage for the horse and its rider.
- **Horse armor** on mules, zombie horses and skeleton horses (configurable), with configurable armor and toughness
  values per item, enchantable at the table and anvil, with the enchantment glint drawn on the horse.
- **Undead horses**: zombie horses burn in daylight unless armored; zombies (and husks, zombie villagers) sometimes spawn
  riding a zombie horse that despawns with them; zombie and skeleton-trap horses can spawn with armor and horseshoes.
- **Encounters**: village centres can have a shod horse; very rarely a village temple has Juan.
- **Leatherworkers** sell horseshoes (configurable trades).
- **Spawn stats**: configurable base and spread for speed, jump and health per horse type.
- **Stats display**: jump height (`^`, blocks) and top speed (`>`, blocks/second) in the horse screen, and under the
  horse's health bar when [Neat](https://modrinth.com/mod/neat) is installed.

Everything is set in `config/wornhorseshoes-common.toml`.

## Differences from the 1.12.2 version

- Horseshoes are stored in the horse's feet equipment slot, so donkey/mule chest contents keep their slots and vanilla
  handles saving, syncing and attribute modifiers. `/item replace entity <horse> armor.feet with ...` works.
- Left out because 1.20.1 already does it: the rearing fix and firing jump events for horses.
- Left out because the mods it supported don't exist on 1.20.1 in the same form: elytra on horses, grappling hook,
  PotionCore and BBLSOhMy compat, and the saddle glint layer.
- Village encounters hook 1.20 jigsaw village pieces (town centres and temples) instead of the old well and church.

## Building and publishing

`./gradlew build` with Java 17. The jar is in `build/libs`. Releases work like on 1.12.2: push a commit tagged with the
new version and the release workflow runs `./gradlew build publishMods` (CurseForge/Modrinth project ids are in
`gradle.properties`).

## License

MIT, see [LICENSE](LICENSE).

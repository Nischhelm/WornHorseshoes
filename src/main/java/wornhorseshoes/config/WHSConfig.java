package wornhorseshoes.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import wornhorseshoes.WornHorseshoes;

import java.util.*;

/**
 * The mod's settings (config/wornhorseshoes-common.toml). Same options as the 1.12.2 mod's wornhorseshoes.cfg, minus
 * the ones vanilla 1.20.1 / Forge already cover (rearing fix, LivingJumpEvent for horses) and the 1.12-only mod compat.
 * Lists are parsed into the caches below on load and on every reload, so the game code only reads ready-made maps.
 */
public final class WHSConfig {

    public static final ForgeConfigSpec SPEC;

    // [enchantments]
    public static final ForgeConfigSpec.BooleanValue DEPTH_STRIDER_NO_DISMOUNT;
    public static final ForgeConfigSpec.BooleanValue HORSE_ARMOR_ENCHANTABLE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HORSE_ARMOR_ENCHANT_BLACKLIST;
    public static final ForgeConfigSpec.BooleanValue HORSESHOES_ENCHANTABLE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HORSESHOES_ENCHANT_BLACKLIST;
    // [enchantments.trampling]
    public static final ForgeConfigSpec.BooleanValue TRAMPLING_ENABLED;
    public static final ForgeConfigSpec.IntValue TRAMPLING_MAX_LVL;
    public static final ForgeConfigSpec.EnumValue<Enchantment.Rarity> TRAMPLING_RARITY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> TRAMPLING_ENCHANTABILITY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRAMPLING_INCOMPATIBLE;
    public static final ForgeConfigSpec.DoubleValue TRAMPLING_KNOCKBACK;
    public static final ForgeConfigSpec.DoubleValue TRAMPLING_DAMAGE;
    // [acquisition]
    public static final ForgeConfigSpec.BooleanValue LEATHERWORKER_SELLS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> LEATHERWORKER_TRADES;
    public static final ForgeConfigSpec.BooleanValue WELL_ENCOUNTER_ENABLED;
    public static final ForgeConfigSpec.DoubleValue WELL_ENCOUNTER_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> WELL_ENCOUNTER_BLACKLIST;
    // [horse_armor]
    public static final ForgeConfigSpec.BooleanValue ALL_HORSES_USE_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ARMOR_HORSES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ARMOR_STATS;
    public static final ForgeConfigSpec.BooleanValue ARMOR_LAYER;
    // [horseshoes]
    public static final ForgeConfigSpec.BooleanValue ENABLE_HORSESHOES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HORSESHOE_STATS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HORSESHOE_MOBS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_HORSESHOE_ITEMS;
    public static final ForgeConfigSpec.BooleanValue HORSESHOES_LAYER;
    // [undead_horses]
    public static final ForgeConfigSpec.BooleanValue SKELETON_TRAPS_ARMORED;
    public static final ForgeConfigSpec.BooleanValue EQUIP_ALLOW_TREASURE;
    public static final ForgeConfigSpec.DoubleValue EQUIP_ENCHANT_MAX_CHANCE;
    public static final ForgeConfigSpec.IntValue EQUIP_ENCHANT_MAX_LVL;
    public static final ForgeConfigSpec.IntValue EQUIP_ENCHANT_MIN_LVL;
    public static final ForgeConfigSpec.DoubleValue MAX_EQUIP_CHANCE;
    public static final ForgeConfigSpec.BooleanValue BURN_ARMOR_PROTECTS;
    public static final ForgeConfigSpec.BooleanValue BURN_BABIES;
    public static final ForgeConfigSpec.EnumValue<UndeadType> BURN_TYPES;
    public static final ForgeConfigSpec.BooleanValue UNDEAD_BURN;
    public static final ForgeConfigSpec.BooleanValue ZOMBIE_HORSES_DESPAWN;
    public static final ForgeConfigSpec.DoubleValue ZOMBIE_RIDER_CHANCE;
    public static final ForgeConfigSpec.BooleanValue ZOMBIE_RIDER_ENABLED;
    // [horse_stats]
    public static final ForgeConfigSpec.BooleanValue DRAW_STATS;
    public static final ForgeConfigSpec.BooleanValue DRAW_STATS_NEAT;
    public static final ForgeConfigSpec.BooleanValue MODIFY_STATS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPAWN_STATS;

    /** Which undead horses burn in daylight. */
    public enum UndeadType { ZOMBIE, SKELETON, BOTH }

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("enchantments");
        DEPTH_STRIDER_NO_DISMOUNT = b.comment("Horses wearing horseshoes with any Depth Strider level don't throw their rider off under water.")
                .define("depthStriderNoDismount", true);
        HORSE_ARMOR_ENCHANTABLE = b.comment("Horse armor can be enchanted (table and anvil) with helmet and chestplate enchantments.")
                .define("horseArmorEnchantable", true);
        HORSE_ARMOR_ENCHANT_BLACKLIST = b.comment("Enchantments horse armor can never get, e.g. \"minecraft:thorns\".")
                .defineListAllowEmpty("horseArmorEnchantBlacklist", List.of(), WHSConfig::isString);
        HORSESHOES_ENCHANTABLE = b.comment("Horseshoes can be enchanted (table and anvil) with boot enchantments.")
                .define("horseshoesEnchantable", true);
        HORSESHOES_ENCHANT_BLACKLIST = b.comment("Enchantments horseshoes can never get.")
                .defineListAllowEmpty("horseshoesEnchantBlacklist", List.of(), WHSConfig::isString);
        b.comment("Trampling: a horseshoe enchantment that knocks back and hurts mobs a galloping or rearing ridden horse runs into.")
                .push("trampling");
        TRAMPLING_ENABLED = b.comment("Off: the enchantment does nothing and can't be found on tables, books or trades.")
                .define("enabled", false);
        TRAMPLING_MAX_LVL = b.defineInRange("maxLevel", 3, 1, 10);
        TRAMPLING_RARITY = b.defineEnum("rarity", Enchantment.Rarity.VERY_RARE);
        TRAMPLING_ENCHANTABILITY = b.comment("min, lvlspan, range: level N is available from table level min + (N-1) * lvlspan up to that plus range.")
                .defineList("enchantability", List.of(5, 20, 50), o -> o instanceof Integer);
        TRAMPLING_INCOMPATIBLE = b.comment("Enchantments that can't be combined with Trampling.")
                .defineListAllowEmpty("incompatible", List.of(), WHSConfig::isString);
        TRAMPLING_KNOCKBACK = b.comment("Knockback per level (half that while rearing).").defineInRange("knockbackPerLevel", 1.0, 0.0, 10.0);
        TRAMPLING_DAMAGE = b.comment("Damage per level (1.5x that while rearing).").defineInRange("damagePerLevel", 1.0, 0.0, 100.0);
        b.pop();
        b.pop();

        b.push("acquisition");
        LEATHERWORKER_SELLS = b.comment("Journeyman leatherworkers offer one horseshoe trade.").define("leatherworkerSellsHorseshoes", true);
        LEATHERWORKER_TRADES = b.comment("One entry per horseshoe: itemid, minPrice, maxPrice, weight[, minEnchLevel, maxEnchLevel]",
                        "Price is in emeralds, weight sets how often this one is picked (each leatherworker gets one).",
                        "Optional enchant levels enchant the horseshoes like an enchanting table at a random level in that range.")
                .defineList("leatherworkerTrades", List.of(
                        "horseshoes_diamond, 8, 10, 1",
                        "horseshoes_gold, 15, 20, 1, 5, 20",
                        "horseshoes_iron, 8, 10, 1"), WHSConfig::isString);
        WELL_ENCOUNTER_ENABLED = b.comment("Villages can have an untamed horse wearing random horseshoes at their center (the 1.12 village well),",
                        "and rarely Juan, a horse with enchanted gold horseshoes, at their temple.")
                .define("wellEncounterEnabled", true);
        WELL_ENCOUNTER_CHANCE = b.comment("Chance per village center. Juan's chance per temple is this / 100.")
                .defineInRange("wellEncounterChance", 0.3, 0.0, 1.0);
        WELL_ENCOUNTER_BLACKLIST = b.comment("Horseshoes that the village horse never wears (itemids).")
                .defineListAllowEmpty("wellEncounterBlacklist", List.of(), WHSConfig::isString);
        b.pop();

        b.push("horse_armor");
        ALL_HORSES_USE_ARMOR = b.comment("The horse types listed in armorHorses can wear horse armor too (horses always can).")
                .define("allHorseTypesUseArmor", true);
        ARMOR_HORSES = b.comment("Horse types (besides minecraft:horse) that can wear horse armor. Llamas can't (their slot holds carpets).")
                .defineListAllowEmpty("armorHorses", List.of("minecraft:mule", "minecraft:skeleton_horse", "minecraft:zombie_horse"), WHSConfig::isString);
        ARMOR_STATS = b.comment("Stats per horse armor item: itemid, armor, toughness.",
                        "Horse armor not listed keeps its vanilla armor value and no toughness.")
                .defineListAllowEmpty("armorStats", List.of(
                        "iron_horse_armor, 5, 0",
                        "golden_horse_armor, 7, 0",
                        "diamond_horse_armor, 11, 0"), WHSConfig::isString);
        ARMOR_LAYER = b.comment("Draw horse armor on the other horse types that can wear it, and an enchantment glint on enchanted horse armor.")
                .define("armorLayer", true);
        b.pop();

        b.push("horseshoes");
        ENABLE_HORSESHOES = b.comment("Main switch for the horseshoe slot. Off: the items still exist but can't be worn.")
                .define("enableHorseshoes", true);
        HORSESHOE_STATS = b.comment("Stats per horseshoe item: itemid, speedMod, jumpMod.",
                        "By default multiplies the total (MULTIPLY_TOTAL); add @0 (add), @1 (multiply base) or @2 to a number to pick.")
                .defineListAllowEmpty("horseshoeStats", List.of(
                        "horseshoes_iron, 0.3, 0.3",
                        "horseshoes_diamond, 0.2, 0.5",
                        "horseshoes_gold, 0.5, 0.2"), WHSConfig::isString);
        HORSESHOE_MOBS = b.comment("Horse types that get a horseshoe slot.")
                .defineListAllowEmpty("horseshoeMobs", List.of(
                        "minecraft:horse", "minecraft:donkey", "minecraft:mule",
                        "minecraft:skeleton_horse", "minecraft:zombie_horse", "minecraft:llama"), WHSConfig::isString);
        EXTRA_HORSESHOE_ITEMS = b.comment("Other mods' items that can go in the horseshoe slot (drawn as gold horseshoes). Riders of a horse",
                        "wearing one of these don't take fall damage (Lucky Horseshoe).")
                .defineListAllowEmpty("extraHorseshoeItems", List.of("bountifulcurios:lucky_horseshoe", "bountifulbaubles:trinket_lucky_horseshoe"), WHSConfig::isString);
        HORSESHOES_LAYER = b.comment("Draw worn horseshoes on the horse's hooves (with a glint when enchanted).")
                .define("horseshoesLayer", true);
        b.pop();

        b.push("undead_horses");
        SKELETON_TRAPS_ARMORED = b.comment("Skeleton horses from skeleton traps can wear random horse armor and horseshoes.")
                .define("armoredSkeletonTraps", true);
        EQUIP_ALLOW_TREASURE = b.comment("Enchanted gear on undead horses can have treasure enchantments.").define("equipEnchantAllowTreasure", false);
        EQUIP_ENCHANT_MAX_CHANCE = b.comment("Max chance to enchant gear on undead horses (scales with local difficulty).")
                .defineInRange("equipEnchantMaxChance", 0.5, 0.0, 1.0);
        EQUIP_ENCHANT_MAX_LVL = b.defineInRange("equipEnchantMaxLevel", 23, 1, 100);
        EQUIP_ENCHANT_MIN_LVL = b.defineInRange("equipEnchantMinLevel", 5, 1, 100);
        MAX_EQUIP_CHANCE = b.comment("Max chance to give an undead horse armor and/or horseshoes on spawn (scales with local difficulty, like mob gear).")
                .defineInRange("maxEquipChance", 0.15, 0.0, 1.0);
        BURN_ARMOR_PROTECTS = b.comment("Undead horses wearing armor don't burn.").define("burnArmorProtects", true);
        BURN_BABIES = b.comment("Baby undead horses burn too.").define("burnBabies", false);
        BURN_TYPES = b.comment("Which undead horses burn.").defineEnum("burnTypes", UndeadType.ZOMBIE);
        UNDEAD_BURN = b.comment("Undead horses burn in daylight, like zombies.").define("undeadHorsesBurnInSun", true);
        ZOMBIE_HORSES_DESPAWN = b.comment("Zombie horses that spawned carrying a zombie despawn like normal monsters.").define("zombieHorsesDespawn", true);
        ZOMBIE_RIDER_CHANCE = b.comment("Chance for a zombie spawning under open sky to spawn riding a (maybe armored) zombie horse.")
                .defineInRange("zombieRiderChance", 0.01, 0.0, 1.0);
        ZOMBIE_RIDER_ENABLED = b.define("zombieRiderEnabled", true);
        b.pop();

        b.push("horse_stats");
        DRAW_STATS = b.comment("Show the horse's jump height (^, blocks) and speed (>, blocks per second) in its inventory screen.")
                .define("drawHorseStats", true);
        DRAW_STATS_NEAT = b.comment("Show them under the horse's Neat health bar too (if Neat is installed).").define("drawHorseStatsNeat", true);
        MODIFY_STATS = b.comment("Roll spawning horses' speed, jump height and max health from spawnStats instead of vanilla's ranges.")
                .define("modifyStats", true);
        SPAWN_STATS = b.comment("type, speedBase, speedRange, jumpBase, jumpRange, maxHpBase, maxHpRange",
                        "Each stat is base + range * (r1 + r2 + r3) / 3 (bell shaped). Speed is in blocks per second (attribute x 43.17),",
                        "jump is the jump strength attribute, max health is rounded down. Types not listed keep vanilla's rolls.")
                .defineListAllowEmpty("spawnStats", List.of(
                        "minecraft:horse, 6.9, 9.71325, 0.4, 0.6, 15, 15",
                        "minecraft:donkey, 7.55475, 0, 0.5, 0, 15, 15",
                        "minecraft:mule, 7.55475, 0, 0.5, 0, 15, 15",
                        "minecraft:llama, 7.55475, 0, 0.5, 0, 15, 15",
                        "minecraft:skeleton_horse, 6.9, 9.7, 0.4, 0.6, 15, 0",
                        "minecraft:zombie_horse, 6.9, 9.7, 0.4, 0.6, 15, 0"), WHSConfig::isString);
        b.pop();

        SPEC = b.build();
    }

    private WHSConfig() {}

    private static boolean isString(Object o) {
        return o instanceof String;
    }

    // ---- parsed caches, rebuilt by reload() on first use after a config load/reload ----

    /** A stat change: value + AttributeModifier operation. */
    public record Mod(double value, AttributeModifier.Operation op) {
        /** "0.3" (default op) or "0.3@1". */
        static Mod parse(String s, AttributeModifier.Operation def) {
            String[] p = s.trim().split("@");
            return new Mod(Double.parseDouble(p[0].trim()), p.length > 1 ? AttributeModifier.Operation.fromValue(Integer.parseInt(p[1].trim())) : def);
        }
    }

    /** speed, jump modifiers per horseshoe item. */
    public record ShoeStats(Mod speed, Mod jump) {}
    /** armor, toughness per horse armor item. */
    public record ArmorStats(double armor, double toughness) {}
    /** One leatherworker trade line. */
    public record Trade(Item item, int minPrice, int maxPrice, int weight, int minEnch, int maxEnch) {}
    /** Spawn stat rolls for one horse type. */
    public record SpawnStats(double speedBase, double speedRange, double jumpBase, double jumpRange, double hpBase, double hpRange) {}

    public static final Map<Item, ShoeStats> shoeStats = new HashMap<>();
    public static final Map<Item, ArmorStats> armorStats = new HashMap<>();
    public static final Set<EntityType<?>> shoeMobs = new HashSet<>();
    public static final Set<EntityType<?>> armorMobs = new HashSet<>();
    public static final Set<Item> extraHorseshoes = new HashSet<>();
    public static final Set<Enchantment> armorBlacklist = new HashSet<>();
    public static final Set<Enchantment> shoesBlacklist = new HashSet<>();
    public static final Set<Enchantment> tramplingIncompatible = new HashSet<>();
    public static final List<Trade> trades = new ArrayList<>();
    public static final Set<Item> wellBlacklist = new HashSet<>();
    public static final Map<EntityType<?>, SpawnStats> spawnStats = new HashMap<>();
    // true once reload() ran (configs are read lazily: some checks happen before the config event on a fresh game)
    private static boolean loaded;

    /** Rebuilds the caches from the current config values. */
    public static synchronized void reload() {
        shoeStats.clear(); armorStats.clear(); shoeMobs.clear(); armorMobs.clear(); extraHorseshoes.clear();
        armorBlacklist.clear(); shoesBlacklist.clear(); tramplingIncompatible.clear(); trades.clear();
        wellBlacklist.clear(); spawnStats.clear();

        for (String line : HORSESHOE_STATS.get()) {
            String[] p = line.split(",");
            Item item = item(p[0], true);
            if (p.length != 3 || item == null) { warn("horseshoe stats", line); continue; }
            try {
                shoeStats.put(item, new ShoeStats(Mod.parse(p[1], AttributeModifier.Operation.MULTIPLY_TOTAL),
                        Mod.parse(p[2], AttributeModifier.Operation.MULTIPLY_TOTAL)));
            } catch (Exception e) { warn("horseshoe stats", line); }
        }
        for (String line : ARMOR_STATS.get()) {
            String[] p = line.split(",");
            Item item = item(p[0], false);
            if (p.length != 3 || item == null) { warn("armor stats", line); continue; }
            try {
                armorStats.put(item, new ArmorStats(Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim())));
            } catch (Exception e) { warn("armor stats", line); }
        }
        HORSESHOE_MOBS.get().forEach(s -> entity(s).ifPresent(shoeMobs::add));
        ARMOR_HORSES.get().forEach(s -> entity(s).ifPresent(armorMobs::add));
        EXTRA_HORSESHOE_ITEMS.get().forEach(s -> {
            Item i = item(s, false);
            if (i != null) extraHorseshoes.add(i);   // missing mods are just skipped
        });
        enchantments(HORSE_ARMOR_ENCHANT_BLACKLIST.get(), armorBlacklist);
        enchantments(HORSESHOES_ENCHANT_BLACKLIST.get(), shoesBlacklist);
        enchantments(TRAMPLING_INCOMPATIBLE.get(), tramplingIncompatible);
        for (String line : LEATHERWORKER_TRADES.get()) {
            String[] p = line.split(",");
            Item item = item(p[0], true);
            if (p.length < 4 || item == null) { warn("leatherworker trades", line); continue; }
            try {
                int minE = p.length >= 6 ? Integer.parseInt(p[4].trim()) : -1;
                int maxE = p.length >= 6 ? Integer.parseInt(p[5].trim()) : -1;
                trades.add(new Trade(item, Integer.parseInt(p[1].trim()), Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim()), minE, maxE));
            } catch (Exception e) { warn("leatherworker trades", line); }
        }
        WELL_ENCOUNTER_BLACKLIST.get().forEach(s -> {
            Item i = item(s, true);
            if (i != null) wellBlacklist.add(i);
        });
        for (String line : SPAWN_STATS.get()) {
            String[] p = line.split(",");
            Optional<EntityType<?>> type = p.length == 7 ? entity(p[0]) : Optional.empty();
            if (type.isEmpty()) { warn("spawn stats", line); continue; }
            try {
                double[] v = new double[6];
                for (int i = 0; i < 6; i++) v[i] = Double.parseDouble(p[i + 1].trim());
                spawnStats.put(type.get(), new SpawnStats(v[0], v[1], v[2], v[3], v[4], v[5]));
            } catch (Exception e) { warn("spawn stats", line); }
        }
        loaded = true;
    }

    /** Marks the caches stale (config loaded or reloaded); they're rebuilt on next use, after registries are filled. */
    public static void invalidate() {
        loaded = false;
    }

    /** Makes sure the caches match the current config (built lazily so item/enchantment ids resolve). */
    public static void ensureLoaded() {
        if (!loaded && SPEC.isLoaded()) reload();
    }

    public static boolean canShoe(AbstractHorse horse) {
        ensureLoaded();
        return ENABLE_HORSESHOES.get() && shoeMobs.contains(horse.getType());
    }

    public static boolean canArmor(AbstractHorse horse) {
        ensureLoaded();
        return ALL_HORSES_USE_ARMOR.get() && armorMobs.contains(horse.getType());
    }

    /** Item id; a bare name means this mod's item when modDefault, else minecraft's. */
    private static Item item(String id, boolean modDefault) {
        id = id.trim();
        if (!id.contains(":")) id = (modDefault ? WornHorseshoes.MODID : "minecraft") + ":" + id;
        ResourceLocation loc = ResourceLocation.tryParse(id);
        return loc != null && ForgeRegistries.ITEMS.containsKey(loc) ? ForgeRegistries.ITEMS.getValue(loc) : null;
    }

    private static Optional<EntityType<?>> entity(String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id.trim());
        return loc != null && ForgeRegistries.ENTITY_TYPES.containsKey(loc) ? Optional.ofNullable(ForgeRegistries.ENTITY_TYPES.getValue(loc)) : Optional.empty();
    }

    private static void enchantments(List<? extends String> ids, Set<Enchantment> into) {
        for (String s : ids) {
            ResourceLocation loc = ResourceLocation.tryParse(s.trim());
            if (loc != null && ForgeRegistries.ENCHANTMENTS.containsKey(loc)) into.add(ForgeRegistries.ENCHANTMENTS.getValue(loc));
        }
    }

    private static void warn(String what, String line) {
        WornHorseshoes.LOGGER.warn("Worn Horseshoes couldn't read {} line \"{}\"", what, line);
    }
}

package wornhorseshoes.config.folders;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HorseshoesConfig {

    @Config.Comment("Add or remove mobs that are allowed to get horseshoes. \n" +
            "Note: This won't work for modded mobs that aren't inheriting AbstractHorse in their code, and will most probably not work fully for those that do.")
    @Config.Name("Mobs that can get horseshoes")
    @Config.RequiresMcRestart
    public String[] horseshoeMobs = {
            "minecraft:horse",
            "minecraft:donkey",
            "minecraft:mule",
            "minecraft:skeleton_horse",
            "minecraft:zombie_horse",
            "minecraft:llama"
    };

    @Config.Comment("Add horseshoes items here. Pattern: itemName, armorMaterialName\n" +
            "If no armor material is provided, a default armor material is used. Horseshoes don't use armor material directly but other vanilla+modded mechanics do (example: enchanting uses material enchantability)\n" +
            "Note: vanilla armor materials are written in all caps, like LEATHER and CHAIN, modded ones can be written in various ways.\n" +
            "Location of the item texture json has to be wornhorseshoes:textures/models/item/itemName.json\n" +
            "Location of the model texture has to be wornhorseshoes:textures/entity/horseshoes/itemName.png")
    @Config.Name("Additional Horseshoes")
    @Config.RequiresMcRestart
    public String[] additionalHorseshoes = {};

    @Config.Comment("Stats per horseshoe item. Pattern: itemName, speedMod, jumpBoostMod\n" +
            "By default uses operation2 (MULT_TOTAL), use @operationNumber (0/1/2) to change")
    @Config.Name("Horseshoe Stats")
    public String[] itemStatsEntries = {
            "horseshoes_iron, 0.3, 0.3",
            "horseshoes_diamond, 0.2, 0.5",
            "horseshoes_gold, 0.5, 0.2"
    };

    public static void init() {
        for (String line : ModConfigHandler.horseshoes.itemStatsEntries) {
            String[] split = line.split(",");
            if (split.length != 3) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Horseshoes stats line {}, expected three entries", line);
                continue;
            }
            String itemName = split[0].trim();
            try {
                Modifier speedMod = new Modifier(split[1]);
                Modifier jumpMod = new Modifier(split[2]);
                itemStats.put(itemName, new Pair<>(speedMod, jumpMod));
            } catch (Exception e) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Horseshoes stats line {}, expected numbers in second and third entry", line);
            }
        }
    }
    public static void reset(){
        itemStats.clear();
        canShoe.clear();
    }

    public static final Map<String, Pair<Modifier, Modifier>> itemStats = new HashMap<>();

    public static class Modifier {
        public final double value;
        public final int operation;

        public Modifier(String entry) {
            if (entry.contains("@")) {
                String[] split = entry.split("@");
                this.value = Double.parseDouble(split[0].trim());
                this.operation = Integer.parseInt(split[1].trim());
            } else {
                this.value = Double.parseDouble(entry.trim());
                this.operation = 2;
            }
        }
    }

    private static final Map<Class<? extends AbstractHorse>, Boolean> canShoe = new HashMap<>();

    public static boolean canShoeHorse(AbstractHorse horse) {
        return canShoeHorse(horse.getClass());
    }
    public static boolean canShoeHorse(Class<? extends AbstractHorse> horseClass) {
        return canShoe.computeIfAbsent(horseClass, clazz -> {
            ResourceLocation loc = EntityList.getKey(clazz);
            return loc != null && Arrays.asList(ModConfigHandler.horseshoes.horseshoeMobs).contains(loc.toString());
        });
    }
}

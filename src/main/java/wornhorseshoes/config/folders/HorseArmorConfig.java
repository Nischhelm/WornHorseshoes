package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
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

@MixinConfig(name = WornHorseshoes.MODID)
public class HorseArmorConfig {

    @Config.Comment({
            "Stats per armor item. Pattern: itemName, armorMod, toughnessMod",
            "By default uses operation0 (ADD), use @operationNumber (0/1/2) to change",
            "Horse Armor that isn't mentioned here will use its vanilla handling instead",
            "Requires MixinToggle \"Register Horse Armor Item\""
    })
    @Config.Name("Armor Stats")
    public String[] itemStatsEntries = {
            "iron_horse_armor, 5, 0",
            "diamond_horse_armor, 11, 0",
            "golden_horse_armor, 7, 0"
    };

    @Config.Comment({
            "Add or remove additional mobs that are allowed to use horse armor.",
            "Note: This won't work for modded mobs that aren't inheriting AbstractHorse in their code, and will most probably not work fully for those that do.",
            "Note: This doesn't work for Llamas."
    })
    @Config.Name("Horses that can use Armor")
    @Config.RequiresMcRestart
    public String[] armorHorses = {
            "minecraft:donkey",
            "minecraft:mule",
            "minecraft:skeleton_horse",
            "minecraft:zombie_horse"
    };

    @Config.Comment({
            "MixinToggle. This will register horse armor as a separate class extending ItemArmor with customisable enchants and stats with tooltip display. ",
            "Set this to false if you don't use any horse armor features of this mod."
    })
    @Config.Name("Register Horse Armor Item")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.armor.registration.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean registerHorseArmorItem = true;

    @Config.Comment("MixinToggle. This allows Zombie and Skeleton Horses as well as Donkeys and Mules to use armor.")
    @Config.Name("All Horse Types use Armor")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.armor.zombieskeleton.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean allHorsesUseArmor = true;

    @Config.Comment({
            "MixinToggle. This disables the janky vanilla handling of Horse Armor rendering.",
            "Instead gives Horse Armor its own render layer, which can also render with enchanted glint.",
            "This is also needed to render Horse Armor on Zombie and Skeleton Horses."
    })
    @Config.Name("Render Custom Horse Armor Layer")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.armor.renderlayer.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean customHorseArmorLayer = true;

    @Config.Comment({
            "MixinToggle. Adds a render layer for saddles to (Zombie/Skeleton/Normal) Horses, Donkeys and Mules.",
            "This allows enchanted saddles to render with enchanted glint."
    })
    @Config.Name("Render Saddle Layer")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.saddle.renderlayer.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean saddleLayer = true;

    private static final Map<Class<? extends AbstractHorse>, Boolean> canArmor = new HashMap<>();

    public static boolean canUseArmor(AbstractHorse horse) {
        return canUseArmor(horse.getClass());
    }
    public static boolean canUseArmor(Class<? extends AbstractHorse> horseClass) {
        return canArmor.computeIfAbsent(horseClass, clazz -> {
            ResourceLocation loc = EntityList.getKey(clazz);
            return loc != null && Arrays.asList(ModConfigHandler.horsearmor.armorHorses).contains(loc.toString());
        });
    }

    public static void init() {
        for (String line : ModConfigHandler.horsearmor.itemStatsEntries) {
            String[] split = line.split(",");
            if (split.length != 3) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Horse Armor stats line {}, expected three entries", line);
                continue;
            }
            String itemName = split[0].trim();
            if(!itemName.contains(":")) itemName = "minecraft:" + itemName;
            try {
                HorseshoesConfig.Modifier armorMod = new HorseshoesConfig.Modifier(split[1], 0);
                HorseshoesConfig.Modifier toughnessMod = new HorseshoesConfig.Modifier(split[2], 0);
                itemStats.put(itemName, new Pair<>(armorMod, toughnessMod));
            } catch (Exception e) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Horse Armor stats line {}, expected numbers in second and third entry", line);
            }
        }
    }
    public static void reset(){
        itemStats.clear();
        canArmor.clear();
    }

    public static final Map<String, Pair<HorseshoesConfig.Modifier, HorseshoesConfig.Modifier>> itemStats = new HashMap<>();
}

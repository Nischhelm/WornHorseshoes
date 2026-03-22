package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;

@MixinConfig(name = WornHorseshoes.MODID)
public class HorseArmorConfig {

    //TODO: stats

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
    public boolean zombieSkeletonHorseArmor = true;

    @Config.Comment("MixinToggle. This allows Skeleton Horses created from Skeleton Horse Traps to wear horse armor and horseshoes randomly.")
    @Config.Name("Armored Skeleton Horse Traps")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.encounter.armoredskeletonhorsetraps.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean skeletonHorseTraps = true;

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
}

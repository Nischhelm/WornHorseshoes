package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;

@MixinConfig(name = WornHorseshoes.MODID)
public class UndeadHorsesConfig {
    @Config.Comment("Chance for a zombie to spawn riding on a zombie horse.")
    @Config.Name("Zombie Rider Chance")
    public float zombieRiderChance = 0.001F;

    @Config.Comment("MixinToggle. This enables zombies to randomly spawn riding on potentially armored zombie horses if they spawn on the surface.")
    @Config.Name("Zombie Rider Enabled")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.encounter.zombierider.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean zombieRiderEnabled = true;

    @Config.Comment("MixinToggle. This enables zombie horses that spawn ridden by zombies to automatically despawn.")
    @Config.Name("Zombie Horses Despawn")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.undead.despawn.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean zombieHorsesDespawn = true;

    @Config.Comment("MixinToggle. This enables undead horses to burn in sunlight unless they wear armor.")
    @Config.Name("Undead Horses Burn in Sun")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.undead.burninsun.json", defaultValue = false)
    @Config.RequiresMcRestart
    public boolean undeadHorsesBurn = false;

    @Config.Comment("Baby zombies don't burn in the sun. Set this to true to let undead baby horses burn in the sun anyway.")
    @Config.Name("Undead Horses Burn - Babies")
    public boolean childrenBurn = false;

    @Config.Comment("Disabling this will make undead horses burn in the sun no matter if they wear armor or not.")
    @Config.Name("Undead Horses Burn - Armor Protects")
    public boolean armorProtects = true;

    @Config.Comment("This allows to select whether only one of the two undead horse types should burn in sunlight. To fully disable this feature, use the MixinToggle \"Undead Horses Burn in Sun\"")
    @Config.Name("Undead Horses Burn - Types")
    public EnumUndead burnTypes = EnumUndead.BOTH;
    public enum EnumUndead { ZOMBIE, SKELETON, BOTH}

    @Config.Comment("MixinToggle. This allows Skeleton Horses created from Skeleton Horse Traps to wear horse armor and horseshoes randomly.")
    @Config.Name("Armored Skeleton Horse Traps")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.encounter.armoredskeletonhorsetraps.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean skeletonHorseTraps = true;
}

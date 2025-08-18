package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;

@MixinConfig(name = WornHorseshoes.MODID)
public class HorseArmorConfig {

    //TODO: stats

    @Config.Comment("This will register horse armor as a separate class extending ItemArmor with customisable enchants and stats with tooltip display. Set this to false if you don't use any horse armor features of this mod.")
    @Config.Name("Register Horse Armor Item")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.horsearmorregistration.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean registerHorseArmorItem = true;
}

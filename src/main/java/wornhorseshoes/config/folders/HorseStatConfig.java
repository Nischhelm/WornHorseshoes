package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;

import java.util.HashMap;
import java.util.Map;

@MixinConfig(name = WornHorseshoes.MODID)
public class HorseStatConfig {
    @Config.Comment("MixinToggle. This makes the horses speed+jump height draw in horse GUI.")
    @Config.Name("Draw Horse Stats")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.horsestats.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean drawHorseStats = true;

    @Config.Comment("MixinToggle. This draws the horses speed+jump height in the horses Neat HP bar.")
    @Config.Name("Draw Horse Stats (Neat)")
    @MixinConfig.MixinToggle(lateMixin = "mixins.wornhorseshoes.neat.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "neat", desired = true, warnIngame = false, reason = "Optional Compat with Neat mod")
    @Config.RequiresMcRestart
    public boolean drawHorseStatsNeat = true;

    @Config.Comment({
            "MixinToggle. Allows to modify both spawn and breeding stat (speed+jump height+maxhp) variation using the given configs here",
            "For starting attributes, vanilla makes them either fixed to always the same value (base),",
            " or picks randomly in a range with a bell shaped distribution (base + range * (rand+rand+rand)/3)",
            "Note: for MaxHP this technically modifies the vanilla behavior minimally. In vanilla it uses smth equivalent to a triangular distribution: base + range * (rand+rand)/2, rounding to nearest integer"
    })
    @Config.Name("Modify Stats")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.horsestatmodify.json", defaultValue = false)
    public boolean modifyStats = false;

    @Config.Name("Spawn Speed Base")
    public Map<String, Double> startSpeedBase = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 0.2*43.17);
        put("minecraft:skeleton_horse", 0.2*43.17);
        put("minecraft:donkey", 0.175*43.17);
        put("minecraft:mule", 0.175*43.17);
        put("minecraft:horse", 0.1125*43.17);
        put("minecraft:llama", 0.175*43.17);
    }};

    @Config.Name("Spawn Speed Range")
    public Map<String, Double> startSpeedRange = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 0.);
        put("minecraft:skeleton_horse", 0.);
        put("minecraft:donkey", 0.);
        put("minecraft:mule", 0.);
        put("minecraft:horse", 0.225*43.17);
        put("minecraft:llama", 0.);
    }};

    @Config.Name("Spawn Jump Height Base")
    public Map<String, Double> startJumpHeightBase = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 0.4);
        put("minecraft:skeleton_horse", 0.4);
        put("minecraft:donkey", 0.5);
        put("minecraft:mule", 0.5);
        put("minecraft:horse", 0.4);
        put("minecraft:llama", 0.5);
    }};

    @Config.Name("Spawn Jump Height Range")
    public Map<String, Double> startJumpHeightRange = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 0.6);
        put("minecraft:skeleton_horse", 0.6);
        put("minecraft:donkey", 0.);
        put("minecraft:mule", 0.);
        put("minecraft:horse", 0.6);
        put("minecraft:llama", 0.);
    }};

    @Config.Name("Spawn Max HP Base")
    public Map<String, Double> startMaxHPBase = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 15.);
        put("minecraft:skeleton_horse", 15.);
        put("minecraft:donkey", 15.);
        put("minecraft:mule", 15.);
        put("minecraft:horse", 15.);
        put("minecraft:llama", 15.);
    }};

    @Config.Name("Spawn Max HP Range")
    public Map<String, Double> startMaxHPRange = new HashMap<String, Double>(){{
        put("minecraft:zombie_horse", 0.);
        put("minecraft:skeleton_horse", 0.);
        put("minecraft:donkey", 15.);
        put("minecraft:mule", 15.);
        put("minecraft:horse", 15.);
        put("minecraft:llama", 15.);
    }};

    //TODO: same with breeding stats
}

package wornhorseshoes.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;

import java.util.HashSet;
import java.util.Set;

@MixinConfig(name = WornHorseshoes.MODID)
public class EnchantmentConfig {
    @Config.Comment("MixinToggle. If horses wear horseshoes with any depth strider lvl, riders won't get dismounted under water.")
    @Config.Name("Depth Strider no dismount")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.enchants.depthstriderdismount.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean depthStriderNoDismount = true;

    @Config.Comment("Set to false to have horse armor never be enchantable")
    @Config.Name("Horse Armor Enchantable")
    public boolean horseArmorEnchantable = true;

    @Config.Comment("By default, horse armor can get any helmet and chestplate enchants. Blacklist enchants here that horse armor should not be able to get.")
    @Config.Name("Horse Armor Enchant Blacklist")
    public String[] horseArmorBlacklist = {};

    @Config.Comment("Set to false to have horseshoes never be enchantable")
    @Config.Name("Horseshoes Enchantable")
    public boolean horseshoesEnchantable = true;

    @Config.Comment({
            "MixinToggle. Fires LivingJumpEvent when horses jump to be compatible with various jumping enchantments and effects. ",
            "If present, includes a fix for PotionCore jump height attribute impeding the horse jump height for no reason"
    })
    @Config.Name("Fire Living Jump")
    @MixinConfig.MixinToggle(
            earlyMixin = "mixins.wornhorseshoes.vanilla.enchants.livingjump.json",
            lateMixin = "mixins.wornhorseshoes.potioncore.json",
            defaultValue = true
    )
    @MixinConfig.CompatHandling(modid = "potioncore", warnIngame = false, desired = true, disableMixin = false) //TODO: test if this crashes without pc
    @Config.RequiresMcRestart
    public boolean fireLivingJump = true;

    @Config.Comment({
            "MixinToggle. Disabling this will disable most enchantments from working on horses.",
            "Most enchantments check for their own presence by checking the entities equipment slots.",
            "A horse by default doesn't have any equipment slots, so checking its equipment for enchantments will always fail.",
            "This mixin lists worn horse armor as being in HEAD and CHEST slot while listing worn horseshoes as being in FEET slot.",
            "This also allows protection enchants to work.",
            "Since horse armor is worn in both HEAD and CHEST, protection on armor will be twice as effective.",
            "On the other hand, horse legs will never be protected."
    })
    @Config.Name("Enable Equipment Slots")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.enchants.equipmentslots.json", defaultValue = true)
    @Config.RequiresMcRestart
    public boolean inventorySlots = true;

    @Config.Comment("By default, horseshoes can get any boot enchants. Blacklist enchants here that horseshoes should not be able to get.")
    @Config.Name("Horseshoes Enchant Blacklist")
    public String[] horseshoesBlacklist = {};

    @Config.Name("Trampling")
    public TramplingEnchantmentConfig trampling = new TramplingEnchantmentConfig();

    @MixinConfig(name = WornHorseshoes.MODID)
    public static class TramplingEnchantmentConfig {
        @Config.Name("Enabled")
        @MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.enchants.trampling.json", defaultValue = true)
        @Config.RequiresMcRestart
        public boolean enabled = true;

        @Config.Name("Max Lvl")
        @Config.RangeInt(min = 1, max = 10)
        public int maxLvl = 3;

        @Config.Name("Rarity")
        public Enchantment.Rarity rarity = Enchantment.Rarity.VERY_RARE;

        @Config.Comment("In order: min, lvlspan, range\n" +
                "Enchantability is a range from \n" +
                " min + (lvl-1) * lvlspan \n" +
                " to that value plus range\n" +
                "Defines on what shown lvls of the enchantment table an enchant with a specific lvl can be gotten")
        @Config.Name("Enchantability")
        public int[] enchantability = {5, 20, 50};

        @Config.Name("Incompatible enchantments")
        public String[] incompatible = {};

        @Config.Comment("Will be 0.5x this amount when horse is rearing.")
        @Config.Name("Knockback per lvl")
        public float knockbackAmount = 1.0F;

        @Config.Comment("Will be 1.5x this amount when horse is rearing.")
        @Config.Name("Damage per lvl")
        public float damage = 1.0F;

        public static final Set<Enchantment> incompatibleEnchants = new HashSet<>();
    }

    public static final Set<Enchantment> horseArmorBlacklistSet = new HashSet<>();
    public static final Set<Enchantment> horseshoesBlacklistSet = new HashSet<>();

    public static void init(){
        for(String s : ModConfigHandler.enchants.horseArmorBlacklist){
            Enchantment ench = Enchantment.getEnchantmentByLocation(s);
            if(ench != null) horseArmorBlacklistSet.add(ench);
        }

        for(String s : ModConfigHandler.enchants.horseshoesBlacklist){
            Enchantment ench = Enchantment.getEnchantmentByLocation(s);
            if(ench != null) horseshoesBlacklistSet.add(ench);
        }

        for(String s : ModConfigHandler.enchants.trampling.incompatible){
            Enchantment incomp = Enchantment.getEnchantmentByLocation(s);
            if(incomp != null) TramplingEnchantmentConfig.incompatibleEnchants.add(incomp);
        }
    }
    public static void reset(){
        horseArmorBlacklistSet.clear();
        horseshoesBlacklistSet.clear();
        TramplingEnchantmentConfig.incompatibleEnchants.clear();
    }
}

package wornhorseshoes.config;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.folders.*;

@Config(modid = WornHorseshoes.MODID)
@MixinConfig(name = WornHorseshoes.MODID)
public class ModConfigHandler {
	@Config.Name("Enchantments")
	public static EnchantmentConfig enchants = new EnchantmentConfig();

	@Config.Name("Acquisition")
	public static EncounterConfig encounters = new EncounterConfig();

	@Config.Name("Horse Armor")
	public static HorseArmorConfig horsearmor = new HorseArmorConfig();

	@Config.Name("Horseshoes")
	public static HorseshoesConfig horseshoes = new HorseshoesConfig();

	@Config.Name("Undead Horses")
	public static UndeadHorsesConfig undead = new UndeadHorsesConfig();

	@Config.Comment("MixinToggle. This makes the horses speed+jump height draw in horse GUI.")
	@Config.Name("Draw Horse Stats")
	@MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.horsestats.json", defaultValue = true)
	@Config.RequiresMcRestart
	public static boolean drawHorseStats = true;

	@Config.Comment("MixinToggle. This makes horses stop rearing normally when players dismount. Without this fix they will keep rearing until they are mounted again.")
	@Config.Name("Fix Rearing")
	@MixinConfig.MixinToggle(earlyMixin = "mixins.wornhorseshoes.vanilla.rearingfix.json", defaultValue = true)
	@Config.RequiresMcRestart
	public static boolean fixRearing = true;


	@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(WornHorseshoes.MODID)) {
				ConfigManager.sync(WornHorseshoes.MODID, Config.Type.INSTANCE);
				reset();
			}
		}
	}

	public static void preInit(){
		HorseshoesConfig.init();
	}
	public static void init(){
		EnchantmentConfig.init();
		EncounterConfig.init();
		HorseArmorConfig.init();
	}
	public static void reset() {
		HorseshoesConfig.reset();
		EnchantmentConfig.reset();
		EncounterConfig.reset();
		HorseArmorConfig.reset();

		preInit();
		init();
	}
}
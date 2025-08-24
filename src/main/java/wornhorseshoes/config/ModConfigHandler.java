package wornhorseshoes.config;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.folders.AcquisitionConfig;
import wornhorseshoes.config.folders.EnchantmentConfig;
import wornhorseshoes.config.folders.HorseArmorConfig;
import wornhorseshoes.config.folders.HorseshoesConfig;

@Config(modid = WornHorseshoes.MODID)
@MixinConfig(name = WornHorseshoes.MODID)
public class ModConfigHandler {
	@Config.Name("Enchantments")
	public static EnchantmentConfig enchants = new EnchantmentConfig();

	@Config.Name("Acquisition")
	public static AcquisitionConfig acquisition = new AcquisitionConfig();

	@Config.Name("Horse Armor")
	public static HorseArmorConfig horsearmor = new HorseArmorConfig();

	@Config.Name("Horseshoes")
	public static HorseshoesConfig horseshoes = new HorseshoesConfig();

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
		AcquisitionConfig.init();
	}
	public static void reset() {
		HorseshoesConfig.reset();
		EnchantmentConfig.reset();
		AcquisitionConfig.reset();

		preInit();
		init();
	}
}
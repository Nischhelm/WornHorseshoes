package wornhorseshoes.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;

@Config(modid = WornHorseshoes.MODID)
public class ModConfigHandler {
	@Config.Comment("If horses wear horseshoes with any depth strider lvl, riders won't get dismounted under water.")
	@Config.Name("Depth Strider no dismount")
	public static boolean depthStriderNoDismount = true;

	@Config.Comment("Stats per material for horseshoes. Pattern: materialName, speedMod, jumpBoostMod\n" +
			"By default uses operation2 (MULT_TOTAL), use @operationNumber (0/1/2) to change")
	@Config.Name("Horseshoe Material stats")
	public static String[] materialStats = {
			"iron, 0.3, 0.3",
			"diamond, 0.2, 0.5",
			"gold, 0.5, 0.2"
	};

	@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(WornHorseshoes.MODID)) {
				ConfigManager.sync(WornHorseshoes.MODID, Config.Type.INSTANCE);

				ModConfigProvider.reset();
			}
		}
	}
}
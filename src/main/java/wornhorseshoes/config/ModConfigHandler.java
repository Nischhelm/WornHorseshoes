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
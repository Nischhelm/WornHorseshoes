package wornhorseshoes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wornhorseshoes.config.ModConfigProvider;

@Mod(modid = WornHorseshoes.MODID, version = WornHorseshoes.VERSION, name = WornHorseshoes.NAME, dependencies = "required-after:fermiumbooter")
public class WornHorseshoes {
    public static final String MODID = "wornhorseshoes";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "WornHorseshoes";
    public static final Logger LOGGER = LogManager.getLogger();

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfigProvider.init();
    }
}
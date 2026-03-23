package wornhorseshoes;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wornhorseshoes.compat.BountifulBaublesCompat;
import wornhorseshoes.compat.CompatUtil;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.handlers.HorseshoeAcquisition;
import wornhorseshoes.proxy.IProxy;

@Mod(
        modid = WornHorseshoes.MODID,
        version = WornHorseshoes.VERSION,
        name = WornHorseshoes.NAME,
        dependencies = "required-after:fermiumbooter@[1.3.0,);"
)
public class WornHorseshoes {
    public static final String MODID = "wornhorseshoes";
    public static final String VERSION = "1.1.0";
    public static final String NAME = "WornHorseshoes";
    public static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(serverSide = "wornhorseshoes.proxy.ServerProxy", clientSide = "wornhorseshoes.proxy.ClientProxy")
    public static IProxy proxy;

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfigHandler.preInit();
        if(CompatUtil.bountifulbaubles.isLoaded()) MinecraftForge.EVENT_BUS.register(BountifulBaublesCompat.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModConfigHandler.init();
        HorseshoeAcquisition.addHorseshoeTrade();
    }
}
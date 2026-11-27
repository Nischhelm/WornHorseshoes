package wornhorseshoes;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.handlers.HorseEvents;
import wornhorseshoes.handlers.HorseshoeDispenseBehavior;
import wornhorseshoes.handlers.LeatherworkerTrades;
import wornhorseshoes.registry.ModRegistry;

/**
 * Worn Horseshoes: enchantable horseshoes, horse armor for more horse types and other horse improvements.
 * Forge 1.20.1 port of Nischhelm's 1.12.2 mod (MIT).
 */
@Mod(WornHorseshoes.MODID)
public class WornHorseshoes {
    public static final String MODID = "wornhorseshoes";
    public static final Logger LOGGER = LoggerFactory.getLogger("WornHorseshoes");

    public WornHorseshoes() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WHSConfig.SPEC);
        ModRegistry.register(modBus);
        modBus.addListener(this::onConfig);
        modBus.addListener(this::onConfigReload);
        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onCreativeTabs);
        MinecraftForge.EVENT_BUS.register(HorseEvents.class);
        MinecraftForge.EVENT_BUS.register(LeatherworkerTrades.class);
    }

    private void onConfig(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == WHSConfig.SPEC) WHSConfig.invalidate();   // caches rebuild on next use
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == WHSConfig.SPEC) WHSConfig.invalidate();
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // dispensers put horseshoes on the horse in front of them (vanilla already does this for horse armor)
        event.enqueueWork(() -> ModRegistry.HORSESHOES.forEach(item ->
                DispenserBlock.registerBehavior(item.get(), new HorseshoeDispenseBehavior())));
    }

    private void onCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) ModRegistry.HORSESHOES.forEach(item -> event.accept(item.get()));
    }
}

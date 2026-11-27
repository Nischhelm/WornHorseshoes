package wornhorseshoes.client;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wornhorseshoes.WornHorseshoes;

/** Client mod-bus setup: registers the horseshoe model layers and adds the gear layers to every horse-like renderer. */
@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void onLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HorseshoeModels.HORSE, HorseshoeModels::horse);
        event.registerLayerDefinition(HorseshoeModels.LLAMA, HorseshoeModels::llama);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        addHorse(event, EntityType.HORSE);
        addHorse(event, EntityType.DONKEY);
        addHorse(event, EntityType.MULE);
        addHorse(event, EntityType.SKELETON_HORSE);
        addHorse(event, EntityType.ZOMBIE_HORSE);
        addLlama(event, EntityType.LLAMA);
        addLlama(event, EntityType.TRADER_LLAMA);
    }

    // The renderers' generic types differ per horse type (Horse/HorseModel<Horse>, ChestedHorseModel<Donkey>...), but all
    // of their models are HorseModels, which is all the layer needs; the unchecked cast just erases that difference.
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addHorse(EntityRenderersEvent.AddLayers event, EntityType<? extends AbstractHorse> type) {
        LivingEntityRenderer renderer = event.getRenderer(type);
        if (renderer != null && renderer.getModel() instanceof HorseModel)
            renderer.addLayer(new HorseGearLayer<>(renderer, event.getEntityModels()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLlama(EntityRenderersEvent.AddLayers event, EntityType<? extends Llama> type) {
        LivingEntityRenderer renderer = event.getRenderer(type);
        if (renderer != null && renderer.getModel() instanceof LlamaModel)
            renderer.addLayer(new LlamaHorseshoeLayer(renderer, event.getEntityModels()));
    }
}

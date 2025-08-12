package wornhorseshoes.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.enchantment.EnchantmentTrampling;
import wornhorseshoes.item.ItemHorseshoes;

@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID)
public class RegistrationHandler {
    public static ItemHorseshoes DIAMOND_HORSESHOE = new ItemHorseshoes(ItemArmor.ArmorMaterial.DIAMOND);
    public static ItemHorseshoes GOLD_HORSESHOE = new ItemHorseshoes(ItemArmor.ArmorMaterial.GOLD);
    public static ItemHorseshoes IRON_HORSESHOE = new ItemHorseshoes(ItemArmor.ArmorMaterial.IRON);

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                DIAMOND_HORSESHOE,
                GOLD_HORSESHOE,
                IRON_HORSESHOE
        );
    }

    @SubscribeEvent
    public static void onEnchantRegistration(RegistryEvent.Register<Enchantment> event){
        event.getRegistry().register(EnchantmentTrampling.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        DIAMOND_HORSESHOE.registerModel();
        GOLD_HORSESHOE.registerModel();
        IRON_HORSESHOE.registerModel();
    }

    @SubscribeEvent
    public static void onInteractEntity(LivingEvent.LivingUpdateEvent event){
        if(!(event.getEntity() instanceof EntityHorse)) return;
        EntityHorse horse = (EntityHorse) event.getEntity();

        if(event.getEntity().world.getTotalWorldTime() % 20 != 0) return;

        if(!horse.isRearing()) return;

        event.getEntity().world.playerEntities.forEach(p ->
                p.sendMessage(new TextComponentString("Horse with UUID " + horse.getUniqueID() + " and id " + horse.getEntityId() + " side " + (event.getEntity().world.isRemote ? "client" : "server") + " isRearing: "+ horse.isRearing())));
    }
}
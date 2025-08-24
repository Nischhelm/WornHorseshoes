package wornhorseshoes.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.enchantment.EnchantmentTrampling;
import wornhorseshoes.item.ItemHorseshoes;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID)
public class RegistrationHandler {
    public static Set<ItemHorseshoes> registeredHorseshoes = new HashSet<>();
    public static ItemHorseshoes DIAMOND_HORSESHOE = new ItemHorseshoes("horseshoes_diamond", ItemArmor.ArmorMaterial.DIAMOND);
    public static ItemHorseshoes GOLD_HORSESHOE = new ItemHorseshoes("horseshoes_gold", ItemArmor.ArmorMaterial.GOLD);
    public static ItemHorseshoes IRON_HORSESHOE = new ItemHorseshoes("horseshoes_iron", ItemArmor.ArmorMaterial.IRON);

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event){
        registeredHorseshoes.add(DIAMOND_HORSESHOE);
        registeredHorseshoes.add(GOLD_HORSESHOE);
        registeredHorseshoes.add(IRON_HORSESHOE);

        for(String entry : ModConfigHandler.horseshoes.additionalHorseshoes){
            String[] split = entry.split(",");
            String itemName = split[0].trim();
            if(split.length == 1)
                registeredHorseshoes.add(new ItemHorseshoes(entry.trim()));
            else
                registeredHorseshoes.add(new ItemHorseshoes(itemName, ItemArmor.ArmorMaterial.valueOf(split[1].trim())));
        }

        registeredHorseshoes.forEach(item -> event.getRegistry().register(item));
    }

    @SubscribeEvent
    public static void onEnchantRegistration(RegistryEvent.Register<Enchantment> event){
        if(ModConfigHandler.enchants.trampling.enabled)
            event.getRegistry().register(EnchantmentTrampling.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) {
        registeredHorseshoes.forEach(ItemHorseshoes::registerModel);
    }

    //TODO: finish up the rearing fix
//    @SubscribeEvent
//    public static void onInteractEntity(LivingEvent.LivingUpdateEvent event){
//        if(!(event.getEntity() instanceof EntityHorse)) return;
//        EntityHorse horse = (EntityHorse) event.getEntity();
//
//        if(event.getEntity().world.getTotalWorldTime() % 20 != 0) return;
//
//        if(!horse.isRearing()) return;
//
//        event.getEntity().world.playerEntities.forEach(p ->
//                p.sendMessage(new TextComponentString("Horse with UUID " + horse.getUniqueID() + " and id " + horse.getEntityId() + " side " + (event.getEntity().world.isRemote ? "client" : "server") + " isRearing: "+ horse.isRearing())));
//    }
}
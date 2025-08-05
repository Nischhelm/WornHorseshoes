package wornhorseshoes.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
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
}
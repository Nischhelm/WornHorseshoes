package wornhorseshoes.handlers;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.item.ItemHorseArmor;

@Mod.EventBusSubscriber
public class TooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event){
        if(!ModConfigHandler.horsearmor.registerHorseArmorItem) return;
        if(!(event.getItemStack().getItem() instanceof ItemHorseArmor)) return;
        ItemHorseArmor horseArmor = (ItemHorseArmor) event.getItemStack().getItem();

        HorseArmorType type = horseArmor.getHorseArmorType(event.getItemStack());
        int armorAdd = type.getProtection();

        if(armorAdd == 0) return;

        String lineSlot = I18n.translateToLocal("item.modifiers." + EntityEquipmentSlot.CHEST.getName());
        int indexOfLineSlot = event.getToolTip().indexOf(lineSlot);

        String lineToAdd = TextFormatting.BLUE + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + 0, ItemStack.DECIMALFORMAT.format(armorAdd), I18n.translateToLocal("attribute.name.generic.armor"));

        if(indexOfLineSlot == -1) {
            //Should never happen, just fallback
            event.getToolTip().add(lineSlot);
            event.getToolTip().add(lineToAdd);
        } else
            event.getToolTip().add(indexOfLineSlot + 1, lineToAdd);
    }
}

package wornhorseshoes.mixin.vanilla.horsearmor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import wornhorseshoes.item.ItemHorseArmor;

@Mixin(Item.class)
public abstract class HorseArmorRegistration {
    @Shadow private static void registerItem(int id, String textualID, Item itemIn){};

    @Redirect(
            method = "registerItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;registerItem(ILjava/lang/String;Lnet/minecraft/item/Item;)V"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "intValue=417"), to = @At(value = "CONSTANT", args = "intValue=420"))
    )
    private static void whs_overrideHorseArmorRegistration(int id, String textualID, Item itemIn){
        switch (id){
            case 417: registerItem(id, textualID, new ItemHorseArmor(ItemArmor.ArmorMaterial.IRON)); break;
            case 418: registerItem(id, textualID, new ItemHorseArmor(ItemArmor.ArmorMaterial.GOLD)); break;
            case 419: registerItem(id, textualID, new ItemHorseArmor(ItemArmor.ArmorMaterial.DIAMOND)); break;
        }
    }
}

package wornhorseshoes.mixin.vanilla.horsearmor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import wornhorseshoes.item.ItemHorseArmor;

@Mixin(Item.class)
public abstract class HorseArmorRegistration {
    @WrapOperation(
            method = "registerItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;registerItem(ILjava/lang/String;Lnet/minecraft/item/Item;)V"),
            slice = @Slice(from = @At(value = "CONSTANT", args = "intValue=417"), to = @At(value = "CONSTANT", args = "intValue=420"))
    )
    private static void whs_overrideHorseArmorRegistration(int id, String textualID, Item itemIn, Operation<Void> original){
        switch (id) {
            case 417: original.call(id, textualID, new ItemHorseArmor(textualID, "horsearmormetal", ItemArmor.ArmorMaterial.IRON)); break;
            case 418: original.call(id, textualID, new ItemHorseArmor(textualID, "horsearmorgold", ItemArmor.ArmorMaterial.GOLD)); break;
            case 419: original.call(id, textualID, new ItemHorseArmor(textualID, "horsearmordiamond", ItemArmor.ArmorMaterial.DIAMOND)); break;
        }
    }
}

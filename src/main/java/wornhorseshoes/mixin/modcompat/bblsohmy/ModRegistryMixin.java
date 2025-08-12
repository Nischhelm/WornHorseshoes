package wornhorseshoes.mixin.modcompat.bblsohmy;

import bblsom.handlers.ModRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import wornhorseshoes.item.ItemHorseArmor;

import java.util.List;

@Mixin(ModRegistry.class)
public abstract class ModRegistryMixin {
    @WrapOperation(
            method = "init",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", target = "Lbblsom/handlers/ForgeConfigHandler$ServerConfig;customHorseArmors:[Ljava/lang/String;")),
            remap = false
    )
    private static boolean wornhorseshoes_redirectHorseArmorConstruction(
            List<Item> ITEMS_HORSEARMOR, Object e, Operation<Boolean> original,
            @Local(name = "name") String name, @Local(name = "horseArmorType")HorseArmorType horseArmorType
    ) {
        return ITEMS_HORSEARMOR.add(new ItemHorseArmor(name, "bblsom", horseArmorType));
    }
}

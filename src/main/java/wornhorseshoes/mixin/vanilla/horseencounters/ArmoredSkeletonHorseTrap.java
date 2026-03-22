package wornhorseshoes.mixin.vanilla.horseencounters;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.handlers.RegistrationHandler;
import wornhorseshoes.mixin.vanilla.horseshoeslot.AbstractHorseAccessor;
import wornhorseshoes.util.IHorseStackGetter;

@Mixin(EntityAISkeletonRiders.class)
public abstract class ArmoredSkeletonHorseTrap {
    @Shadow @Final private EntitySkeletonHorse horse;

    @Inject(
            method = "createHorse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void whs_addArmor_additionalHorses(DifficultyInstance difficulty, CallbackInfoReturnable<AbstractHorse> cir, @Local EntitySkeletonHorse horse) {
        whs$equipSkeletonHorse(horse);
    }

    @Inject(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addWeatherEffect(Lnet/minecraft/entity/Entity;)Z", ordinal = 0)
    )
    private void whs_addArmor_thisHorse(CallbackInfo ci) {
        whs$equipSkeletonHorse(this.horse);
    }

    @Unique
    private static void whs$equipSkeletonHorse(EntitySkeletonHorse horse) {
        //TODO: config chance & enchant

        // TODO : only add armor if skellie horses are allowed to wear armor
        ItemStack armor = new ItemStack(Items.GOLDEN_HORSE_ARMOR);
        armor = EnchantmentHelper.addRandomEnchantment(horse.getRNG(), armor, 15, false);
        IHorseStackGetter.setArmorStack(horse, armor);
        ((AbstractHorseAccessor) horse).getHorseChest().setInventorySlotContents(1, armor);

        ItemStack shoes = new ItemStack(RegistrationHandler.GOLD_HORSESHOE);
        shoes = EnchantmentHelper.addRandomEnchantment(horse.getRNG(), shoes, 15, false);
        horse.getDataManager().set(IHorseStackGetter.HORSESHOE_STACK, shoes);
        ((AbstractHorseAccessor) horse).getHorseChest().setInventorySlotContents(2, shoes);
    }
}

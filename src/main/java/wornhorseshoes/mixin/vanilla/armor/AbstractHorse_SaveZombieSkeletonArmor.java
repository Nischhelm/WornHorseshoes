package wornhorseshoes.mixin.vanilla.armor;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorse_SaveZombieSkeletonArmor {

    @Shadow protected ContainerHorseChest horseChest;
    @Shadow public abstract boolean isArmor(ItemStack stack);

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void writeEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(!((AbstractHorse) (Object) this instanceof EntityHorse))
            if (!this.horseChest.getStackInSlot(1).isEmpty())
                compound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
    }

    @Inject(method = "readEntityFromNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;updateHorseSlots()V"))
    public void readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(!((AbstractHorse) (Object) this instanceof EntityHorse))
            if (compound.hasKey("ArmorItem", 10)) {
                ItemStack itemstack = new ItemStack(compound.getCompoundTag("ArmorItem"));

                if (!itemstack.isEmpty() && isArmor(itemstack))
                    this.horseChest.setInventorySlotContents(1, itemstack);
            }
    }
}

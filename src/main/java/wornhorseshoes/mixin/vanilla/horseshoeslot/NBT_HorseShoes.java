package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class NBT_HorseShoes extends EntityLivingBase {
    public NBT_HorseShoes(World worldIn) {
        super(worldIn);
    }

    @Shadow protected ContainerHorseChest horseChest;

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void whs_writeHorseshoeToNBT(NBTTagCompound compound, CallbackInfo ci){
        if(this.horseChest.getStackInSlot(2).isEmpty()) return;
        compound.setTag("WHS_Horseshoes", this.horseChest.getStackInSlot(2).writeToNBT(new NBTTagCompound()));
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At("TAIL")
    )
    private void whs_readHorseshoeFromNBT(NBTTagCompound compound, CallbackInfo ci){
        if(!compound.hasKey("WHS_Horseshoes")) return;

        ItemStack itemstack = new ItemStack(compound.getCompoundTag("WHS_Horseshoes"));
        this.horseChest.setInventorySlotContents(2, itemstack);
    }
}

package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends EntityLivingBase {
    public AbstractHorseMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow protected ContainerHorseChest horseChest;

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void whs_writeHorseshoeToNBT(NBTTagCompound compound, CallbackInfo ci){
        if((AbstractHorse) (Object) this instanceof AbstractChestHorse) return;
        if(this.horseChest.getStackInSlot(this.horseChest.getSizeInventory()-1).isEmpty()) return;
        compound.setTag("WHS_Horseshoes", this.horseChest.getStackInSlot(this.horseChest.getSizeInventory()-1).writeToNBT(new NBTTagCompound()));
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At("TAIL")
    )
    private void whs_readHorseshoeFromNBT(NBTTagCompound compound, CallbackInfo ci){
        if((AbstractHorse) (Object) this instanceof AbstractChestHorse) return; //TODO: test
        if(!compound.hasKey("WHS_Horseshoes")) return;

        ItemStack itemstack = new ItemStack(compound.getCompoundTag("WHS_Horseshoes"));
        this.horseChest.setInventorySlotContents(this.horseChest.getSizeInventory() - 1, itemstack);
    }

    @Override
    @Nonnull
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn){
        switch (slotIn) {
            case HEAD: case CHEST: //protects both
                return this.horseChest.getStackInSlot(1);
            case FEET:
                return this.horseChest.getStackInSlot(2);
            default:
                return ItemStack.EMPTY;
        }
    }
}

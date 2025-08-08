package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.item.ItemHorseshoes;

import javax.annotation.Nonnull;

@Mixin(ContainerHorseInventory.class)
public abstract class ContainerHorseInventoryMixin extends Container {
    @Shadow @Final private IInventory horseInventory;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/ContainerHorseInventory;addSlotToContainer(Lnet/minecraft/inventory/Slot;)Lnet/minecraft/inventory/Slot;",
                    ordinal = 1,
                    shift = At.Shift.AFTER,
                    unsafe = true
            ) //sketchy af injection
    )
    private void whs_addHorseShoeSlot(IInventory playerInventory, IInventory horseInventoryIn, AbstractHorse horse, EntityPlayer player, CallbackInfo ci){
        this.addSlotToContainer(new Slot(horseInventoryIn, 2, 8, 54) {
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemHorseshoes; //TODO: check why vanilla goes so crazy with checking if its horsearmor breh
            }
            public int getSlotStackLimit()
            {
                return 1;
            }
            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true; //TODO: only allow horseshoes on horses?
            }
        });
    }

    @ModifyConstant(
            method = "<init>",
            constant = @Constant(intValue = 2, ordinal = 0)
    )
    private int whs_shiftInventorySlotIndices(int constant){
        return 3;
    }

    /**
     * @author Nischhelm
     * @reason todo: can be done via inject and shift the 2's to 3's
     */
    @Overwrite
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot == null || !slot.getHasStack()) return stackCopy;

        ItemStack stackOrig = slot.getStack();
        stackCopy = stackOrig.copy();

        int horseInvSize = this.horseInventory.getSizeInventory(); //without horseshoe slot (after player inv)
        int maxSize = this.inventorySlots.size(); //last slot before horseshoe slot

        //from horse to player
        if (index < horseInvSize) {
            if (!this.mergeItemStack(stackOrig, horseInvSize, maxSize, true)) return ItemStack.EMPTY;
        }

        //from player to horse

        //to armor slot
        else if (this.getSlot(1).isItemValid(stackOrig) && !this.getSlot(1).getHasStack()) {
            if (!this.mergeItemStack(stackOrig, 1, 2, false)) return ItemStack.EMPTY;
        //to saddle slot
        } else if (this.getSlot(0).isItemValid(stackOrig) && !this.getSlot(0).getHasStack()) {
            if (!this.mergeItemStack(stackOrig, 0, 1, false)) return ItemStack.EMPTY;
        //to horseshoe slot
        } else if (this.getSlot(2).isItemValid(stackOrig) && !this.getSlot(2).getHasStack()) {
            if (!this.mergeItemStack(stackOrig, 2, 3, false)) return ItemStack.EMPTY;
            //to chest
        } else if (horseInvSize <= 3 || !this.mergeItemStack(stackOrig, 3, horseInvSize, false)) return ItemStack.EMPTY;

        if (stackOrig.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();

        return stackCopy;
    }
}

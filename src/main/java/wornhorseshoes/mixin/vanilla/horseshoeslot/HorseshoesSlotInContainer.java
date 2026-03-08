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
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.item.ItemHorseshoes;

import javax.annotation.Nonnull;

@Mixin(ContainerHorseInventory.class)
public abstract class HorseshoesSlotInContainer extends Container {
    @Shadow @Final private IInventory horseInventory;
    @Shadow @Final private AbstractHorse horse;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void whs_addHorseShoeSlot(IInventory playerInventory, IInventory horseInventoryIn, AbstractHorse horse, EntityPlayer player, CallbackInfo ci){
        if(HorseshoesConfig.canShoeHorse(horse)) {
            Slot slot = new Slot(horseInventoryIn, 2, 8, 54) {
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ItemHorseshoes;
                }

                public int getSlotStackLimit() {
                    return 1;
                }

                @SideOnly(Side.CLIENT)
                public boolean isEnabled() {
                    return true;
                }
            };
            for (int i = 2; i < this.inventorySlots.size(); ++i) {
                this.inventorySlots.get(i).slotNumber = i + 1; //shift all except 0 and 1 one up
            }
            slot.slotNumber = 2;
            this.inventorySlots.add(2, slot);
            this.inventoryItemStacks.add(ItemStack.EMPTY);
        }
    }

    @ModifyConstant(
            method = "<init>",
            constant = @Constant(intValue = 2, ordinal = 0)
    )
    private int whs_shiftInventorySlotIndices(int constant){
        return constant + (HorseshoesConfig.canShoeHorse(this.horse) ? 1 : 0);
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

        boolean hasHorseshoesSlot = HorseshoesConfig.canShoeHorse(this.horse);

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
        } else if (hasHorseshoesSlot && this.getSlot(2).isItemValid(stackOrig) && !this.getSlot(2).getHasStack()) {
            if (!this.mergeItemStack(stackOrig, 2, 3, false)) return ItemStack.EMPTY;
            //to chest
        } else if (horseInvSize <= 2+(hasHorseshoesSlot?1:0) || !this.mergeItemStack(stackOrig, 2+(hasHorseshoesSlot?1:0), horseInvSize, false)) return ItemStack.EMPTY;

        if (stackOrig.isEmpty()) slot.putStack(ItemStack.EMPTY);
        else slot.onSlotChanged();

        return stackCopy;
    }
}

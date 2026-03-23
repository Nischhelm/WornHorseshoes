package wornhorseshoes.mixin.vanilla.horseshoes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
                    return ItemHorseshoes.isHorseshoe(stack.getItem());
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

    @ModifyExpressionValue(
            method = "transferStackInSlot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Slot;isItemValid(Lnet/minecraft/item/ItemStack;)Z", ordinal = 1)
    )
    private boolean whs_dontStackSaddles(boolean isValidSaddle){
        return isValidSaddle && !this.getSlot(0).getHasStack();
    }

    @Definition(id = "horseInventory", field = "Lnet/minecraft/inventory/ContainerHorseInventory;horseInventory:Lnet/minecraft/inventory/IInventory;")
    @Definition(id = "getSizeInventory", method = "Lnet/minecraft/inventory/IInventory;getSizeInventory()I")
    @Expression("this.horseInventory.getSizeInventory() <= 2")
    @ModifyExpressionValue(method = "transferStackInSlot", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean whs_shiftClickToHorseshoeSlot(boolean original, @Local(name = "itemstack1") ItemStack stackOrig, @Cancellable CallbackInfoReturnable<ItemStack> cir){
        //try to shift into horseshoe slot
        boolean hasHorseshoesSlot = HorseshoesConfig.canShoeHorse(this.horse);
        if (hasHorseshoesSlot && this.getSlot(2).isItemValid(stackOrig) && !this.getSlot(2).getHasStack()) {
            if (!this.mergeItemStack(stackOrig, 2, 3, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return original;
            }
        }
        return this.horseInventory.getSizeInventory() <= 2 + (hasHorseshoesSlot ? 1 : 0);
    }

    @ModifyConstant(
            method = "transferStackInSlot",
            constant = @Constant(intValue = 2, ordinal = 2)
    )
    private int whs_shiftMinIndex(int constant){
        return constant + (HorseshoesConfig.canShoeHorse(this.horse) ? 1 : 0); //inventory needs to be shifted one to higher indices. max index is already shifted
    }
}

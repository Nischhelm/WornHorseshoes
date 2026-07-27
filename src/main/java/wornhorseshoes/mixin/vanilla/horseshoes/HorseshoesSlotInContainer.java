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


    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.horseInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.modifyExpressionValue$zbi000$whs_dontStackSaddles(this.getSlot(0).isItemValid(itemstack1))) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, this.constant$zbi000$whs_shiftMinIndex(2), this.horseInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public ItemStack func_82846_b(EntityPlayer var1, int var2) {
        ItemStack var3 = ItemStack.field_190927_a;
        Slot var4 = (Slot)this.field_75151_b.get(var2);
        if (var4 != null && var4.func_75216_d()) {
            ItemStack var5 = var4.func_75211_c();
            var3 = var5.func_77946_l();
            if (var2 < this.field_111243_a.func_70302_i_()) {
                if (!this.func_75135_a(var5, this.field_111243_a.func_70302_i_(), this.field_75151_b.size(), true)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.func_75139_a(1).func_75214_a(var5) && !this.func_75139_a(1).func_75216_d()) {
                if (!this.func_75135_a(var5, 1, 2, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.modifyExpressionValue$bdf000$whs_dontStackSaddles(this.func_75139_a(0).func_75214_a(var5))) {
                if (!this.func_75135_a(var5, 0, 1, false)) {
                    return ItemStack.field_190927_a;
                }
            } else if (this.field_111243_a.func_70302_i_() <= 2 || !this.func_75135_a(var5, this.constant$bdf000$whs_shiftMinIndex(2), this.field_111243_a.func_70302_i_(), false)) {
                return ItemStack.field_190927_a;
            }

            if (var5.func_190926_b()) {
                var4.func_75215_d(ItemStack.field_190927_a);
            } else {
                var4.func_75218_e();
            }
        }

        return var3;
    }
}

package wornhorseshoes.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.menu.HorseshoeContainer;
import wornhorseshoes.util.HorseGear;

/**
 * Adds the horseshoe slot to the horse inventory screen. It's added last (after the player inventory), so every
 * vanilla slot keeps its number; quickMoveStack (shift-click) is redone for menus that have it, with the same rules as
 * vanilla plus: shift-clicking horseshoes puts them on the horse, and shift-clicking the worn ones takes them off.
 */
@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

    @Shadow @Final private Container horseContainer;

    // index of the horseshoe slot in this.slots, or -1 when this horse type has none
    @Unique private int whs$shoeSlot = -1;

    protected HorseInventoryMenuMixin(MenuType<?> type, int id) {
        super(type, id);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void whs$addHorseshoeSlot(int id, Inventory playerInventory, Container container, AbstractHorse horse, CallbackInfo ci) {
        if (!WHSConfig.canShoe(horse)) return;
        this.addSlot(new HorseshoeContainer(horse).createSlot());
        this.whs$shoeSlot = this.slots.size() - 1;
    }

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void whs$quickMove(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (this.whs$shoeSlot < 0) return;   // no horseshoe slot: vanilla handles it
        cir.setReturnValue(whs$moveStack(index));
    }

    /** Vanilla's quickMoveStack with the horseshoe slot (last slot) handled and excluded from the player ranges. */
    @Unique
    private ItemStack whs$moveStack(int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        int horseSize = this.horseContainer.getContainerSize();
        int playerStart = horseSize;                // first player inventory slot
        int hotbarStart = playerStart + 27;
        int playerEnd = this.whs$shoeSlot;          // player slots end where the horseshoe slot starts

        if (index < horseSize || index == this.whs$shoeSlot) {
            // from the horse (saddle, armor, chest or horseshoes) into the player inventory
            if (!this.moveItemStackTo(stack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
        } else if (HorseGear.isHorseshoe(stack) && this.slots.get(this.whs$shoeSlot).mayPlace(stack) && !this.slots.get(this.whs$shoeSlot).hasItem()) {
            if (!this.moveItemStackTo(stack, this.whs$shoeSlot, this.whs$shoeSlot + 1, false)) return ItemStack.EMPTY;
        } else if (this.getSlot(1).mayPlace(stack) && !this.getSlot(1).hasItem()) {
            if (!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
        } else if (this.getSlot(0).mayPlace(stack)) {
            if (!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        } else if (horseSize <= 2 || !this.moveItemStackTo(stack, 2, horseSize, false)) {
            // no horse slot takes it: move between main inventory and hotbar like vanilla
            boolean moved = index >= hotbarStart && index < playerEnd
                    ? this.moveItemStackTo(stack, playerStart, hotbarStart, false)
                    : index >= playerStart && index < hotbarStart
                    ? this.moveItemStackTo(stack, hotbarStart, playerEnd, false)
                    : this.moveItemStackTo(stack, hotbarStart, hotbarStart, false);
            if (!moved) return ItemStack.EMPTY;
            return ItemStack.EMPTY;   // vanilla returns EMPTY here too (stops the shift-click loop)
        }
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return original;
    }
}

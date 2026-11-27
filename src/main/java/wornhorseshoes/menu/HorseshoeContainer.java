package wornhorseshoes.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wornhorseshoes.util.HorseGear;

/**
 * A one-slot view of a horse's FEET equipment slot, so the horse inventory screen can show a horseshoe slot without
 * changing the horse's own inventory (whose slot numbers the saddle, armor and chest contents depend on).
 */
public class HorseshoeContainer implements Container {

    private final AbstractHorse horse;

    public HorseshoeContainer(AbstractHorse horse) {
        this.horse = horse;
    }

    /** The slot the horse inventory screen gets (below the armor slot, where the 1.12 mod had it). */
    public Slot createSlot() {
        return new Slot(this, 0, 8, 54) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return HorseGear.isHorseshoe(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        };
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return HorseGear.getShoes(this.horse);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack shoes = getItem(0);
        if (shoes.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        ItemStack taken = shoes.split(amount);
        HorseGear.setShoes(this.horse, shoes.isEmpty() ? ItemStack.EMPTY : shoes);
        return taken;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack shoes = getItem(0);
        HorseGear.setShoes(this.horse, ItemStack.EMPTY);
        return shoes;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        HorseGear.setShoes(this.horse, stack);
    }

    @Override
    public void setChanged() {
        // the horse's equipment is saved and synced by the game itself
    }

    @Override
    public boolean stillValid(Player player) {
        return this.horse.isAlive() && this.horse.distanceTo(player) < 8.0F;
    }

    @Override
    public void clearContent() {
        HorseGear.setShoes(this.horse, ItemStack.EMPTY);
    }
}

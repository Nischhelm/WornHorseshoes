package wornhorseshoes.mixin.vanilla.enchantment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wornhorseshoes.util.IHorseStackGetter;

import javax.annotation.Nonnull;
import java.util.Arrays;

@Mixin(AbstractHorse.class)
public abstract class HorseInventorySlots extends EntityLivingBase {
    public HorseInventorySlots(World worldIn) {
        super(worldIn);
    }

    @Shadow protected ContainerHorseChest horseChest;

    @Override
    @Nonnull
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn){
        if(!this.world.isRemote) {
            switch (slotIn) {
                case HEAD: case CHEST: //protects both
                    return this.horseChest.getStackInSlot(1);
                case FEET:
                    return this.horseChest.getStackInSlot(2);
            }
        } else {
            //horseChest (inventory) not synced
            switch (slotIn) {
                case HEAD: case CHEST: //protects both
                    return IHorseStackGetter.getArmorStack((AbstractHorse) (Object)this);
                case FEET:
                    return ((IHorseStackGetter) this).whs$getHorseshoesStack();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    //This allows protection / FF
    public Iterable<ItemStack> getArmorInventoryList() { //TODO: this can probably be done less stupidly. idk man, horsechest would need to be removed or smth. or shadowed
        //Using horse armor twice since it protects both head + chest
        if(!this.world.isRemote)
            return Arrays.asList(this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(2));
        else {
            ItemStack armor = IHorseStackGetter.getArmorStack((AbstractHorse)(Object)this);
            ItemStack shoes = ((IHorseStackGetter) this).whs$getHorseshoesStack();
            return Arrays.asList(armor, armor, shoes);
        }
    }
}

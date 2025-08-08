package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wornhorseshoes.config.ModConfigHandler;

import javax.annotation.Nonnull;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends EntityLivingBase {
    public AbstractHorseMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow protected ContainerHorseChest horseChest;

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

    @Override
    public boolean shouldDismountInWater(@Nonnull Entity rider) {
        if(ModConfigHandler.depthStriderNoDismount) {
            int depthStriderLvl = EnchantmentHelper.getDepthStriderModifier(this);
            return depthStriderLvl <= 0;
        }
        return super.shouldDismountInWater(rider);
    }
}

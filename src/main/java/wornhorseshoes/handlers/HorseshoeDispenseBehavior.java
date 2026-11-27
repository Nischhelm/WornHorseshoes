package wornhorseshoes.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.HorseGear;

/**
 * Dispensers put horseshoes on the first horse in front of them that can wear some and has none (vanilla does the
 * same for horse armor on tamed horses). With no such horse the item is dropped like any other.
 */
public class HorseshoeDispenseBehavior extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        BlockPos front = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        for (AbstractHorse horse : source.getLevel().getEntitiesOfClass(AbstractHorse.class, new AABB(front),
                h -> h.isAlive() && WHSConfig.canShoe(h) && HorseGear.getShoes(h).isEmpty())) {
            HorseGear.setShoes(horse, stack.split(1));
            this.setSuccess(true);
            return stack;
        }
        return super.execute(source, stack);   // nothing to shoe: drop it
    }
}

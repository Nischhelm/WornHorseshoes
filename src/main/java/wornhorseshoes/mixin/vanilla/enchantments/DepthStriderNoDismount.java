package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(AbstractHorse.class)
public abstract class DepthStriderNoDismount extends EntityLivingBase {
    public DepthStriderNoDismount(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean shouldDismountInWater(@Nonnull Entity rider) {
        int depthStriderLvl = EnchantmentHelper.getDepthStriderModifier(this);
        return depthStriderLvl <= 0 && super.shouldDismountInWater(rider);
    }
}

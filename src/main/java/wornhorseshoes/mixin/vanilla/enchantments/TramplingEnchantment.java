package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wornhorseshoes.enchantment.EnchantmentTrampling;

import javax.annotation.Nonnull;

@Mixin(AbstractHorse.class)
public abstract class TramplingEnchantment extends EntityLivingBase {
    public TramplingEnchantment(World worldIn) {
        super(worldIn);
    }

    @Shadow protected int gallopTime;
    @Shadow public abstract boolean isRearing();

    @Override
    protected void collideWithEntity(@Nonnull Entity entityIn) {
        super.collideWithEntity(entityIn);
        //Knockback and hurt hit mobs
        EnchantmentTrampling.handleCollision((AbstractHorse) (Object) this, entityIn, this.gallopTime, this.isRearing());
    }

    @Override
    public void knockBack(@Nonnull Entity entityIn, float strength, double xRatio, double zRatio) {
        //Stops horse getting knocked back when ridden with trampling hooves
        if(
                this.isBeingRidden() &&
                        (this.gallopTime > 3 || this.isRearing()) &&
                        EnchantmentHelper.getMaxEnchantmentLevel(EnchantmentTrampling.INSTANCE, this) > 0
        ) return;

        super.knockBack(entityIn, strength, xRatio, zRatio);
    }
}

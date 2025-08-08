package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import wornhorseshoes.enchantment.EnchantmentTrampling;

import javax.annotation.Nonnull;

@Mixin(AbstractHorse.class)
public abstract class TramplingEnchantment extends EntityLivingBase {
    @Shadow protected int gallopTime;

    public TramplingEnchantment(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void collideWithEntity(@Nonnull Entity entityIn) {
        super.collideWithEntity(entityIn);

        if(this.world.isRemote) return;
        if(this.gallopTime <= 3) return;
        if(!(entityIn instanceof EntityLivingBase)) return;
        if(this.getPassengers().contains(entityIn)) return;

        int tramplingLvl = EnchantmentHelper.getMaxEnchantmentLevel(EnchantmentTrampling.INSTANCE, this);
        if(tramplingLvl <= 0) return;

        EntityLivingBase victim = (EntityLivingBase) entityIn;

        double knockbackAngle = whs$getKnockbackAngle(victim, tramplingLvl);
        victim.knockBack(this, 1.0F * tramplingLvl, - Math.sin(knockbackAngle), - Math.cos(knockbackAngle));
        victim.attackEntityFrom(DamageSource.causeMobDamage(this), 1.5F * tramplingLvl);
    }

    @Unique
    private double whs$getKnockbackAngle(EntityLivingBase victim, int tramplingLvl) {
        double TAU = 2 * Math.PI;
        double distAngle = (Math.atan2(victim.posX - this.posX, victim.posZ - this.posZ) + TAU); // PI to 3 PI

        //maff be maffing
        double motionAngle = -this.rotationYaw / 180 * Math.PI; //0 to 2 PI

        double angleDiff = (distAngle - motionAngle + TAU) % TAU; //0 to 2 PI
        if (angleDiff > Math.PI) angleDiff -= TAU; //-PI to PI

        double angleLimit = 15. / 180. * Math.PI * (4 - tramplingLvl);

        return motionAngle + MathHelper.clamp(angleDiff, -angleLimit, angleLimit);
    }

    @Override
    public void knockBack(@Nonnull Entity entityIn, float strength, double xRatio, double zRatio) {
        //Stops horse getting knocked back when ridden with trampling hooves

        if(this.isBeingRidden()) {
            if (this.gallopTime > 3) {
                int tramplingLvl = EnchantmentHelper.getMaxEnchantmentLevel(EnchantmentTrampling.INSTANCE, this);
                if (tramplingLvl > 0) {
                    return;
                }
            }
        }
        super.knockBack(entityIn, strength, xRatio, zRatio);
    }
}

package wornhorseshoes.mixin.vanilla.zombieskeletonhorses;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.ICanDespawn;

@Mixin(EntityLiving.class)
public abstract class ZombieRiderDespawn extends EntityLivingBase {
    public ZombieRiderDespawn(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "despawnEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLiving;setDead()V")
    )
    private void whs_despawnRiddenZombieHorse(CallbackInfo ci) {
        if (!((EntityLivingBase) this instanceof EntityZombie)) return;
        if (this.getRidingEntity() instanceof EntityZombieHorse)
            this.getRidingEntity().setDead();
    }
}

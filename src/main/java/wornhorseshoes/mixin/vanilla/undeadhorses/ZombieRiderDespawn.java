package wornhorseshoes.mixin.vanilla.undeadhorses;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (!((EntityLiving)(Object) this instanceof EntityZombie)) return;
        if (this.getRidingEntity() instanceof EntityZombieHorse)
            this.getRidingEntity().setDead();
    }
}

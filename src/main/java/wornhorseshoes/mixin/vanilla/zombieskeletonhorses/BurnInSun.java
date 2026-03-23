package wornhorseshoes.mixin.vanilla.zombieskeletonhorses;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.util.IHorseStackGetter;

@Mixin(AbstractHorse.class)
public abstract class BurnInSun extends EntityAnimal {
    public BurnInSun(World worldIn) {
        super(worldIn);
    }

    @Unique
    private boolean whs$shouldBurnInDay() {
        AbstractHorse thisHorse = (AbstractHorse) (Object) this;
        switch (ModConfigHandler.undead.burnTypes){
            case BOTH: return thisHorse instanceof EntityZombieHorse || thisHorse instanceof EntitySkeletonHorse;
            case ZOMBIE: return thisHorse instanceof EntityZombieHorse;
            case SKELETON: return thisHorse instanceof EntitySkeletonHorse;
        }
        return false;
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    public void whs_burnInSun(CallbackInfo ci) {
        //Quite the conditions
        if (this.world.isRemote) return;
        if (!this.world.isDaytime()) return;
        if (!ModConfigHandler.undead.childrenBurn && this.isChild()) return;
        if (!this.whs$shouldBurnInDay()) return;
        AbstractHorse thisHorse = (AbstractHorse) (Object) this;
        if (ModConfigHandler.undead.armorProtects && !IHorseStackGetter.getArmorStack(thisHorse).isEmpty()) return;
        float brightness = this.getBrightness();
        if (brightness <= 0.5 || brightness <= this.rand.nextFloat() * 15 + 0.4) return;
        if (!this.world.canSeeSky(new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ))) return;

        this.setFire(8);
    }
}

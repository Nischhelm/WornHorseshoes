package wornhorseshoes.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.network.NetworkHandler;
import wornhorseshoes.network.ToServerSetRearingPacket;
import wornhorseshoes.util.IRearingSetter;

@Mixin(AbstractHorse.class)
public abstract class RearingFix extends EntityLivingBase implements IRearingSetter {
    public RearingFix(World worldIn) {
        super(worldIn);
    }

    @Unique private boolean whs$isRearing = false;

    @Inject(method = "setRearing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;setHorseWatchableBoolean(IZ)V"))
    private void whs_sendRearingStateToServer(boolean rearing, CallbackInfo ci){
        whs$setIsRearingNoUpdate(rearing); //Both sides should store in separate field
        if(this.world.isRemote) NetworkHandler.sendToServer(new ToServerSetRearingPacket(this, rearing));
    }

    @Override
    public void whs$setIsRearingNoUpdate(boolean isRearing){
        this.whs$isRearing = isRearing;
    }

    @ModifyReturnValue(method = "isRearing", at = @At("RETURN"))
    private boolean whs_returnCachedValue(boolean original){
        return whs$isRearing;
    }
}

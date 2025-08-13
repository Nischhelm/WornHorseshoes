package wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.LayerHorseSaddle;

@Mixin(RenderAbstractHorse.class)
public abstract class RenderHorseSaddle_AbstractHorse extends RenderLiving<EntityHorse> {
    public RenderHorseSaddle_AbstractHorse(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;F)V", at = @At("TAIL"))
    private void whs_registerHorseArmorLayer(RenderManager renderManagerIn, float scaleIn, CallbackInfo ci){
        this.addLayer(new LayerHorseSaddle(this));
    }
}

package wornhorseshoes.mixin.vanilla.renderenchantedlayers.saddle;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.layer.LayerHorseSaddle;

@Mixin(RenderAbstractHorse.class)
public abstract class RenderAbstractHorseSaddle extends RenderLiving<AbstractHorse> {
    public RenderAbstractHorseSaddle(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;F)V", at = @At("TAIL"))
    private void whs_registerHorseSaddleLayer(RenderManager renderManagerIn, float scaleIn, CallbackInfo ci){
        this.addLayer(new LayerHorseSaddle(this));
    }
}

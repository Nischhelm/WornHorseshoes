package wornhorseshoes.mixin.vanilla.renderenchantedlayers.horseshoes;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderAbstractHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.layer.LayerHorseShoes;

@Mixin(RenderAbstractHorse.class)
public abstract class RenderAbstractHorseshoes extends RenderLiving<AbstractHorse> {
    public RenderAbstractHorseshoes(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;F)V", at = @At("TAIL"))
    private void whs_registerHorseShoesLayer(RenderManager renderManagerIn, float scaleIn, CallbackInfo ci){
        this.addLayer(new LayerHorseShoes(this));
    }
}

package wornhorseshoes.mixin.vanilla.renderenchantedlayers.horseshoes;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityLlama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.layer.LayerHorseShoes;
import wornhorseshoes.client.model.ModelHorseShoes_Llama;

@Mixin(RenderLlama.class)
public abstract class RenderLlamaShoes extends RenderLiving<EntityLlama> {
    public RenderLlamaShoes(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void whs_registerLlamaShoesLayer(RenderManager renderer, CallbackInfo ci){
        this.addLayer(new LayerHorseShoes(this, new ModelHorseShoes_Llama(0.1F)));
    }
}
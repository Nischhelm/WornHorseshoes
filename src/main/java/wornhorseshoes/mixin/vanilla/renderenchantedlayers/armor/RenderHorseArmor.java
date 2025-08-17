package wornhorseshoes.mixin.vanilla.renderenchantedlayers.armor;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.layer.LayerHorseArmor;

@Mixin(RenderHorse.class)
public abstract class RenderHorseArmor extends RenderLiving<EntityHorse> {
    public RenderHorseArmor(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void whs_registerHorseArmorLayer(RenderManager renderer, CallbackInfo ci){
        this.addLayer(new LayerHorseArmor(this));
    }
}

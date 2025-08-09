package wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.client.LayerHorseArmor;

@Mixin(RenderHorse.class)
public abstract class RenderHorseArmor extends RenderLiving<EntityHorse> {
    public RenderHorseArmor(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerHorseArmorRenderer(RenderManager renderer, CallbackInfo ci){
        this.addLayer(new LayerHorseArmor((RenderHorse) (Object) this));
    }
}

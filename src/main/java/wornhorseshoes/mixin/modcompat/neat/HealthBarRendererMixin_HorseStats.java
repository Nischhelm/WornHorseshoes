package wornhorseshoes.mixin.modcompat.neat;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.neat.HealthBarRenderer;
import wornhorseshoes.util.HorseStatUtil;

import java.text.DecimalFormat;

@Mixin(HealthBarRenderer.class)
public abstract class HealthBarRendererMixin_HorseStats {

    @Unique
    private static final DecimalFormat whs$statformatter = new DecimalFormat("#.#");

    @Inject(
            method = "renderHealthBar",
            at = @At(value = "FIELD", target = "Lvazkii/neat/NeatConfig;showCurrentHP:Z"),
            remap = false
    )
    public void whs_renderHorseStats(EntityLivingBase passedEntity, float partialTicks, Entity viewPoint, CallbackInfo ci, @Local Minecraft mc) {
        if(!(passedEntity instanceof AbstractHorse)) return;
        AbstractHorse horse = (AbstractHorse) passedEntity;

        String text = "^" + whs$statformatter.format(HorseStatUtil.getJumpHeight(horse));
        text += " >" + whs$statformatter.format(HorseStatUtil.getSpeed(horse));

        mc.fontRenderer.drawString(text, 0, 30, 16777215);
    }
}

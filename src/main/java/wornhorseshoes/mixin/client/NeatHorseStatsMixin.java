package wornhorseshoes.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.HorseGear;

import java.text.DecimalFormat;

/**
 * Neat compat: adds "^jump >speed" under a horse's Neat health bar. Injected right before Neat checks showCurrentHP,
 * where the pose is already moved and scaled for the bar's text, so the line lines up with Neat's own numbers.
 * @Pseudo + require 0: without Neat this mixin is simply skipped.
 */
@Pseudo
@Mixin(targets = "vazkii.neat.HealthBarRenderer", remap = false)
public abstract class NeatHorseStatsMixin {
    @Unique private static final DecimalFormat WHS_FORMAT = new DecimalFormat("#.#");

    @Inject(method = "hookRender", require = 0,
            at = @At(value = "INVOKE", target = "Lvazkii/neat/NeatConfig$ConfigAccess;showCurrentHP()Z"))
    private static void whs$drawStats(Entity entity, PoseStack pose, MultiBufferSource buffers, Camera camera,
                                      EntityRenderer<? super Entity> renderer, float partialTicks, double x, double y, double z,
                                      CallbackInfo ci) {
        if (!(entity instanceof AbstractHorse horse) || !WHSConfig.DRAW_STATS_NEAT.get()) return;
        String text = "^" + WHS_FORMAT.format(HorseGear.jumpHeight(horse)) + " >" + WHS_FORMAT.format(HorseGear.speed(horse));
        Minecraft.getInstance().font.drawInBatch(text, 0F, 30F, 0xFFFFFF, false, pose.last().pose(), buffers,
                Font.DisplayMode.NORMAL, 0, 0xF000F0);
    }
}

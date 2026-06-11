package wornhorseshoes.mixin.vanilla;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.HorseStatUtil;

@Mixin(GuiScreenHorseInventory.class)
public abstract class HorseStatsDisplay extends GuiContainer {
    public HorseStatsDisplay(Container container) {
        super(container);
    }

    @Shadow @Final private AbstractHorse horseEntity;

    @Inject(method = "drawGuiContainerForegroundLayer", at = @At("TAIL"))
    private void whs_drawHorseStats(int mouseX, int mouseY, CallbackInfo ci){
        String text = "^" + (int) HorseStatUtil.getJumpHeight(this.horseEntity);
        this.fontRenderer.drawString(text, 27, 19, 0x00AAAAAA);

        text = ">" + (int) HorseStatUtil.getSpeed(this.horseEntity);
        this.fontRenderer.drawString(text, 78 - this.fontRenderer.getStringWidth(text), 19, 0x00AAAAAA);
    }
}

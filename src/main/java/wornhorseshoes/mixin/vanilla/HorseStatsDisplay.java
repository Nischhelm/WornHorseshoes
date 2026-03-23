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

@Mixin(GuiScreenHorseInventory.class)
public abstract class HorseStatsDisplay extends GuiContainer {
    public HorseStatsDisplay(Container container) {
        super(container);
    }

    @Shadow @Final private AbstractHorse horseEntity;

    @Inject(method = "drawGuiContainerForegroundLayer", at = @At("TAIL"))
    private void whs_drawHorseStats(int mouseX, int mouseY, CallbackInfo ci){
        double roundedJumpHeight = this.horseEntity.getHorseJumpStrength();
        roundedJumpHeight = -0.6238977 + 2.9998898 * roundedJumpHeight  + 2.8384039 * roundedJumpHeight * roundedJumpHeight; // approximating with a quadratic [0.1 : 0.1 : 1, 1.5] map to [1.125 1.5 2.125 2.875 3.625 4.375 5.25 10.25]
        this.fontRenderer.drawString("^" + (int) roundedJumpHeight, 27, 19, 0x00AAAAAA);

        double roundedSpeed = this.horseEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 43.17; //apparently 1 speed = 43.17 blocks per sec
        String text = ">" + (int) roundedSpeed;
        this.fontRenderer.drawString(text, 78 - this.fontRenderer.getStringWidth(text), 19, 0x00AAAAAA);
    }
}

package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenHorseInventory.class)
public abstract class GuiScreenHorseInventoryMixin extends GuiContainer {
    public GuiScreenHorseInventoryMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(
            method = "drawGuiContainerBackgroundLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;wearsArmor()Z")
    )
    private void whs_drawHorseshoeSlot(float partialTicks, int mouseX, int mouseY, CallbackInfo ci){
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i + 7, j + 53, 0, this.ySize, 18, 18);
    }
}

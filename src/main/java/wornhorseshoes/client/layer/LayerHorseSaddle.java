package wornhorseshoes.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wornhorseshoes.client.model.ModelHorseSaddle;
import wornhorseshoes.mixin.vanilla.renderenchantedlayers.saddle.RenderAccessor;
import wornhorseshoes.util.IHorseStackGetter;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerHorseSaddle implements LayerRenderer<AbstractHorse> {
    private final RenderLiving<? extends AbstractHorse> horseRenderer;
    private final ModelHorseSaddle horseSaddleModel;

    public LayerHorseSaddle(RenderLiving<? extends AbstractHorse> rendererIn) {
        this.horseRenderer = rendererIn;
        this.horseSaddleModel = new ModelHorseSaddle();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doRenderLayer(@Nonnull AbstractHorse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack horseArmor = ((IHorseStackGetter) horse).whs$getSaddleStack();
        if (horseArmor.getItem() instanceof ItemSaddle) {
            this.horseRenderer.bindTexture(((RenderAccessor) this.horseRenderer).invokeGetEntityTexture(horse));
            this.horseSaddleModel.setModelAttributes(this.horseRenderer.getMainModel());
            this.horseSaddleModel.setLivingAnimations(horse, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1, 1, 1, 1);
            this.horseSaddleModel.render(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if(horseArmor.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.horseRenderer, horse, this.horseSaddleModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
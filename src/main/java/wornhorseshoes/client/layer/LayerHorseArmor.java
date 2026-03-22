package wornhorseshoes.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wornhorseshoes.client.model.ModelHorseArmor;
import wornhorseshoes.util.IHorseStackGetter;

@SideOnly(Side.CLIENT)
public class LayerHorseArmor <T extends AbstractHorse> implements LayerRenderer<T> {
    private final RenderLiving<T> horseRenderer;
    private final ModelHorseArmor horseArmorModel;

    public LayerHorseArmor(RenderLiving<T> rendererIn) {
        this.horseRenderer = rendererIn;
        this.horseArmorModel = new ModelHorseArmor(0.1F);
    }

    @Override
    public void doRenderLayer(AbstractHorse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack horseArmor = IHorseStackGetter.getArmorStack(horse);
        if (!horseArmor.isEmpty()) {
            this.horseRenderer.bindTexture(((IHorseStackGetter)horse).whs$getArmorTexture());
            this.horseArmorModel.setModelAttributes(this.horseRenderer.getMainModel());
            this.horseArmorModel.setLivingAnimations(horse, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1, 1, 1, 1);
            this.horseArmorModel.render(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if(horseArmor.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.horseRenderer, horse, this.horseArmorModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
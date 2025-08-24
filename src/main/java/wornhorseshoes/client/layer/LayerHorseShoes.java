package wornhorseshoes.client.layer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wornhorseshoes.client.model.ModelHorseShoes;
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.item.ItemHorseshoes;
import wornhorseshoes.util.IHorseStackGetter;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerHorseShoes implements LayerRenderer<AbstractHorse> {
    protected final RenderLiving<? extends AbstractHorse> horseRenderer;
    protected final ModelBase horseShoesModel;

    public LayerHorseShoes(RenderLiving<? extends AbstractHorse> rendererIn) {
        this(rendererIn, new ModelHorseShoes(0.1F));
    }

    //Explicitly used by Llama cause it has a different Model
    public LayerHorseShoes(RenderLiving<? extends AbstractHorse> rendererIn, ModelBase horseShoesModel) {
        this.horseRenderer = rendererIn;
        this.horseShoesModel = horseShoesModel;
    }

    @Override
    public void doRenderLayer(@Nonnull AbstractHorse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!HorseshoesConfig.canShoeHorse(horse)) return;

        ItemStack horseshoes = ((IHorseStackGetter) horse).whs$getHorseshoesStack();
        if (horseshoes.getItem() instanceof ItemHorseshoes) {
            this.horseRenderer.bindTexture(ItemHorseshoes.getHorseshoesTexture(horse, horseshoes));
            this.horseShoesModel.setModelAttributes(this.horseRenderer.getMainModel());
            this.horseShoesModel.setLivingAnimations(horse, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1, 1, 1, 1);
            this.horseShoesModel.render(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if(horseshoes.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.horseRenderer, horse, this.horseShoesModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
package wornhorseshoes.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted.HorseArmorAccessor;

@SideOnly(Side.CLIENT)
public class LayerHorseArmor implements LayerRenderer<EntityHorse> {
    private final RenderHorse horseRenderer;
    private final ModelHorseArmor horseArmorModel;

    public LayerHorseArmor(RenderHorse rendererIn) {
        this.horseRenderer = rendererIn;
        this.horseArmorModel = new ModelHorseArmor(0.1F);
    }

    @Override
    public void doRenderLayer(EntityHorse horse, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack horseArmor = horse.getDataManager().get(((HorseArmorAccessor) horse).getArmorStack());
        if (!horseArmor.isEmpty()) {
            this.horseRenderer.bindTexture(getTextureFromStack(horseArmor, horse));
            this.horseArmorModel.setModelAttributes(this.horseRenderer.getMainModel());
            this.horseArmorModel.setLivingAnimations(horse, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1, 1, 1, 1);
            this.horseArmorModel.render(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if(horseArmor.isItemEnchanted())
                LayerArmorBase.renderEnchantedGlint(this.horseRenderer, horse, this.horseArmorModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    private static ResourceLocation getTextureFromStack(ItemStack stack, EntityHorse horse) {
        return new ResourceLocation(stack.getItem().getHorseArmorTexture(horse, stack));
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
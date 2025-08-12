package wornhorseshoes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted.HorseArmorAccessor;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class LayerHorseArmor implements LayerRenderer<EntityHorse> {
    private static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");

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
                renderEnchantedGlint(this.horseRenderer, horse, this.horseArmorModel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    private static ResourceLocation getTextureFromStack(ItemStack stack, EntityHorse horse) {
        return new ResourceLocation(stack.getItem().getHorseArmorTexture(horse, stack));
    }

    public static void renderEnchantedGlint(RenderLivingBase<EntityHorse> renderer, EntityLivingBase entity, ModelBase model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float subTick = (float) entity.ticksExisted + partialTicks;

        renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);

        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(GL_EQUAL);
        GlStateManager.depthMask(false);
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.disableLighting();

        for (float passCounter = 0; passCounter < 1.1F; ++passCounter) { //2x
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
            GlStateManager.matrixMode(GL_TEXTURE);
            GlStateManager.loadIdentity();
            GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.rotate(30.0F - passCounter * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, subTick * (0.001F + passCounter * 0.003F) * 20.0F, 0.0F);

            GlStateManager.matrixMode(GL_MODELVIEW);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.matrixMode(GL_TEXTURE);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(GL_LEQUAL);
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
package wornhorseshoes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.HorseGear;

/**
 * Draws worn horseshoes and, for horse types vanilla doesn't draw armor on (donkey, mule, zombie and skeleton horse),
 * horse armor. Enchanted gear also gets the armor glint; for the normal horse that glint goes over vanilla's own armor
 * layer, since vanilla never draws one there.
 */
public class HorseGearLayer<T extends AbstractHorse, M extends HorseModel<T>> extends RenderLayer<T, M> {
    private final HorseModel<T> shoesModel;
    private final HorseModel<T> armorModel;

    public HorseGearLayer(RenderLayerParent<T, M> parent, EntityModelSet models) {
        super(parent);
        this.shoesModel = new HorseModel<>(models.bakeLayer(HorseshoeModels.HORSE));
        this.armorModel = new HorseModel<>(models.bakeLayer(ModelLayers.HORSE_ARMOR));  // same mesh vanilla's armor uses
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, T horse, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack shoes = HorseGear.getShoes(horse);
        if (WHSConfig.HORSESHOES_LAYER.get() && !shoes.isEmpty()) {
            animate(this.shoesModel, horse, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
            this.shoesModel.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(HorseGear.shoesTexture(shoes))),
                    light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            if (shoes.hasFoil()) glint(this.shoesModel, pose, buffers, light);
        }

        ItemStack armor = HorseGear.getArmor(horse);
        if (!(armor.getItem() instanceof HorseArmorItem armorItem)) return;
        boolean vanillaDraws = horse instanceof Horse;
        if (!vanillaDraws && (!WHSConfig.ARMOR_LAYER.get() || !WHSConfig.canArmor(horse))) return;
        if (!vanillaDraws || armor.hasFoil())
            animate(this.armorModel, horse, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
        if (!vanillaDraws) {
            float r = 1F, g = 1F, b = 1F;
            if (armorItem instanceof DyeableHorseArmorItem dyeable) {      // leather horse armor keeps its dye colour
                int c = dyeable.getColor(armor);
                r = (c >> 16 & 255) / 255F; g = (c >> 8 & 255) / 255F; b = (c & 255) / 255F;
            }
            this.armorModel.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(armorItem.getTexture())),
                    light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
        }
        if (armor.hasFoil()) glint(this.armorModel, pose, buffers, light);
    }

    /** Copies the body's pose (baby, riding...) and runs the vanilla horse animation on the overlay model. */
    private void animate(HorseModel<T> model, T horse, float limbSwing, float limbSwingAmount, float partialTick,
                         float ageInTicks, float netHeadYaw, float headPitch) {
        this.getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(horse, limbSwing, limbSwingAmount, partialTick);
        model.setupAnim(horse, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    /**
     * Draws the model again with the scrolling enchantment glint. The glint only shows where its depth exactly equals
     * what's already drawn, so it must be the glint type that matches how the gear was drawn: entityGlint for the plain
     * entity render type used here and by vanilla's horse armor layer (armorEntityGlint is offset to match player armor's
     * own render type, and draws nothing over these).
     */
    static void glint(net.minecraft.client.model.EntityModel<?> model, PoseStack pose, MultiBufferSource buffers, int light) {
        model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityGlint()), light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }
}

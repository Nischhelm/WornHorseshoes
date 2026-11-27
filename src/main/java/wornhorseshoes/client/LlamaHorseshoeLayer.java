package wornhorseshoes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.HorseGear;

/** Horseshoes on llamas, which have their own model (the 1.12 mod had a separate llama model for the same reason). */
public class LlamaHorseshoeLayer extends RenderLayer<Llama, LlamaModel<Llama>> {
    private final LlamaModel<Llama> model;

    public LlamaHorseshoeLayer(RenderLayerParent<Llama, LlamaModel<Llama>> parent, EntityModelSet models) {
        super(parent);
        this.model = new LlamaModel<>(models.bakeLayer(HorseshoeModels.LLAMA));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, Llama llama, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack shoes = HorseGear.getShoes(llama);
        if (!WHSConfig.HORSESHOES_LAYER.get() || shoes.isEmpty()) return;
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(llama, limbSwing, limbSwingAmount, partialTick);
        this.model.setupAnim(llama, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(HorseGear.shoesTexture(shoes))),
                light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        if (shoes.hasFoil()) HorseGearLayer.glint(this.model, pose, buffers, light);
    }
}

package wornhorseshoes.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelHorseShoes_Llama extends ModelBase {
    //Copied and boiled down from ModelLlama
    public final ModelRenderer leg1,leg2,leg3,leg4;

    public ModelHorseShoes_Llama(float scale) {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.leg1 = new ModelRenderer(this, 96, 51);
        this.leg1.addBox(-2.0F, 11.0F, -2.0F, 4, 3, 4, scale);
        this.leg1.setRotationPoint(-3.5F, 10.0F, 6.0F);

        this.leg2 = new ModelRenderer(this, 78, 51);
        this.leg2.addBox(-2.0F, 11.0F, -2.0F, 4, 3, 4, scale);
        this.leg2.setRotationPoint(3.5F, 10.0F, 6.0F);

        this.leg3 = new ModelRenderer(this, 60, 51);
        this.leg3.addBox(-2.0F, 11.0F, -2.0F, 4, 3, 4, scale);
        this.leg3.setRotationPoint(-3.5F, 10.0F, -5.0F);

        this.leg4 = new ModelRenderer(this, 44, 51);
        this.leg4.addBox(-2.0F, 11.0F, -2.0F, 4, 3, 4, scale);
        this.leg4.setRotationPoint(3.5F, 10.0F, -5.0F);
    }

    @Override
    public void render(@Nonnull Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (this.isChild) return;
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        this.leg1.render(scale);
        this.leg2.render(scale);
        this.leg3.render(scale);
        this.leg4.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn) {
        float f1 = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.rotateAngleX = f1;
        this.leg2.rotateAngleX = - f1;
        this.leg3.rotateAngleX = - f1;
        this.leg4.rotateAngleX = f1;
    }
}
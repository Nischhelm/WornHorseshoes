package wornhorseshoes.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelHorseShoes extends ModelBase {
    //Copied and boiled down from ModelHorse
    private final ModelRenderer backLeftHoof;
    private final ModelRenderer backRightHoof;
    private final ModelRenderer frontLeftHoof;
    private final ModelRenderer frontRightHoof;

    public ModelHorseShoes(float scale) {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.backLeftHoof = new ModelRenderer(this, 78, 51);
        this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4, scale);
        this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);

        this.backRightHoof = new ModelRenderer(this, 96, 51);
        this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4, scale);
        this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);

        this.frontLeftHoof = new ModelRenderer(this, 44, 51);
        this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4, scale);
        this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);

        this.frontRightHoof = new ModelRenderer(this, 60, 51);
        this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4, scale);
        this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        AbstractHorse abstracthorse = (AbstractHorse)entity;
        if(abstracthorse.isChild()) return;

        this.backLeftHoof.render(scale);
        this.backRightHoof.render(scale);
        this.frontLeftHoof.render(scale);
        this.frontRightHoof.render(scale);
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);

        AbstractHorse abstracthorse = (AbstractHorse)entity;
        float rearingAmount = abstracthorse.getRearingAmount(partialTickTime);
        float invRearingAmount = 1.0F - rearingAmount;
        float f10 = - MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
        float f11 = f10 * 0.8F;
        float f10a = f10 * 0.5F;
        float f12 = 0.2617994F * rearingAmount;
        float f12a = f12 - f10a * invRearingAmount;
        float f12b = f12 + f10a * invRearingAmount;
        float f13 = - MathHelper.cos(((float)entity.ticksExisted + partialTickTime) * 0.6F);
        float f13a = f13 - f11;
        float f14 = (-1.0471976F + f13a) * rearingAmount + f11;
        float f15 = (-1.0471976F - f13a) * rearingAmount - f11;

        float f16a = Math.max(0.0F, f10a);
        float f16b = Math.max(0.0F, -f10a);

        this.backLeftHoof.rotateAngleX = -0.08726646F * rearingAmount - (f10a + f16a) * invRearingAmount;
        this.backRightHoof.rotateAngleX = -0.08726646F * rearingAmount + (f10a - f16b) * invRearingAmount;
        this.frontLeftHoof.rotateAngleX = rearingAmount * (f14 + (float)Math.PI * Math.max(0.0F, 0.2F + f13 * 0.2F)) + invRearingAmount * (f11 + f16a);
        this.frontRightHoof.rotateAngleX = rearingAmount * (f15 + (float)Math.PI * Math.max(0.0F, 0.2F - f13 * 0.2F)) + invRearingAmount * (-f11 + f16b);

        this.backLeftHoof.rotationPointY =  9.0F + MathHelper.cos(f12a) * 7.0F;
        this.backLeftHoof.rotationPointZ = 11.0F + MathHelper.sin(f12a) * 7.0F;

        this.backRightHoof.rotationPointY = 9.0F + MathHelper.cos(f12b) * 7.0F;
        this.backRightHoof.rotationPointZ = 11.0F + MathHelper.sin(f12b) * 7.0F;

        float rotFrontY = 9.0F - 11.0F * rearingAmount;
        float rotFrontZ = -8.0F + 6.0F * rearingAmount;

        this.frontLeftHoof.rotationPointY = rotFrontY + MathHelper.cos(f14) * 7.0F;
        this.frontLeftHoof.rotationPointZ = rotFrontZ + MathHelper.sin(f14) * 7.0F;

        this.frontRightHoof.rotationPointY = rotFrontY + MathHelper.cos(f15) * 7.0F;
        this.frontRightHoof.rotationPointZ = rotFrontZ + MathHelper.sin(f15) * 7.0F;
    }
}
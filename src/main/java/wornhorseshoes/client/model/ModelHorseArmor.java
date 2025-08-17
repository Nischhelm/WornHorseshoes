package wornhorseshoes.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelHorseArmor extends ModelBase {
    //Copied and boiled down from ModelHorse
    private final ModelRenderer head;
    private final ModelRenderer upperMouth;
    private final ModelRenderer lowerMouth;
    private final ModelRenderer horseLeftEar;
    private final ModelRenderer horseRightEar;
    private final ModelRenderer neck;
    private final ModelRenderer mane;
    private final ModelRenderer body;
    private final ModelRenderer tailBase;
    private final ModelRenderer backLeftLeg;
    private final ModelRenderer backRightLeg;
    private final ModelRenderer frontLeftLeg;
    private final ModelRenderer frontRightLeg;

    public ModelHorseArmor(float scaleFactor) {

        this.textureWidth = 128;
        this.textureHeight = 128;

        this.body = new ModelRenderer(this, 0, 34);
        this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24, scaleFactor);
        this.body.setRotationPoint(0.0F, 11.0F, 9.0F);

        this.tailBase = new ModelRenderer(this, 44, 0);
        this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3, scaleFactor);
        this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.tailBase.rotateAngleX = -1.134464F;

        this.backLeftLeg = new ModelRenderer(this, 78, 29);
        this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5, scaleFactor);
        this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);

        this.backRightLeg = new ModelRenderer(this, 96, 29);
        this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5, scaleFactor);
        this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);

        this.frontLeftLeg = new ModelRenderer(this, 44, 29);
        this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4, scaleFactor);
        this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);

        this.frontRightLeg = new ModelRenderer(this, 60, 29);
        this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4, scaleFactor);
        this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);

        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7, scaleFactor);
        this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.head.rotateAngleX = 0.5235988F;
        this.upperMouth = new ModelRenderer(this, 24, 18);
        this.upperMouth.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6, scaleFactor);
        this.upperMouth.setRotationPoint(0.0F, 3.95F, -10.0F);
        this.upperMouth.rotateAngleX = 0.5235988F;
        this.lowerMouth = new ModelRenderer(this, 24, 27);
        this.lowerMouth.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5, scaleFactor);
        this.lowerMouth.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.lowerMouth.rotateAngleX = 0.5235988F;
        this.head.addChild(this.upperMouth);
        this.head.addChild(this.lowerMouth);

        this.horseLeftEar = new ModelRenderer(this, 0, 0);
        this.horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1, scaleFactor);
        this.horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseLeftEar.rotateAngleX = 0.5235988F;

        this.horseRightEar = new ModelRenderer(this, 0, 0);
        this.horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1, scaleFactor);
        this.horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseRightEar.rotateAngleX = 0.5235988F;

        this.neck = new ModelRenderer(this, 0, 12);
        this.neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8, scaleFactor);
        this.neck.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.neck.rotateAngleX = 0.5235988F;

        this.mane = new ModelRenderer(this, 58, 0);
        this.mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4, scaleFactor);
        this.mane.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.mane.rotateAngleX = 0.5235988F;
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        AbstractHorse horse = (AbstractHorse)entity;
        if(horse.isChild()) return;
        if(horse instanceof AbstractChestHorse) return;

        this.backLeftLeg.render(scale);
        this.backRightLeg.render(scale);
        this.frontLeftLeg.render(scale);
        this.frontRightLeg.render(scale);

        this.body.render(scale);
        this.tailBase.render(scale);
        this.neck.render(scale);
        this.mane.render(scale);

        this.horseLeftEar.render(scale);
        this.horseRightEar.render(scale);

        this.head.render(scale);
    }

    private float updateHorseRotation(float prevAngle, float currAngle, float partialTick) {
        float angleDiff = currAngle - prevAngle;
        for (; angleDiff < -180.0F; angleDiff += 360.0F) ;
        while (angleDiff >= 180.0F) angleDiff -= 360.0F;

        return prevAngle + partialTick * angleDiff;
    }

    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);

        float currYaw = this.updateHorseRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTickTime);
        float currYawHead = this.updateHorseRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTickTime);
        float currPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;

        float yawDiff = MathHelper.clamp(currYawHead - currYaw, -20.0F, 20.0F);
        float currPitchRadians = currPitch * 0.017453292F;

        if (limbSwingAmount > 0.2F) currPitchRadians += MathHelper.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;

        AbstractHorse abstracthorse = (AbstractHorse)entity;
        float grassEatingAmount = abstracthorse.getGrassEatingAmount(partialTickTime);
        float rearingAmount = abstracthorse.getRearingAmount(partialTickTime);
        float maxHeadAngleChange = 1.0F - Math.max(rearingAmount, grassEatingAmount);
        float f7 = 1.0F - rearingAmount;
        float mouthOpenness = abstracthorse.getMouthOpennessAngle(partialTickTime);
        boolean tailIsWagging = abstracthorse.tailCounter != 0;
        float subTickCounter = (float)entity.ticksExisted + partialTickTime;
        float f10 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI);
        float f11 = f10 * 0.8F * limbSwingAmount;

        this.head.rotationPointY = 4.0F;
        this.head.rotationPointZ = -10.0F;
        this.tailBase.rotationPointY = 3.0F;
        this.body.rotateAngleX = 0.0F;
        this.head.rotateAngleX = 0.5235988F + currPitchRadians;
        this.head.rotateAngleY = yawDiff * 0.017453292F;
        this.head.rotateAngleX = rearingAmount * (0.2617994F + currPitchRadians) + grassEatingAmount * 2.1816616F + maxHeadAngleChange * this.head.rotateAngleX;
        this.head.rotateAngleY = rearingAmount * yawDiff * 0.017453292F + maxHeadAngleChange * this.head.rotateAngleY;
        this.head.rotationPointY = rearingAmount * -6.0F + grassEatingAmount * 11.0F + maxHeadAngleChange * this.head.rotationPointY;
        this.head.rotationPointZ = rearingAmount * -1.0F + grassEatingAmount * -10.0F + maxHeadAngleChange * this.head.rotationPointZ;
        this.tailBase.rotationPointY = rearingAmount * 9.0F + f7 * this.tailBase.rotationPointY;
        this.body.rotateAngleX = rearingAmount * -((float)Math.PI / 4F) + f7 * this.body.rotateAngleX;
        this.horseLeftEar.rotationPointY = this.head.rotationPointY;
        this.horseRightEar.rotationPointY = this.head.rotationPointY;
        this.neck.rotationPointY = this.head.rotationPointY;
        this.upperMouth.rotationPointY = 0.02F;
        this.lowerMouth.rotationPointY = 0.0F;
        this.mane.rotationPointY = this.head.rotationPointY;
        this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
        this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
        this.neck.rotationPointZ = this.head.rotationPointZ;
        this.upperMouth.rotationPointZ = 0.02F - mouthOpenness;
        this.lowerMouth.rotationPointZ = mouthOpenness;
        this.mane.rotationPointZ = this.head.rotationPointZ;
        this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
        this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
        this.neck.rotateAngleX = this.head.rotateAngleX;
        this.upperMouth.rotateAngleX = -0.09424778F * mouthOpenness;
        this.lowerMouth.rotateAngleX = 0.15707964F * mouthOpenness;
        this.mane.rotateAngleX = this.head.rotateAngleX;
        this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
        this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.upperMouth.rotateAngleY = 0.0F;
        this.lowerMouth.rotateAngleY = 0.0F;
        this.mane.rotateAngleY = this.head.rotateAngleY;
        float f12 = 0.2617994F * rearingAmount;
        float f13 = MathHelper.cos(subTickCounter * 0.6F + (float)Math.PI);
        this.frontLeftLeg.rotationPointY = -2.0F * rearingAmount + 9.0F * f7;
        this.frontLeftLeg.rotationPointZ = -2.0F * rearingAmount + -8.0F * f7;
        this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
        this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
        float f14 = (-1.0471976F + f13) * rearingAmount + f11 * f7;
        float f15 = (-1.0471976F - f13) * rearingAmount + -f11 * f7;
        this.backLeftLeg.rotateAngleX = f12 + -f10 * 0.5F * limbSwingAmount * f7;
        this.backRightLeg.rotateAngleX = f12 + f10 * 0.5F * limbSwingAmount * f7;
        this.frontLeftLeg.rotateAngleX = f14;
        this.frontRightLeg.rotateAngleX = f15;

        f12 = Math.min(-1.3089969F + limbSwingAmount * 1.5F, 0.0F);

        if (tailIsWagging) {
            this.tailBase.rotateAngleY = MathHelper.cos(subTickCounter * 0.7F);
            f12 = 0.0F;
        }
        else this.tailBase.rotateAngleY = 0.0F;

        this.tailBase.rotateAngleX = f12;
    }
}
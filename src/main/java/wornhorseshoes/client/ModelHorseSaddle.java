package wornhorseshoes.client;

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
public class ModelHorseSaddle extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer body;

    private final ModelRenderer horseFaceRopes;
    private final ModelRenderer horseSaddleBottom;
    private final ModelRenderer horseSaddleFront;
    private final ModelRenderer horseSaddleBack;
    private final ModelRenderer horseLeftSaddleRope;
    private final ModelRenderer horseLeftSaddleMetal;
    private final ModelRenderer horseRightSaddleRope;
    private final ModelRenderer horseRightSaddleMetal;
    private final ModelRenderer horseLeftFaceMetal;
    private final ModelRenderer horseRightFaceMetal;
    private final ModelRenderer horseLeftRein;
    private final ModelRenderer horseRightRein;

    public ModelHorseSaddle() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.body = new ModelRenderer(this, 0, 34);
        this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
        this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
        this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.head.rotateAngleX = 0.5235988F;
        this.horseSaddleBottom = new ModelRenderer(this, 80, 0);
        this.horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
        this.horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseSaddleFront = new ModelRenderer(this, 106, 9);
        this.horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
        this.horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseSaddleBack = new ModelRenderer(this, 80, 9);
        this.horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
        this.horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
        this.horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
        this.horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
        this.horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.horseRightSaddleRope = new ModelRenderer(this, 80, 0);
        this.horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
        this.horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
        this.horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseLeftFaceMetal.rotateAngleX = 0.5235988F;
        this.horseRightFaceMetal = new ModelRenderer(this, 74, 13);
        this.horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
        this.horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseRightFaceMetal.rotateAngleX = 0.5235988F;
        this.horseLeftRein = new ModelRenderer(this, 44, 10);
        this.horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseRightRein = new ModelRenderer(this, 44, 5);
        this.horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseFaceRopes = new ModelRenderer(this, 80, 12);
        this.horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
        this.horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.horseFaceRopes.rotateAngleX = 0.5235988F;
    }

    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        AbstractHorse horse = (AbstractHorse)entity;
        if(horse.isChild() || ! horse.isHorseSaddled()) return;

        this.horseFaceRopes.render(scale);
        this.horseSaddleBottom.render(scale);
        this.horseSaddleFront.render(scale);
        this.horseSaddleBack.render(scale);
        this.horseLeftSaddleRope.render(scale);
        this.horseLeftSaddleMetal.render(scale);
        this.horseRightSaddleRope.render(scale);
        this.horseRightSaddleMetal.render(scale);
        this.horseLeftFaceMetal.render(scale);
        this.horseRightFaceMetal.render(scale);

        if (horse.isBeingRidden()) {
            this.horseLeftRein.render(scale);
            this.horseRightRein.render(scale);
        }
    }

    private float updateHorseRotation(float prevAngle, float currAngle, float partialTick) {
        float angleDiff = currAngle - prevAngle;
        for (; angleDiff < -180.0F; angleDiff += 360.0F) ;
        while (angleDiff >= 180.0F) angleDiff -= 360.0F;

        return prevAngle + partialTick * angleDiff;
    }

    public void setLivingAnimations(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        AbstractHorse horse = (AbstractHorse)entity;
        if(!horse.isHorseSaddled()) return;

        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
        float currYaw = this.updateHorseRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTickTime);
        float currYawHead = this.updateHorseRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTickTime);
        float currPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;

        float yawDiff = MathHelper.clamp(currYawHead - currYaw, -20.0F, 20.0F);

        float currPitchRadians = currPitch * 0.017453292F;

        if (limbSwingAmount > 0.2F) currPitchRadians += MathHelper.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;

        float grassEatingAmount = horse.getGrassEatingAmount(partialTickTime);
        float rearingAmount = horse.getRearingAmount(partialTickTime);
        float maxHeadAngleChange = 1.0F - Math.max(rearingAmount, grassEatingAmount);
        float invRearingAmount = 1.0F - rearingAmount;
        boolean isRidden = horse.isBeingRidden();

        this.head.rotateAngleX = rearingAmount * (0.2617994F + currPitchRadians) + grassEatingAmount * 2.1816616F + maxHeadAngleChange * (0.5235988F + currPitchRadians);
        this.head.rotateAngleY = rearingAmount * yawDiff * 0.017453292F + maxHeadAngleChange * yawDiff * 0.017453292F;
        this.head.rotationPointY = - rearingAmount * 6.0F + grassEatingAmount * 11.0F + maxHeadAngleChange * 4.0F;
        this.head.rotationPointZ = - rearingAmount - grassEatingAmount * 10.0F - maxHeadAngleChange * 10.0F;

        this.body.rotateAngleX = - rearingAmount * (float)Math.PI / 4F;

        this.horseSaddleBottom.rotationPointY = rearingAmount * 0.5F + invRearingAmount * 2.0F;
        this.horseSaddleBottom.rotationPointZ = rearingAmount * 11.0F + invRearingAmount * 2.0F;

        this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
        this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
        this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
        this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
        this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
        this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;

        this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
        this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
        this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
        this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
        this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
        this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;

        this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
        this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
        this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;

        this.horseLeftRein.rotationPointY = this.head.rotationPointY;
        this.horseRightRein.rotationPointY = this.head.rotationPointY;
        this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
        this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
        this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;

        this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
        this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
        this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
        this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
        this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;

        this.horseLeftRein.rotateAngleX = currPitchRadians;
        this.horseRightRein.rotateAngleX = currPitchRadians;

        this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
        this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
        this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
        this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
        this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
        this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
        this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
        this.horseRightRein.rotateAngleY = this.head.rotateAngleY;

        if (isRidden) {
            this.horseLeftSaddleRope.rotateAngleX = -1.0471976F;
            this.horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
            this.horseRightSaddleRope.rotateAngleX = -1.0471976F;
            this.horseRightSaddleMetal.rotateAngleX = -1.0471976F;
            this.horseLeftSaddleRope.rotateAngleZ = 0.0F;
            this.horseLeftSaddleMetal.rotateAngleZ = 0.0F;
            this.horseRightSaddleRope.rotateAngleZ = 0.0F;
            this.horseRightSaddleMetal.rotateAngleZ = 0.0F;
        } else {
            float f11 = -0.8F * limbSwingAmount * MathHelper.cos(limbSwing * 0.6662F);
            this.horseLeftSaddleRope.rotateAngleX = f11 / 3.0F;
            this.horseLeftSaddleMetal.rotateAngleX = f11 / 3.0F;
            this.horseRightSaddleRope.rotateAngleX = f11 / 3.0F;
            this.horseRightSaddleMetal.rotateAngleX = f11 / 3.0F;
            this.horseLeftSaddleRope.rotateAngleZ = f11 / 5.0F;
            this.horseLeftSaddleMetal.rotateAngleZ = f11 / 5.0F;
            this.horseRightSaddleRope.rotateAngleZ = -f11 / 5.0F;
            this.horseRightSaddleMetal.rotateAngleZ = -f11 / 5.0F;
        }
    }
}
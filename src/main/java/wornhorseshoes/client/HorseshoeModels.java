package wornhorseshoes.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import wornhorseshoes.WornHorseshoes;

/**
 * Model layers for the worn horseshoes. Each is a copy of the vanilla horse (or llama) part tree, because HorseModel /
 * LlamaModel look their parts up by name and animate them, but every part is empty except a hoof-sized box at the
 * bottom of each leg. Rendering this with the vanilla model classes means the hooves move exactly like the legs.
 * Texture offsets come from the 1.12 mod's ModelHorseShoes, so its 128x128 horseshoe textures work unchanged.
 */
public final class HorseshoeModels {
    public static final ModelLayerLocation HORSE = new ModelLayerLocation(new ResourceLocation(WornHorseshoes.MODID, "horseshoes"), "main");
    public static final ModelLayerLocation LLAMA = new ModelLayerLocation(new ResourceLocation(WornHorseshoes.MODID, "llama_horseshoes"), "main");

    private static final CubeDeformation HOOF = new CubeDeformation(0.1F);   // slightly larger than the leg so it sits on top

    private HorseshoeModels() {}

    /** Horse-shaped tree (horse, donkey, mule, skeleton and zombie horse all use HorseModel's part names). */
    public static LayerDefinition horse() {
        final float PIVOT_Y = 14F;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        // empty body and head, with the children HorseModel's constructor asks for
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0F, 11F, 5F));
        body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head_parts", CubeListBuilder.create(), PartPose.offset(0F, 4F, -12F));
        PartDefinition skull = head.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        skull.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.ZERO);
        skull.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.ZERO);
        for (String part : new String[]{"mane", "upper_mouth", "left_saddle_mouth", "right_saddle_mouth", "left_saddle_line",
                "right_saddle_line", "head_saddle", "mouth_saddle_wrap"})
            head.addOrReplaceChild(part, CubeListBuilder.create(), PartPose.ZERO);

        // adult legs are 11 tall from y=-1.01; the hoof is the bottom 3 (same x/z as the leg box)
        leg(root, "left_hind_leg", 78, 51, -3F, -1F, 7F, 4F, PIVOT_Y, 7F);
        leg(root, "right_hind_leg", 96, 51, -1F, -1F, 7F, -4F, PIVOT_Y, 7F);
        leg(root, "left_front_leg", 44, 51, -3F, -1.9F, 7F, 4F, PIVOT_Y, -12F);
        leg(root, "right_front_leg", 60, 51, -1F, -1.9F, 7F, -4F, PIVOT_Y, -12F);
        // baby legs are the same box stretched by 5.5 both ways and drawn at half size, which leaves their bottom 0.7 below
        // the ground; the hoof ends at y=14 instead, where the half-size leg meets the ground, so the shoe stays visible
        leg(root, "left_hind_baby_leg", 78, 51, -3F, -1F, 11F, 4F, PIVOT_Y, 7F);
        leg(root, "right_hind_baby_leg", 96, 51, -1F, -1F, 11F, -4F, PIVOT_Y, 7F);
        leg(root, "left_front_baby_leg", 44, 51, -3F, -1.9F, 11F, 4F, PIVOT_Y, -12F);
        leg(root, "right_front_baby_leg", 60, 51, -1F, -1.9F, 11F, -4F, PIVOT_Y, -12F);
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Llama-shaped tree (llama and trader llama). Llama legs are 14 tall from y=0. */
    public static LayerDefinition llama() {
        final float PIVOT_Y = 10F;
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0F, 7F, -6F));
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0F, 5F, 2F));
        root.addOrReplaceChild("right_chest", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("left_chest", CubeListBuilder.create(), PartPose.ZERO);
        leg(root, "right_hind_leg", 96, 51, -2F, -2F, 11F, -3.5F, PIVOT_Y, 6F);
        leg(root, "left_hind_leg", 78, 51, -2F, -2F, 11F, 3.5F, PIVOT_Y, 6F);
        leg(root, "right_front_leg", 60, 51, -2F, -2F, 11F, -3.5F, PIVOT_Y, -5F);
        leg(root, "left_front_leg", 44, 51, -2F, -2F, 11F, 3.5F, PIVOT_Y, -5F);
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** A leg part at the vanilla pivot holding only a 4x3x4 hoof box. */
    private static void leg(PartDefinition root, String name, int u, int v, float x, float z, float y,
                            float pivotX, float pivotY, float pivotZ) {
        root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(u, v).addBox(x, y, z, 4F, 3F, 4F, HOOF),
                PartPose.offset(pivotX, pivotY, pivotZ));
    }
}

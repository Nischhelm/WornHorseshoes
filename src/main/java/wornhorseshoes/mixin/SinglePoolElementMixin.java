package wornhorseshoes.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.registry.ModRegistry;
import wornhorseshoes.util.HorseGear;

import java.util.List;

/**
 * Village encounters, ported from the 1.12 well/church encounters. 1.20 villages are jigsaw structures, so this hooks
 * the piece placement and looks at the template name:
 *  - a village town centre ("/town_centers/", the 1.12 well's replacement) may get an untamed horse with random horseshoes;
 *  - a village temple ("_temple") may get Juan, a brown horse with enchanted golden horseshoes, at 1/100 of that chance.
 * place() runs once per chunk the piece overlaps, so the horse is only spawned from the chunk holding the piece's centre.
 */
@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin {

    @Shadow @Final protected Either<ResourceLocation, StructureTemplate> template;
    @Shadow public abstract BoundingBox getBoundingBox(StructureTemplateManager manager, BlockPos pos, Rotation rotation);

    @Inject(method = "place", at = @At("RETURN"))
    private void whs$villageEncounter(StructureTemplateManager manager, WorldGenLevel level, StructureManager structures,
                                      ChunkGenerator generator, BlockPos offset, BlockPos pos, Rotation rotation,
                                      BoundingBox box, RandomSource random, boolean keepJigsaws, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() || !WHSConfig.WELL_ENCOUNTER_ENABLED.get()) return;
        String path = this.template.left().map(ResourceLocation::getPath).orElse("");
        if (!path.startsWith("village/")) return;
        boolean centre = path.contains("/town_centers/");
        boolean temple = path.contains("_temple");
        if (!centre && !temple) return;

        // only the chunk containing the piece's centre spawns the horse, so it happens once per piece
        Vec3i mid = this.getBoundingBox(manager, offset, rotation).getCenter();
        if (!box.isInside(mid)) return;

        Horse horse = EntityType.HORSE.create(level.getLevel());
        if (horse == null || !WHSConfig.canShoe(horse)) return;
        float chance = WHSConfig.WELL_ENCOUNTER_CHANCE.get().floatValue() / (temple ? 100F : 1F);
        if (random.nextFloat() >= chance) return;

        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mid.getX(), mid.getZ());
        horse.moveTo(mid.getX() + 0.5D, y, mid.getZ() + 0.5D, random.nextFloat() * 360F, 0F);
        horse.finalizeSpawn(level, level.getCurrentDifficultyAt(horse.blockPosition()), MobSpawnType.STRUCTURE, null, null);
        horse.setPersistenceRequired();

        if (temple) {
            horse.setCustomName(Component.literal("Juan"));
            ((HorseInvoker) horse).whs$setVariantAndMarkings(Variant.BROWN, Markings.NONE);   // 1.12 variant 3
            ItemStack shoes = new ItemStack(ModRegistry.GOLD_HORSESHOES.get());
            EnchantmentHelper.enchantItem(random, shoes, 20, false);
            HorseGear.setShoes(horse, shoes);
        } else {
            WHSConfig.ensureLoaded();
            List<ItemStack> options = ModRegistry.HORSESHOES.stream().map(r -> new ItemStack(r.get()))
                    .filter(s -> !WHSConfig.wellBlacklist.contains(s.getItem())).toList();
            if (options.isEmpty()) return;
            HorseGear.setShoes(horse, options.get(random.nextInt(options.size())));
        }
        level.addFreshEntityWithPassengers(horse);
    }
}

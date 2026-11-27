package wornhorseshoes.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.HorseGear;
import wornhorseshoes.util.UndeadHorses;

/** Server-side horse events. */
public final class HorseEvents {

    private HorseEvents() {}

    /** Lucky Horseshoe on a horse: nobody riding it (or riding its riders) takes fall damage, the horse neither. */
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (!event.getSource().is(DamageTypeTags.IS_FALL)) return;
        Entity bottom = event.getEntity().getRootVehicle();
        if (bottom instanceof AbstractHorse horse && HorseGear.isLuckyHorseshoe(HorseGear.getShoes(horse))) event.setCanceled(true);
    }

    /** Undead horses burn in daylight (like zombies) unless they wear armor. */
    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse) || horse.level().isClientSide) return;
        if (!WHSConfig.UNDEAD_BURN.get() || !burnsType(horse)) return;
        if (horse.isBaby() && !WHSConfig.BURN_BABIES.get()) return;
        if (WHSConfig.BURN_ARMOR_PROTECTS.get() && !HorseGear.getArmor(horse).isEmpty()) return;
        if (!horse.level().isDay()) return;
        // the zombie's sunburn check (Mob.isSunBurnTick): bright, random by brightness, dry, under open sky
        float brightness = horse.getLightLevelDependentMagicValue();
        BlockPos eyes = BlockPos.containing(horse.getX(), horse.getEyeY(), horse.getZ());
        boolean wet = horse.isInWaterRainOrBubble() || horse.isInPowderSnow || horse.wasInPowderSnow;
        if (brightness > 0.5F && horse.getRandom().nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F && !wet && horse.level().canSeeSky(eyes)) {
            horse.setSecondsOnFire(8);
        }
    }

    private static boolean burnsType(AbstractHorse horse) {
        return switch (WHSConfig.BURN_TYPES.get()) {
            case ZOMBIE -> horse instanceof ZombieHorse;
            case SKELETON -> horse instanceof SkeletonHorse;
            case BOTH -> horse instanceof ZombieHorse || horse instanceof SkeletonHorse;
        };
    }

    /** A despawning zombie takes the zombie horse it spawned on with it. */
    @SubscribeEvent
    public static void onLeave(EntityLeaveLevelEvent event) {
        if (!(event.getEntity() instanceof Zombie zombie) || zombie.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        if (zombie.getVehicle() instanceof ZombieHorse horse && UndeadHorses.canDespawn(horse)) horse.discard();
    }
}

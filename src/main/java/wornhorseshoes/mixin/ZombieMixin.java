package wornhorseshoes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.util.UndeadHorses;

import javax.annotation.Nullable;

/**
 * Zombie riders: a zombie (or husk or zombie villager) spawning under open sky sometimes spawns on a zombie horse,
 * which may get armor and horseshoes, and despawns with it. Drowned and zombified piglins are left out (the 1.12 mod's
 * surface zombies didn't include them).
 */
@Mixin(Zombie.class)
public abstract class ZombieMixin {

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void whs$spawnOnHorse(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                  @Nullable SpawnGroupData data, @Nullable CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Zombie self = (Zombie) (Object) this;
        if (!WHSConfig.ZOMBIE_RIDER_ENABLED.get() || self instanceof Drowned || self instanceof ZombifiedPiglin) return;
        if (self.isPassenger() || self.getRandom().nextFloat() >= WHSConfig.ZOMBIE_RIDER_CHANCE.get()) return;
        if (!level.canSeeSky(self.blockPosition())) return;
        ZombieHorse horse = EntityType.ZOMBIE_HORSE.create(level.getLevel());
        if (horse == null) return;
        horse.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), self.getXRot());
        horse.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null, null);
        horse.setAge(self.isBaby() ? -24000 : 0);
        horse.setTamed(true);                  // tamed so it accepts its zombie rider's steering
        UndeadHorses.setCanDespawn(horse);
        UndeadHorses.equip(horse, difficulty.getSpecialMultiplier());
        level.addFreshEntity(horse);
        self.startRiding(horse, true);         // the zombie itself is added by whatever is spawning it
    }
}

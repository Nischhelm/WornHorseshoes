package wornhorseshoes.mixin.vanilla.encounter;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.util.HorseEquipUtil;
import wornhorseshoes.util.ICanDespawn;

@Mixin(EntityZombie.class)
public abstract class ZombieHorseRider extends EntityMob {
    @Shadow
    public abstract boolean isChild();

    public ZombieHorseRider(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "onInitialSpawn", at = @At("TAIL"))
    private void whs_spawnOnHorse(DifficultyInstance difficulty, IEntityLivingData livingdata, CallbackInfoReturnable<IEntityLivingData> cir){
        if(this.getRNG().nextFloat() >= ModConfigHandler.encounters.zombieRiderChance) return;


        EntityZombieHorse horse = new EntityZombieHorse(this.world);
        horse.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.world.spawnEntity(horse);

        //TODO: why does armor/shoes sometimes get dmgd on death? and why does it drop twice then?
        horse.setGrowingAge(this.isChild() ? -24000 : 0); //TODO: test
        horse.setHorseTamed(true);
        ((ICanDespawn)horse).whs$setCanDespawn(true);
        HorseEquipUtil.equipHorse(horse, difficulty.getClampedAdditionalDifficulty());

        this.startRiding(horse);
    }

    //TODO: make them burn in sunlight
}

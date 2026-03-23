package wornhorseshoes.mixin.vanilla.undeadhorses;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import wornhorseshoes.util.ICanDespawn;

@Mixin({EntityZombieHorse.class, EntitySkeletonHorse.class})
public abstract class HorseDespawn extends EntityAnimal implements ICanDespawn {
    public HorseDespawn(World worldIn) {
        super(worldIn);
    }

    @Unique private boolean whs$canDespawn = false;

    @Override
    public boolean canDespawn(){
        return whs$getCanDespawn();
    }

    @Override
    public boolean whs$getCanDespawn(){
        return whs$canDespawn;
    }

    @Override
    public void whs$setCanDespawn(boolean canDespawn){
        this.whs$canDespawn = canDespawn;
    }
}

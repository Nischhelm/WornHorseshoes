package wornhorseshoes.mixin.vanilla.horsestats;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import wornhorseshoes.config.ModConfigHandler;

import java.util.Random;

@Mixin({EntityHorse.class, AbstractChestHorse.class, EntityZombieHorse.class, EntitySkeletonHorse.class})
public abstract class AllHorseMixin_ModifyStats extends AbstractHorse {
    public AllHorseMixin_ModifyStats(World worldIn) {
        super(worldIn);
    }

    @ModifyArg(
            method = "applyEntityAttributes",
            at = @At(value = "INVOKE", target = "getEntityAttribute(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;")
    )
    private IAttribute whs_saveAttribute(IAttribute iattr, @Share("attr")LocalRef<String> attr){
        attr.set(iattr.getName());
        return iattr;
    }

    @ModifyArg(
            method = "applyEntityAttributes",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;setBaseValue(D)V")
    )
    private double whs_modifyAttribute(double original, @Share("attr")LocalRef<String> attr){
        ResourceLocation loc = EntityList.getKey(this);
        if(loc == null) return original;
        String horseId = loc.toString();

        Double base, range;
        switch (attr.get()) {
            case "generic.movementSpeed":
                base = ModConfigHandler.stats.startSpeedBase.get(horseId);
                range = ModConfigHandler.stats.startSpeedRange.get(horseId);
                if(base == null || range == null) return original;

                return (base + range * whs$bellishRange(rand)) / 43.17;

            case "horse.jumpStrength":
                base = ModConfigHandler.stats.startJumpHeightBase.get(horseId);
                range = ModConfigHandler.stats.startJumpHeightRange.get(horseId);
                if(base == null || range == null) return original;

                return base + range * whs$bellishRange(rand);

            case "generic.maxHealth":
                base = ModConfigHandler.stats.startMaxHPBase.get(horseId);
                range = ModConfigHandler.stats.startMaxHPRange.get(horseId);
                if(base == null || range == null) return original;

                return (int) (base + range * whs$bellishRange(rand));
        }
        return original;
    }

    @Unique
    private double whs$bellishRange(Random rand){
        return (rand.nextFloat() + rand.nextFloat() + rand.nextFloat())/3.;
    }
}

package wornhorseshoes.mixin.vanilla.renderenchantedlayers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.util.IHorseStackGetter;

@Mixin(AbstractHorse.class)
public abstract class SyncStacks extends Entity implements IHorseStackGetter {
    @Shadow protected ContainerHorseChest horseChest;

    public SyncStacks(World worldIn) {
        super(worldIn);
    }

    @Unique private static final DataParameter<ItemStack> SADDLE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);
    @Unique private static final DataParameter<ItemStack> HORSESHOE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;initHorseChest()V", unsafe = true))
    private void whs_registerSaddleDataParameter(World worldIn, CallbackInfo ci){
        this.dataManager.register(SADDLE_STACK, ItemStack.EMPTY);
        this.dataManager.register(HORSESHOE_STACK, ItemStack.EMPTY);
    }

    @ModifyExpressionValue(
            method = "updateHorseSlots",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ContainerHorseChest;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack whs_updateSaddleStack(ItemStack original){
        this.dataManager.set(SADDLE_STACK, original);
        this.dataManager.set(HORSESHOE_STACK, this.horseChest.getStackInSlot(2));
        return original;
    }

    @Override
    public ItemStack whs$getSaddleStack() {
        return this.dataManager.get(SADDLE_STACK);
    }

    @Override
    public ItemStack whs$getHorseshoesStack() {
        return this.dataManager.get(HORSESHOE_STACK);
    }
}

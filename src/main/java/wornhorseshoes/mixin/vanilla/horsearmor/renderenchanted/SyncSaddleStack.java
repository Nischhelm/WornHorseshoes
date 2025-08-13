package wornhorseshoes.mixin.vanilla.horsearmor.renderenchanted;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.util.ISaddleGetter;

@Mixin(AbstractHorse.class)
public abstract class SyncSaddleStack extends Entity implements ISaddleGetter {
    public SyncSaddleStack(World worldIn) {
        super(worldIn);
    }

    @Unique private static final DataParameter<ItemStack> SADDLE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;initHorseChest()V", unsafe = true))
    private void whs_registerSaddleDataParameter(World worldIn, CallbackInfo ci){
        this.dataManager.register(SADDLE_STACK, ItemStack.EMPTY);
    }

    @ModifyExpressionValue(
            method = "updateHorseSlots",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ContainerHorseChest;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack whs_updateSaddleStack(ItemStack original){
        WornHorseshoes.LOGGER.info("stack null {} datamanager null {} key null {}", original == null, this.dataManager == null, SADDLE_STACK == null);
        this.dataManager.set(SADDLE_STACK, original);
        return original;
    }

    @Override
    public ItemStack whs$getSaddleStack() {
        return this.dataManager.get(SADDLE_STACK);
    }
}

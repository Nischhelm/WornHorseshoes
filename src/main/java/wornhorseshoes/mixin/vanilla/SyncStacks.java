package wornhorseshoes.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.util.IHorseStackGetter;

import javax.annotation.Nullable;

@Mixin(AbstractHorse.class)
public abstract class SyncStacks extends Entity implements IHorseStackGetter {
    @Shadow protected ContainerHorseChest horseChest;

    public SyncStacks(World worldIn) {
        super(worldIn);
    }

    static {
        IHorseStackGetter.registerDataParameters();
    }

    @ModifyExpressionValue(
            method = "updateHorseSlots",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ContainerHorseChest;getStackInSlot(I)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack whs_updateSaddleStack(ItemStack original){
        this.dataManager.set(IHorseStackGetter.SADDLE_STACK, original);
        if(HorseshoesConfig.canShoeHorse((AbstractHorse) (Object) this))
            this.dataManager.set(IHorseStackGetter.HORSESHOE_STACK, this.horseChest.getStackInSlot(2));
        return original;
    }

    @Override
    public ItemStack whs$getSaddleStack() {
        return this.dataManager.get(IHorseStackGetter.SADDLE_STACK);
    }

    @Override
    public ItemStack whs$getHorseshoesStack() {
        return this.dataManager.get(IHorseStackGetter.HORSESHOE_STACK);
    }

    @Unique @Nullable
    private ResourceLocation whs$armorLoc;

    @Override @Nullable
    public ResourceLocation whs$getArmorTexture(){
        return this.whs$armorLoc;
    }

    @Override
    public void whs$setArmorTexture(@Nullable ResourceLocation loc){
        this.whs$armorLoc = loc;
    }

    @Inject(method = "onUpdate", at = @At(value = "TAIL"))
    private void whs_updateArmorTexture(CallbackInfo ci){
        if (!this.world.isRemote) return;
        if (!this.dataManager.isDirty()) return;
        AbstractHorse thisHorse = (AbstractHorse) (Object) this;
        ItemStack armorStack = IHorseStackGetter.getArmorStack(thisHorse);
        if(armorStack.isEmpty()) whs$setArmorTexture(null);
        else {
            String loc = armorStack.getItem().getHorseArmorTexture(thisHorse, armorStack);
            whs$setArmorTexture(loc == null ? null : new ResourceLocation(loc));
        }
    }
}

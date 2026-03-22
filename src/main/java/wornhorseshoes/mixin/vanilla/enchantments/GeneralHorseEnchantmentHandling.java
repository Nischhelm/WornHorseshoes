package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wornhorseshoes.mixin.vanilla.renderenchantedlayers.armor.HorseArmorAccessor;
import wornhorseshoes.util.IHorseStackGetter;
import wornhorseshoes.util.ISkipsPotionCoreHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

@Mixin(AbstractHorse.class)
public abstract class GeneralHorseEnchantmentHandling extends EntityLivingBase {
    public GeneralHorseEnchantmentHandling(World worldIn) {
        super(worldIn);
    }

    @Shadow protected ContainerHorseChest horseChest;

    @Override
    @Nonnull
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn){
        if(!this.world.isRemote) {
            switch (slotIn) {
                case HEAD:
                case CHEST: //protects both
                    return this.horseChest.getStackInSlot(1);
                case FEET:
                    return this.horseChest.getStackInSlot(2);
                default:
                    return ItemStack.EMPTY;
            }
        } else {
            //horseChest (inventory) not synced
            switch (slotIn) {
                case HEAD: case CHEST: //protects both
                    if((AbstractHorse)(Object) this instanceof EntityHorse)
                        return this.getDataManager().get(HorseArmorAccessor.getArmorStack());
                case FEET:
                    return ((IHorseStackGetter) this).whs$getHorseshoesStack();
                default:
                    return ItemStack.EMPTY;
            }
        }
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/passive/AbstractHorse;jumpPower:F"
            ),
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/init/SoundEvents;ENTITY_HORSE_JUMP:Lnet/minecraft/util/SoundEvent;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorse;getAIMoveSpeed()F")
            )
    )
    public void whs_fireLivingJumpEvent(float strafe, float vertical, float forward, CallbackInfo ci){
        LivingEvent.LivingJumpEvent event = new LivingEvent.LivingJumpEvent(this);
        ((ISkipsPotionCoreHandler) event).whs$setSkipsPotionCore();
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    @Nonnull
    //This allows protection / FF
    public Iterable<ItemStack> getArmorInventoryList() { //TODO: this can probably be done less stupidly. idk man, horsechest would need to be removed or smth. or shadowed
        //Using horse armor twice since it protects both head + chest
        if(!this.world.isRemote)
            return Arrays.asList(this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(2));
        else {
            ItemStack armor = (AbstractHorse)(Object) this instanceof EntityHorse ? this.getDataManager().get(HorseArmorAccessor.getArmorStack()) : ItemStack.EMPTY;
            ItemStack shoes = ((IHorseStackGetter) this).whs$getHorseshoesStack();
            return Arrays.asList(armor, armor, shoes);
        }
    }
}

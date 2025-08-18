package wornhorseshoes.mixin.vanilla.enchantments;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        switch (slotIn) {
            case HEAD: case CHEST: //protects both
                return this.horseChest.getStackInSlot(1);
            case FEET:
                return this.horseChest.getStackInSlot(2);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Inject(
            method = "travel",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/AbstractHorse;jumpPower:F", ordinal = 5)
    )
    public void whs_fireLivingJumpEvent(float strafe, float vertical, float forward, CallbackInfo ci){
        //TODO why no work
        ForgeHooks.onLivingJump(this);
    }

    @Override
    @Nonnull
    //This allows protection / FF
    public Iterable<ItemStack> getArmorInventoryList() { //TODO: this can probably be done less stupidly. idk man, horsechest would need to be removed or smth. or shadowed
        //Using horse armor twice since it protects both head + chest
        return Arrays.asList(this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(1), this.horseChest.getStackInSlot(2));
    }
}

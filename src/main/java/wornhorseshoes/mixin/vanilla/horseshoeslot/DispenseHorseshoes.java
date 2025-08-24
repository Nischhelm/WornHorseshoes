package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.item.ItemHorseArmor;
import wornhorseshoes.item.ItemHorseshoes;

import java.util.List;

@Mixin(ItemArmor.class)
public abstract class DispenseHorseshoes {
    @Inject(
            method = "dispenseArmor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void whs_dispenseHorseshoes(IBlockSource blockSource, ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        Item item = stack.getItem();
        if(!(item instanceof ItemHorseshoes || item instanceof ItemHorseArmor)) return;

        BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().getValue(BlockDispenser.FACING));
        List<AbstractHorse> list = blockSource.getWorld().getEntitiesWithinAABB(AbstractHorse.class, new AxisAlignedBB(blockpos));

        if (list.isEmpty()) cir.setReturnValue(ItemStack.EMPTY);
        else {
            if (item instanceof ItemHorseshoes) {
                AbstractHorse horse = list.stream().filter(HorseshoesConfig::canShoeHorse).findFirst().orElse(null);
                if(horse == null) return;
                ItemStack equipStack = stack.splitStack(1);
                ContainerHorseChest horseChest = ((AbstractHorseAccessor) horse).getHorseChest();
                horseChest.setInventorySlotContents(2, equipStack);

                cir.setReturnValue(stack);
            } else if(item instanceof  ItemHorseArmor){
                EntityHorse horse = (EntityHorse) list.stream().filter(h -> h instanceof EntityHorse).findFirst().orElse(null);
                if(horse == null) return;
                ItemStack equipStack = stack.splitStack(1);
                ContainerHorseChest horseChest = ((AbstractHorseAccessor) horse).getHorseChest();
                horseChest.setInventorySlotContents(1, equipStack);
                horse.setHorseArmorStack(equipStack);

                cir.setReturnValue(stack);
            }
        }
    }
}

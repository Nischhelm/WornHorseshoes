package wornhorseshoes.mixin.vanilla.horseshoeslot;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
        if(!(stack.getItem() instanceof ItemHorseshoes)) return;

        BlockPos blockpos = blockSource.getBlockPos().offset(blockSource.getBlockState().getValue(BlockDispenser.FACING));
        List<AbstractHorse> list = blockSource.getWorld().getEntitiesWithinAABB(AbstractHorse.class, new AxisAlignedBB(blockpos));

        if (list.isEmpty()) cir.setReturnValue(ItemStack.EMPTY);

        else {
            AbstractHorse horse = list.get(0);
            ItemStack equipStack = stack.splitStack(1);
            ContainerHorseChest horseChest = ((AbstractHorseAccessor) horse).getHorseChest();
            horseChest.setInventorySlotContents(horseChest.getSizeInventory()-1, equipStack);

            cir.setReturnValue(stack);
        }
    }
}

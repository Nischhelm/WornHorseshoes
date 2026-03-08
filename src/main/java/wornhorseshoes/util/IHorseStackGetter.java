package wornhorseshoes.util;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public interface IHorseStackGetter {
    ItemStack whs$getSaddleStack();
    ItemStack whs$getHorseshoesStack();

    DataParameter<ItemStack> SADDLE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);
    DataParameter<ItemStack> HORSESHOE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);
    static void registerDataParameters(){} //this is just to trigger the static <clinit> block here at the correct time

}

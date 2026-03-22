package wornhorseshoes.util;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import wornhorseshoes.mixin.vanilla.renderenchantedlayers.armor.HorseArmorAccessor;

import javax.annotation.Nullable;

public interface IHorseStackGetter {
    ItemStack whs$getSaddleStack();
    ItemStack whs$getHorseshoesStack();
    @Nullable ResourceLocation whs$getArmorTexture();

    void whs$setArmorTexture(@Nullable ResourceLocation loc);

    DataParameter<ItemStack> SADDLE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);
    DataParameter<ItemStack> HORSESHOE_STACK = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.ITEM_STACK);

    DataParameter<ItemStack> ZHORSE_ARMOR_STACK = EntityDataManager.createKey(EntityZombieHorse.class, DataSerializers.ITEM_STACK);
    DataParameter<ItemStack> SHORSE_ARMOR_STACK = EntityDataManager.createKey(EntitySkeletonHorse.class, DataSerializers.ITEM_STACK);

    static void registerDataParameters(){} //this is just to trigger the static <clinit> block here at the correct time

    static DataParameter<ItemStack> getArmorParameter(AbstractHorse horse) {
        if(horse instanceof EntityHorse) {
            return HorseArmorAccessor.getArmorStack();
        } else if(horse instanceof EntitySkeletonHorse) {
            return SHORSE_ARMOR_STACK;
        } else if(horse instanceof EntityZombieHorse) {
            return ZHORSE_ARMOR_STACK;
        }
        return null;
    }

    static ItemStack getArmorStack(AbstractHorse horse) {
        DataParameter<ItemStack> param = getArmorParameter(horse);
        return param == null ? ItemStack.EMPTY : horse.getDataManager().get(param);
    }

    static void setArmorStack(AbstractHorse horse, ItemStack stack) {
        DataParameter<ItemStack> param = getArmorParameter(horse);
        if(param != null) horse.getDataManager().set(param, stack);
    }
}

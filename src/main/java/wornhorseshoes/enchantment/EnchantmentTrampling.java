package wornhorseshoes.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.item.ItemHorseshoes;

public class EnchantmentTrampling extends Enchantment {
    public static EnchantmentTrampling INSTANCE = new EnchantmentTrampling();

    public EnchantmentTrampling() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
        this.setRegistryName(WornHorseshoes.MODID, "trampling");
        this.setName("trampling");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ItemHorseshoes;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ItemHorseshoes;
    }
}

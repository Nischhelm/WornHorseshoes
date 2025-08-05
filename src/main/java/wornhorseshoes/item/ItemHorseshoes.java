package wornhorseshoes.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import wornhorseshoes.WornHorseshoes;

import javax.annotation.Nonnull;

public class ItemHorseshoes extends ItemArmor {
    public ItemHorseshoes(ItemArmor.ArmorMaterial material) {
        super(material, 0, EntityEquipmentSlot.FEET);
        setRegistryName(WornHorseshoes.MODID,"horseshoes_"+material.getName());
        this.setTranslationKey("horseshoes_"+material.getName());
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, Enchantment enchantment) {
        return enchantment.type == EnumEnchantmentType.ARMOR_FEET;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stackInHand = playerIn.getHeldItem(handIn);
        return new ActionResult<>(EnumActionResult.FAIL, stackInHand);
    }

    @Override
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot slot, @Nonnull Entity entity) {
        return entity instanceof AbstractHorse && slot == EntityEquipmentSlot.FEET;
    }
}

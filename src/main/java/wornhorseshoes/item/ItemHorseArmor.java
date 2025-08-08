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

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ItemHorseArmor extends ItemArmor {
    public ItemHorseArmor(ArmorMaterial material) {
        super(material, 0, EntityEquipmentSlot.CHEST);

        String itemName, translationKey;
        switch (material){
            case IRON:
                itemName = "iron_horse_armor";
                translationKey = "horsearmormetal";
                break;
            case GOLD:
                itemName = "golden_horse_armor";
                translationKey = "horsearmorgold";
                break;
            case DIAMOND: default:
                itemName = "diamond_horse_armor";
                translationKey = "horsearmordiamond";
                break;
        }

        setRegistryName("minecraft", itemName);
        this.setTranslationKey(translationKey);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stackInHand = playerIn.getHeldItem(handIn);
        return new ActionResult<>(EnumActionResult.FAIL, stackInHand);
    }

    @Override
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot slot, @Nonnull Entity entity) {
        return entity instanceof AbstractHorse && (slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, @Nonnull Enchantment enchantment) {
        if (!(stack.getItem() instanceof ItemHorseArmor)) return false; //just safety
        //Allow armor, head and chest enchants
        return Arrays.asList(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.ARMOR_HEAD).contains(enchantment.type);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}

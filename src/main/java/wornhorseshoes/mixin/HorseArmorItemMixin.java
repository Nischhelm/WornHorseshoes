package wornhorseshoes.mixin;

import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;

/**
 * Horse armor items:
 * <ul>
 * <li>armor value from [horse_armor] armorStats (vanilla's getProtection is what every horse type's armor bonus and
 *     the tooltip read);</li>
 * <li>enchantable at the table and anvil with helmet and chestplate enchantments, with the enchantability of the
 *     matching armor material (vanilla horse armor can't be enchanted at all).</li>
 * </ul>
 */
@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item {

    public HorseArmorItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getProtection", at = @At("RETURN"), cancellable = true)
    private void whs$configuredArmor(CallbackInfoReturnable<Integer> cir) {
        WHSConfig.ensureLoaded();
        WHSConfig.ArmorStats stats = WHSConfig.armorStats.get((Item) this);
        if (stats != null) cir.setReturnValue((int) Math.round(stats.armor()));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return WHSConfig.HORSE_ARMOR_ENCHANTABLE.get() && stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        // same enchantability as the armor material of the same metal
        Item self = this;
        if (self == Items.GOLDEN_HORSE_ARMOR) return 25;
        if (self == Items.DIAMOND_HORSE_ARMOR) return 10;
        if (self == Items.LEATHER_HORSE_ARMOR) return 15;
        return 9;   // iron and modded horse armor
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (!WHSConfig.HORSE_ARMOR_ENCHANTABLE.get()) return false;
        WHSConfig.ensureLoaded();
        if (WHSConfig.armorBlacklist.contains(enchantment)) return false;
        // any armor, helmet or chestplate enchantment (horse armor covers the head and body)
        EnchantmentCategory category = enchantment.category;
        return category == EnchantmentCategory.ARMOR || category == EnchantmentCategory.ARMOR_HEAD || category == EnchantmentCategory.ARMOR_CHEST;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return WHSConfig.HORSE_ARMOR_ENCHANTABLE.get();
    }
}

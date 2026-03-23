package wornhorseshoes.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.handlers.RegistrationHandler;
import wornhorseshoes.mixin.vanilla.accessors.HorseInventoryAccessor;

import java.util.Random;

public class HorseEquipUtil {
    public static void equipHorse(AbstractHorse horse, float localDifficulty) {
        //Basically vanilla mob armor algorithm, slightly modified
        int tier = horse.getRNG().nextInt(2);
        for (int i = 0; i < 3; i++) if (horse.getRNG().nextFloat() < 0.095F) ++tier;

        ContainerHorseChest inventory = ((HorseInventoryAccessor) horse).getHorseChest();

        if (horse.getRNG().nextFloat() < ModConfigHandler.undead.maxEquipChance * localDifficulty) {
            ItemStack armor = new ItemStack(getEquipmentByTier(true, tier));
            armor = setEnchantmentBasedOnDifficulty(armor, horse.getRNG(), localDifficulty);
            inventory.setInventorySlotContents(1, armor);
            IHorseStackGetter.setArmorStack(horse, armor);
        }

        if (horse.getRNG().nextFloat() < ModConfigHandler.undead.maxEquipChance * localDifficulty) {
            ItemStack shoes = new ItemStack(getEquipmentByTier(false, tier));
            shoes = setEnchantmentBasedOnDifficulty(shoes, horse.getRNG(), localDifficulty);
            inventory.setInventorySlotContents(2, shoes);
            horse.getDataManager().set(IHorseStackGetter.HORSESHOE_STACK, shoes);
        }
    }

    public static Item getEquipmentByTier(boolean isArmor, int tier) {
        if (isArmor) {
            switch (tier) {
                //TODO: tier 0 leather armor by mobs of mayhem backport
                case 1: return Items.GOLDEN_HORSE_ARMOR;
                case 2: case 3: return Items.IRON_HORSE_ARMOR;
                case 4: return Items.DIAMOND_HORSE_ARMOR;
            }
        } else {
            switch (tier) {
                case 1: return RegistrationHandler.GOLD_HORSESHOE;
                case 2: case 3: return RegistrationHandler.IRON_HORSESHOE;
                case 4: return RegistrationHandler.DIAMOND_HORSESHOE;
            }
        }
        return Items.AIR;
    }

    protected static ItemStack setEnchantmentBasedOnDifficulty(ItemStack stack, Random rand, float localDifficulty) {
        if (!stack.isEmpty() && (rand.nextFloat() < ModConfigHandler.undead.maxEquipEnchantChance * localDifficulty)) {
            int enchLvl = MathHelper.getInt(rand, ModConfigHandler.undead.minEnchLvl, ModConfigHandler.undead.minEnchLvl + (int) (localDifficulty * (ModConfigHandler.undead.maxEnchLvl - ModConfigHandler.undead.minEnchLvl)));
            return EnchantmentHelper.addRandomEnchantment(rand, stack, enchLvl, ModConfigHandler.undead.allowTreasure);
        }
        return stack;
    }
}

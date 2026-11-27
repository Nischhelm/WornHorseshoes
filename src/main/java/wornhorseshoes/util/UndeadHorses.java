package wornhorseshoes.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.registry.ModRegistry;

/** Gear and despawning for undead horses (zombie riders' horses, skeleton trap horses). */
public final class UndeadHorses {

    // persistent-data flag: this undead horse despawns like a monster
    private static final String CAN_DESPAWN = "WHS_CanDespawn";

    private UndeadHorses() {}

    public static boolean canDespawn(AbstractHorse horse) {
        return WHSConfig.ZOMBIE_HORSES_DESPAWN.get() && horse.getPersistentData().getBoolean(CAN_DESPAWN);
    }

    public static void setCanDespawn(AbstractHorse horse) {
        horse.getPersistentData().putBoolean(CAN_DESPAWN, true);
    }

    /**
     * Maybe gives the horse armor and horseshoes, like vanilla mob gear: a random tier, a chance that grows with local
     * difficulty (clamped 0..1), and maybe enchanted.
     */
    public static void equip(AbstractHorse horse, float localDifficulty) {
        RandomSource random = horse.getRandom();
        int tier = random.nextInt(2);
        for (int i = 0; i < 3; i++) if (random.nextFloat() < 0.095F) tier++;
        double chance = WHSConfig.MAX_EQUIP_CHANCE.get() * localDifficulty;

        if (horse.canWearArmor() && random.nextFloat() < chance) {
            ItemStack armor = enchantMaybe(new ItemStack(byTier(true, tier)), random, localDifficulty);
            if (!armor.isEmpty()) horse.getSlot(401).set(armor);   // the armor inventory slot (updates CHEST + bonus)
        }
        if (WHSConfig.canShoe(horse) && random.nextFloat() < chance) {
            ItemStack shoes = enchantMaybe(new ItemStack(byTier(false, tier)), random, localDifficulty);
            if (!shoes.isEmpty()) HorseGear.setShoes(horse, shoes);
        }
    }

    private static Item byTier(boolean armor, int tier) {
        return switch (tier) {
            case 1 -> armor ? Items.GOLDEN_HORSE_ARMOR : ModRegistry.GOLD_HORSESHOES.get();
            case 2, 3 -> armor ? Items.IRON_HORSE_ARMOR : ModRegistry.IRON_HORSESHOES.get();
            case 4 -> armor ? Items.DIAMOND_HORSE_ARMOR : ModRegistry.DIAMOND_HORSESHOES.get();
            default -> Items.AIR;   // tier 0: nothing
        };
    }

    private static ItemStack enchantMaybe(ItemStack stack, RandomSource random, float localDifficulty) {
        if (stack.isEmpty() || random.nextFloat() >= WHSConfig.EQUIP_ENCHANT_MAX_CHANCE.get() * localDifficulty) return stack;
        int min = WHSConfig.EQUIP_ENCHANT_MIN_LVL.get();
        int level = Mth.nextInt(random, min, min + (int) (localDifficulty * (WHSConfig.EQUIP_ENCHANT_MAX_LVL.get() - min)));
        return EnchantmentHelper.enchantItem(random, stack, level, WHSConfig.EQUIP_ALLOW_TREASURE.get());
    }
}

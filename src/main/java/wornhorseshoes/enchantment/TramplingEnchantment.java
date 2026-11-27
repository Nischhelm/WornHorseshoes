package wornhorseshoes.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.registry.ModRegistry;
import wornhorseshoes.util.HorseGear;

import java.util.List;

/**
 * Trampling (horseshoes only): a ridden horse that is galloping or rearing knocks back and hurts the mobs it runs into,
 * and isn't knocked back by them itself. Off by default ([enchantments.trampling] enabled): while off it can't be found
 * on tables, books or trades and does nothing.
 */
public class TramplingEnchantment extends Enchantment {

    public TramplingEnchantment() {
        // rarity is read once at registration (vanilla enchantments can't change it later either)
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    private static boolean enabled() {
        return WHSConfig.TRAMPLING_ENABLED.get();
    }

    @Override
    public Rarity getRarity() {
        return WHSConfig.TRAMPLING_RARITY.get();
    }

    @Override
    public int getMaxLevel() {
        return WHSConfig.TRAMPLING_MAX_LVL.get();
    }

    @Override
    public int getMinCost(int level) {
        List<? extends Integer> e = WHSConfig.TRAMPLING_ENCHANTABILITY.get();
        return e.get(0) + e.get(1) * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + WHSConfig.TRAMPLING_ENCHANTABILITY.get().get(2);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        WHSConfig.ensureLoaded();
        return !WHSConfig.tramplingIncompatible.contains(other) && super.checkCompatibility(other);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return enabled() && HorseGear.isHorseshoe(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return enabled() && HorseGear.isHorseshoe(stack);
    }

    @Override
    public boolean isDiscoverable() {
        return enabled();
    }

    @Override
    public boolean isTradeable() {
        return enabled();
    }

    @Override
    public boolean isAllowedOnBooks() {
        return enabled();
    }

    /** Trampling level of the horse's horseshoes, 0 if off or none. */
    private static int level(AbstractHorse horse) {
        if (!enabled()) return 0;
        return EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.TRAMPLING.get(), horse.getItemBySlot(EquipmentSlot.FEET));
    }

    /** Charging = ridden and galloping for a few steps, or rearing. */
    private static boolean charging(AbstractHorse horse, int gallopTime, boolean rearing) {
        return horse.isVehicle() && (gallopTime > 3 || rearing);
    }

    /** Knocks back and hurts a mob the charging horse touched (called from the horse's push). */
    public static void onCollide(AbstractHorse horse, Entity entity, int gallopTime, boolean rearing) {
        if (horse.level().isClientSide || !(entity instanceof LivingEntity victim)) return;
        if (!(gallopTime > 3 || rearing) || horse.hasPassenger(entity)) return;
        int lvl = level(horse);
        if (lvl <= 0) return;
        double angle = knockbackAngle(horse, victim, lvl, rearing);
        float strength = (float) ((rearing ? 0.5 : 1.0) * WHSConfig.TRAMPLING_KNOCKBACK.get() * lvl);
        float damage = (float) ((rearing ? 1.5 : 1.0) * WHSConfig.TRAMPLING_DAMAGE.get() * lvl);
        victim.knockback(strength, -Math.sin(angle), -Math.cos(angle));
        victim.hurt(horse.damageSources().mobAttack(horse), damage);
    }

    /** True if the horse shouldn't be knocked back right now (charging with Trampling). */
    public static boolean resistsKnockback(AbstractHorse horse, int gallopTime, boolean rearing) {
        return charging(horse, gallopTime, rearing) && level(horse) > 0;
    }

    /**
     * Direction to throw the victim: straight away from a rearing horse, otherwise along the horse's heading, bent
     * towards the victim's side by at most 15 degrees per missing level (the 1.12 mod's formula).
     */
    private static double knockbackAngle(AbstractHorse horse, LivingEntity victim, int lvl, boolean rearing) {
        double tau = 2 * Math.PI;
        double toVictim = Math.atan2(victim.getX() - horse.getX(), victim.getZ() - horse.getZ()) + tau;
        if (rearing) return toVictim;
        double heading = -horse.getYRot() / 180.0 * Math.PI;
        double diff = (toVictim - heading + tau) % tau;
        if (diff > Math.PI) diff -= tau;
        double limit = 15.0 / 180.0 * Math.PI * (4 - lvl);
        return heading + Mth.clamp(diff, -limit, limit);
    }
}

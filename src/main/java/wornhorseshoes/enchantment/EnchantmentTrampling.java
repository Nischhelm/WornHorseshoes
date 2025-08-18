package wornhorseshoes.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Unique;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.config.folders.EnchantmentConfig;
import wornhorseshoes.item.ItemHorseshoes;

import javax.annotation.Nonnull;

public class EnchantmentTrampling extends Enchantment {
    public static EnchantmentTrampling INSTANCE = new EnchantmentTrampling();

    public EnchantmentTrampling() {
        super(ModConfigHandler.enchants.trampling.rarity, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET});
        this.setRegistryName(WornHorseshoes.MODID, "trampling");
        this.setName("trampling");
    }

    public static void handleCollision(AbstractHorse horse, Entity entityIn, int gallopTime, boolean rearing) {
        if(horse.world.isRemote) return;
        if(gallopTime <= 3 && !rearing) return;
        if(!(entityIn instanceof EntityLivingBase)) return;
        if(horse.getPassengers().contains(entityIn)) return;

        int tramplingLvl = EnchantmentHelper.getMaxEnchantmentLevel(EnchantmentTrampling.INSTANCE, horse);
        if(tramplingLvl <= 0) return;

        EntityLivingBase victim = (EntityLivingBase) entityIn;

        double knockbackAngle = whs$getKnockbackAngle(horse, victim, tramplingLvl);
        float knockbackStrength = (horse.isRearing() ? 0.5F : 1.0F) * ModConfigHandler.enchants.trampling.knockbackAmount * tramplingLvl;
        float dmgAmount = (horse.isRearing() ? 1.5F : 1.0F) * ModConfigHandler.enchants.trampling.damage * tramplingLvl;
        victim.knockBack(horse, knockbackStrength, - Math.sin(knockbackAngle), - Math.cos(knockbackAngle));
        victim.attackEntityFrom(DamageSource.causeMobDamage(horse), dmgAmount);
    }

    @Unique
    private static double whs$getKnockbackAngle(AbstractHorse horse, EntityLivingBase victim, int tramplingLvl) {
        double TAU = 2 * Math.PI;
        double distAngle = (Math.atan2(victim.posX - horse.posX, victim.posZ - horse.posZ) + TAU); // PI to 3 PI
        if(horse.isRearing()) return distAngle;

        //maff be maffing
        double motionAngle = -horse.rotationYaw / 180 * Math.PI; //0 to 2 PI

        double angleDiff = (distAngle - motionAngle + TAU) % TAU; //0 to 2 PI
        if (angleDiff > Math.PI) angleDiff -= TAU; //-PI to PI

        double angleLimit = 15. / 180. * Math.PI * (4 - tramplingLvl);

        return motionAngle + MathHelper.clamp(angleDiff, -angleLimit, angleLimit);
    }

    @Override
    public int getMaxLevel() {
        return ModConfigHandler.enchants.trampling.maxLvl;
    }

    @Override
    public int getMinEnchantability(int lvl) {
        return ModConfigHandler.enchants.trampling.enchantability[0] + ModConfigHandler.enchants.trampling.enchantability[1] * (lvl - 1);
    }

    @Override
    public int getMaxEnchantability(int lvl) {
        return this.getMinEnchantability(lvl) + ModConfigHandler.enchants.trampling.enchantability[2];
    }

    @Override
    public boolean canApplyTogether(@Nonnull Enchantment other){
        if(EnchantmentConfig.TramplingEnchantmentConfig.incompatibleEnchants.contains(other)) return false;
        return super.canApplyTogether(other);
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

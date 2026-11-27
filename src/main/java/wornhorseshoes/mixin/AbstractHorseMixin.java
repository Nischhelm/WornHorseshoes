package wornhorseshoes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.enchantment.TramplingEnchantment;
import wornhorseshoes.util.HorseGear;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Horse changes that live on AbstractHorse:
 * <ul>
 * <li>horse armor for the types in [horse_armor] armorHorses (mules, skeleton and zombie horses by default): the
 *     armor slot shows in their screen, the armor is mirrored to the CHEST slot (so enchantments and rendering find
 *     it) and gives its armor bonus, and it's saved as "ArmorItem" like the horse's own;</li>
 * <li>armor toughness from [horse_armor] armorStats, for every horse type;</li>
 * <li>spawn stats from [horse_stats] spawnStats;</li>
 * <li>riders stay mounted under water when the horseshoes have Depth Strider;</li>
 * <li>the Trampling horseshoe enchantment.</li>
 * </ul>
 */
@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {

    // vanilla's horse armor modifier id, so a horse never gets the bonus twice
    @Unique private static final UUID WHS$ARMOR_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    @Shadow protected SimpleContainer inventory;
    @Shadow private int gallopSoundCounter;

    @Shadow public abstract boolean isStanding();

    protected AbstractHorseMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    /** True for horse types this mod gives armor to (the horse itself and llamas handle armor on their own). */
    @Unique
    private boolean whs$extraArmorType() {
        AbstractHorse self = (AbstractHorse) (Object) this;
        return !(self instanceof Horse) && !(self instanceof Llama) && WHSConfig.canArmor(self);
    }

    // ---- armor for more horse types ----

    @Inject(method = "canWearArmor", at = @At("RETURN"), cancellable = true)
    private void whs$canWearArmor(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && whs$extraArmorType()) cir.setReturnValue(true);
    }

    @Inject(method = "isArmor", at = @At("RETURN"), cancellable = true)
    private void whs$isArmor(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && whs$extraArmorType()) cir.setReturnValue(stack.getItem() instanceof HorseArmorItem);
    }

    @Inject(method = "updateContainerEquipment", at = @At("TAIL"))
    private void whs$equipArmor(CallbackInfo ci) {
        if (this.level().isClientSide) return;
        AbstractHorse self = (AbstractHorse) (Object) this;
        if (whs$extraArmorType()) {
            // what Horse.setArmorEquipment does for horses: mirror to CHEST and add the armor bonus
            ItemStack armor = this.inventory.getItem(1);
            ItemStack worn = armor.getItem() instanceof HorseArmorItem ? armor : ItemStack.EMPTY;
            this.setItemSlot(EquipmentSlot.CHEST, worn);
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);   // dropped from the inventory instead, like vanilla
            this.getAttribute(Attributes.ARMOR).removeModifier(WHS$ARMOR_UUID);
            int bonus = worn.getItem() instanceof HorseArmorItem horseArmor ? horseArmor.getProtection() : 0;
            if (bonus != 0) this.getAttribute(Attributes.ARMOR).addTransientModifier(
                    new AttributeModifier(WHS$ARMOR_UUID, "Horse armor bonus", bonus, AttributeModifier.Operation.ADDITION));
        }
        // configured toughness (Horse sets its armor after this runs, so HorseMixin does it for horses)
        if (!(self instanceof Horse)) HorseGear.applyToughness(self);
    }

    // armor item before a container change, to play the equip sound when it changes
    @Unique private net.minecraft.world.item.Item whs$armorBefore;

    @Inject(method = "containerChanged", at = @At("HEAD"))
    private void whs$rememberArmor(Container container, CallbackInfo ci) {
        this.whs$armorBefore = this.getItemBySlot(EquipmentSlot.CHEST).getItem();
    }

    @Inject(method = "containerChanged", at = @At("TAIL"))
    private void whs$armorSound(Container container, CallbackInfo ci) {
        // Horse plays this itself; the other types get the same sound when armor goes on
        if (!whs$extraArmorType()) return;
        ItemStack now = this.getItemBySlot(EquipmentSlot.CHEST);
        if (this.tickCount > 20 && !now.isEmpty() && now.getItem() != this.whs$armorBefore) this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void whs$saveArmor(CompoundTag tag, CallbackInfo ci) {
        if (whs$extraArmorType() && !this.inventory.getItem(1).isEmpty()) tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void whs$loadArmor(CompoundTag tag, CallbackInfo ci) {
        if (!whs$extraArmorType() || !tag.contains("ArmorItem", 10)) return;
        ItemStack armor = ItemStack.of(tag.getCompound("ArmorItem"));
        if (armor.getItem() instanceof HorseArmorItem) {
            this.inventory.setItem(1, armor);
            ((AbstractHorseInvoker) this).whs$updateContainerEquipment();
        }
    }

    // ---- spawn stats ----

    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;randomizeAttributes(Lnet/minecraft/util/RandomSource;)V"))
    private void whs$spawnStats(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data,
                                @Nullable CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (!WHSConfig.MODIFY_STATS.get()) return;
        WHSConfig.ensureLoaded();
        WHSConfig.SpawnStats stats = WHSConfig.spawnStats.get(this.getType());
        if (stats == null) return;
        // base + range * bell-shaped roll, like the 1.12 mod
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((stats.speedBase() + stats.speedRange() * whs$bell()) / 43.17);
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(stats.jumpBase() + stats.jumpRange() * whs$bell());
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((int) (stats.hpBase() + stats.hpRange() * whs$bell()));
        this.setHealth(this.getMaxHealth());
    }

    @Unique
    private double whs$bell() {
        return (this.random.nextFloat() + this.random.nextFloat() + this.random.nextFloat()) / 3.0;
    }

    // ---- Depth Strider horseshoes keep the rider mounted under water ----

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        if (WHSConfig.DEPTH_STRIDER_NO_DISMOUNT.get() && EnchantmentHelper.getDepthStrider(this) > 0) return true;
        return type.canRideVehicleUnder(this, rider);   // Forge's default
    }

    // ---- Trampling ----

    @Override
    protected void doPush(Entity entity) {
        super.doPush(entity);
        TramplingEnchantment.onCollide((AbstractHorse) (Object) this, entity, this.gallopSoundCounter, this.isStanding());
    }

    @Override
    public void knockback(double strength, double x, double z) {
        // a ridden horse charging with Trampling hooves isn't pushed back by what it runs over
        if (TramplingEnchantment.resistsKnockback((AbstractHorse) (Object) this, this.gallopSoundCounter, this.isStanding())) return;
        super.knockback(strength, x, z);
    }

    /**
     * Slot 100 ("armor.feet" in /item and loot functions) is where the horseshoes live. The vanilla slot access only takes
     * items whose equipment slot is FEET, and horseshoes report MAINHAND so players and armor stands can't wear them, so
     * horses get their own access: horseshoes or empty, and only for horses that can be shod. Uses setShoes so the
     * "always drop" chance is set the same way as from the inventory slot.
     */
    @Inject(method = "getSlot", at = @At("HEAD"), cancellable = true)
    private void whs$horseshoeSlotAccess(int slot, CallbackInfoReturnable<SlotAccess> cir) {
        if (slot != 100 + EquipmentSlot.FEET.getIndex()) return;
        AbstractHorse self = (AbstractHorse) (Object) this;
        if (!WHSConfig.canShoe(self)) { cir.setReturnValue(SlotAccess.NULL); return; }
        cir.setReturnValue(new SlotAccess() {
            @Override public ItemStack get() { return HorseGear.getShoes(self); }
            @Override public boolean set(ItemStack stack) {
                if (!stack.isEmpty() && !HorseGear.isHorseshoe(stack)) return false;
                HorseGear.setShoes(self, stack);
                return true;
            }
        });
    }
}

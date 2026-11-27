package wornhorseshoes.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import wornhorseshoes.WornHorseshoes;

import java.util.function.Supplier;

/**
 * Armor materials for horseshoes. Horseshoes give no armor and never break (durability 0); the material only supplies
 * what other game mechanics read from it: the enchantability (same as the matching vanilla armor material, so iron,
 * gold and diamond horseshoes enchant like iron, gold and diamond boots) and the anvil repair item.
 */
public enum HorseshoeMaterial implements ArmorMaterial {
    IRON("iron", 9, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD("gold", 25, SoundEvents.ARMOR_EQUIP_GOLD, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND("diamond", 10, SoundEvents.ARMOR_EQUIP_DIAMOND, () -> Ingredient.of(Items.DIAMOND));

    private final String name;
    private final int enchantability;
    private final SoundEvent sound;
    private final Supplier<Ingredient> repair;

    HorseshoeMaterial(String name, int enchantability, SoundEvent sound, Supplier<Ingredient> repair) {
        this.name = WornHorseshoes.MODID + ":horseshoes_" + name;
        this.enchantability = enchantability;
        this.sound = sound;
        this.repair = repair;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return 0;   // never breaks (the item isn't damageable)
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return 0;   // horseshoes change speed and jump, not armor
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repair.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}

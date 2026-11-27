package wornhorseshoes.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.item.HorseshoesItem;

import javax.annotation.Nullable;

/**
 * Where a horse's gear lives. Horseshoes are the horse's FEET equipment slot: the game saves it (ArmorItems), syncs it
 * to clients, applies the item's attribute modifiers and lets boot enchantments find it, all without extra code. Horse
 * armor stays in inventory slot 1 like vanilla and is mirrored to the CHEST slot (vanilla does that for horses; this mod
 * does it for the other types that can wear armor).
 */
public final class HorseGear {

    // drop chance > 1 = always dropped on death and never damaged (what players expect from gear they put on)
    public static final float ALWAYS_DROP = 2.0F;
    private static final ResourceLocation LUCKY_TEXTURE = new ResourceLocation(WornHorseshoes.MODID, "textures/entity/horseshoes/horseshoes_gold.png");

    private HorseGear() {}

    /** True for this mod's horseshoes and the configured extra items (Lucky Horseshoe). */
    public static boolean isHorseshoe(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof HorseshoesItem) return true;
        WHSConfig.ensureLoaded();
        return WHSConfig.extraHorseshoes.contains(item);
    }

    /** True for configured extra items like Bountiful Curios' Lucky Horseshoe. */
    public static boolean isLuckyHorseshoe(ItemStack stack) {
        if (stack.isEmpty()) return false;
        WHSConfig.ensureLoaded();
        return WHSConfig.extraHorseshoes.contains(stack.getItem());
    }

    /** The horseshoes a horse wears (empty if none, or if its type can't wear any). */
    public static ItemStack getShoes(AbstractHorse horse) {
        if (!WHSConfig.canShoe(horse)) return ItemStack.EMPTY;
        ItemStack feet = horse.getItemBySlot(EquipmentSlot.FEET);
        return isHorseshoe(feet) ? feet : ItemStack.EMPTY;
    }

    /** Puts horseshoes on (or takes them off with EMPTY). */
    public static void setShoes(AbstractHorse horse, ItemStack stack) {
        horse.setItemSlot(EquipmentSlot.FEET, stack);
        horse.setDropChance(EquipmentSlot.FEET, ALWAYS_DROP);
    }

    /** Texture drawn on the hooves for a worn horseshoe item, or null. */
    @Nullable
    public static ResourceLocation shoesTexture(ItemStack stack) {
        if (stack.getItem() instanceof HorseshoesItem shoes) return shoes.getEntityTexture();
        return isLuckyHorseshoe(stack) ? LUCKY_TEXTURE : null;
    }

    /** The armor a horse wears (mirrored to the CHEST slot, so this works on clients too). */
    public static ItemStack getArmor(AbstractHorse horse) {
        return horse.getItemBySlot(EquipmentSlot.CHEST);
    }

    private static final java.util.UUID TOUGHNESS_UUID = java.util.UUID.fromString("7A1C2E6B-3F5D-4C8B-9A2E-6D1F0B3C4E5A");

    /** Sets the horse's armor toughness bonus from [horse_armor] armorStats for the armor it wears (server side). */
    public static void applyToughness(AbstractHorse horse) {
        net.minecraft.world.entity.ai.attributes.AttributeInstance attr = horse.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (attr == null) return;
        attr.removeModifier(TOUGHNESS_UUID);
        ItemStack armor = getArmor(horse);
        WHSConfig.ensureLoaded();
        WHSConfig.ArmorStats stats = armor.isEmpty() ? null : WHSConfig.armorStats.get(armor.getItem());
        if (stats != null && stats.toughness() != 0) attr.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                TOUGHNESS_UUID, "Horse armor toughness", stats.toughness(), net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
    }

    /** Jump height in blocks for the horse's jump strength (the 1.12 mod's quadratic fit of the game's jump). */
    public static double jumpHeight(AbstractHorse horse) {
        double jump = horse.getAttributeValue(Attributes.JUMP_STRENGTH);
        return -0.6238977 + 2.9998898 * jump + 2.8384039 * jump * jump;
    }

    /** Top speed in blocks per second (the speed attribute is about 1/43.17 of that). */
    public static double speed(AbstractHorse horse) {
        return horse.getAttributeValue(Attributes.MOVEMENT_SPEED) * 43.17;
    }
}

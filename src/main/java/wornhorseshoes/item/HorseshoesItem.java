package wornhorseshoes.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.WHSConfig;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Horseshoes. A horse wears them in its FEET equipment slot (filled through the horseshoe slot in its inventory
 * screen), which is what makes boot enchantments like Depth Strider, Frost Walker and Feather Falling work on it, and
 * makes the game apply this item's speed and jump modifiers like any worn armor's attributes.
 * <p>
 * It extends ArmorItem (boots) only so enchanting tables, anvils and enchantment categories treat it as boots. It can't
 * be worn by players or mobs: right-click does nothing, Forge's canEquip only allows a horse's FEET slot, and
 * getEquipmentSlot says MAINHAND so mobs that pick up loot don't put it on as boots.
 */
public class HorseshoesItem extends ArmorItem {

    // same UUID for every horseshoe: a horse only ever wears one pair, and swapping pairs replaces the modifier
    private static final UUID MODIFIER_UUID = UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes());

    private final ResourceLocation entityTexture;

    public HorseshoesItem(String name, HorseshoeMaterial material, Properties properties) {
        super(material, Type.BOOTS, properties.stacksTo(1));
        this.entityTexture = new ResourceLocation(WornHorseshoes.MODID, "textures/entity/horseshoes/" + name + ".png");
    }

    /** Texture drawn on the horse's hooves while worn. */
    public ResourceLocation getEntityTexture() {
        return this.entityTexture;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.fail(player.getItemInHand(hand));   // players can't wear horseshoes
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return entity instanceof AbstractHorse && armorType == EquipmentSlot.FEET;
    }

    @Override
    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.MAINHAND;   // keeps zombies & co from wearing them as boots
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.FEET) return ImmutableMultimap.of();
        WHSConfig.ensureLoaded();
        WHSConfig.ShoeStats stats = WHSConfig.shoeStats.get(this);
        if (stats == null) return ImmutableMultimap.of();   // no stats configured: no armor either
        return ImmutableMultimap.of(
                Attributes.MOVEMENT_SPEED, new AttributeModifier(MODIFIER_UUID, "Horseshoes speed", stats.speed().value(), stats.speed().op()),
                Attributes.JUMP_STRENGTH, new AttributeModifier(MODIFIER_UUID, "Horseshoes jump strength", stats.jump().value(), stats.jump().op()));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        // vanilla only lets damageable items be enchanted; horseshoes never break but should still enchant
        return WHSConfig.HORSESHOES_ENCHANTABLE.get();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (!WHSConfig.HORSESHOES_ENCHANTABLE.get()) return false;
        WHSConfig.ensureLoaded();
        if (WHSConfig.shoesBlacklist.contains(enchantment)) return false;
        return super.canApplyAtEnchantingTable(stack, enchantment);   // boot enchantments (category checks)
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return WHSConfig.HORSESHOES_ENCHANTABLE.get();
    }
}

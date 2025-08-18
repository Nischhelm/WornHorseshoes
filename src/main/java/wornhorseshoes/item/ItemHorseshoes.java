package wornhorseshoes.item;

import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.config.folders.EnchantmentConfig;
import wornhorseshoes.config.folders.HorseshoesConfig;
import wornhorseshoes.util.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class ItemHorseshoes extends ItemArmor {
    public ItemHorseshoes(ItemArmor.ArmorMaterial material) {
        super(material, 0, EntityEquipmentSlot.FEET);
        setRegistryName(WornHorseshoes.MODID,"horseshoes_"+material.getName());
        this.setTranslationKey("horseshoes_"+material.getName());
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
        return entity instanceof AbstractHorse && slot == EntityEquipmentSlot.FEET;
    }

    public void registerModel() {
        WornHorseshoes.proxy.registerItemRenderer(this, 0);
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        //No armor/toughness on horseshoes
        multimap.get(SharedMonsterAttributes.ARMOR.getName()).clear();
        multimap.get(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName()).clear();

        if (equipmentSlot == this.armorType) {
            Pair<Double, Double> modifiers = HorseshoesConfig.materialStats.get(this.getArmorMaterial().getName());
            if(modifiers != null) {
                multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Speed modifier", modifiers.getLeft(), 2));
                multimap.put("horse.jumpStrength", new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Jump Strength modifier", modifiers.getRight(), 2));
            }
        }

        return multimap;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    public ResourceLocation getHorseshoesTexture(EntityLiving wearer, ItemStack stack) {
        Item item = stack.getItem();
        if(!(item instanceof ItemHorseshoes)) return new ResourceLocation("");
        ItemHorseshoes horseshoes = (ItemHorseshoes) stack.getItem();
        switch (horseshoes.getArmorMaterial()){
            //TODO: cache on construction
            case DIAMOND: return new ResourceLocation("wornhorseshoes:textures/entity/horseshoes/horseshoes_diamond.png");
            case GOLD: return new ResourceLocation("wornhorseshoes:textures/entity/horseshoes/horseshoes_gold.png");
            case IRON: return new ResourceLocation("wornhorseshoes:textures/entity/horseshoes/horseshoes_iron.png");
        }
        return new ResourceLocation("");
    }

    @Override
    @Nullable
    public EntityEquipmentSlot getEquipmentSlot(@Nonnull ItemStack stack) {
        return EntityEquipmentSlot.MAINHAND; //Mainhand for zombies to pick it up but not wear it as armor (EntityLiving.updateEquipmentIfNeeded), default slot for all items
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment) {
        if (!ModConfigHandler.enchants.horseshoesEnchantable) return false;
        if (!(stack.getItem() instanceof ItemHorseshoes)) return false; //just safety
        if(EnchantmentConfig.horseshoesBlacklistSet.contains(enchantment)) return false;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
}

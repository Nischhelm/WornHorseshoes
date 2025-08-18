package wornhorseshoes.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.config.folders.EnchantmentConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public class ItemHorseArmor extends ItemArmor {
    private static final UUID ARMOR_UUID = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E");
    private static final ArmorMaterial nullMaterial = EnumHelper.addArmorMaterial("nullMaterial", "", 0, new int[]{0,0,0,0}, 0, null, 0);
    private final HorseArmorType horseArmorType;

    //For vanilla overwrite
    public ItemHorseArmor(String itemName, String translationKey, ArmorMaterial material) {
        super(material, 0, EntityEquipmentSlot.CHEST);

        horseArmorType = null; //do vanilla handling instead, for compat

        setRegistryName("minecraft", itemName);
        this.setTranslationKey(translationKey);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    //For modded horse armor
    public ItemHorseArmor(String name, String modName, HorseArmorType horseArmorType) {
        super(nullMaterial, 0, EntityEquipmentSlot.CHEST);

        this.horseArmorType = horseArmorType;
        this.setRegistryName(modName + ":" + name);
        this.setTranslationKey(modName + "." + name);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stackInHand = playerIn.getHeldItem(handIn);
        return new ActionResult<>(EnumActionResult.FAIL, stackInHand);
    }

    @Override
    public boolean isValidArmor(@Nonnull ItemStack stack, @Nonnull EntityEquipmentSlot slot, @Nonnull Entity entity) {
        return entity instanceof AbstractHorse && (slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment) {
        if (!ModConfigHandler.enchants.horseArmorEnchantable) return false;
        if (!(stack.getItem() instanceof ItemHorseArmor)) return false; //just safety
        if(EnchantmentConfig.horseArmorBlacklistSet.contains(enchantment)) return false;

        //Allow armor, head and chest enchants
        return Arrays.asList(EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.ARMOR_HEAD).contains(enchantment.type);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    @Nonnull
    public HorseArmorType getHorseArmorType(@Nonnull ItemStack stack) {
        if(this.horseArmorType == null)
            return super.getHorseArmorType(stack);
        return this.horseArmorType;
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (equipmentSlot == this.armorType) {
            //TODO: custom armor/toughness overrides via config? rn hardcoded for vanilla horse armor and/or via armor material
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_UUID, "Armor modifier", 0/*this.damageReduceAmount*/, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_UUID, "Armor toughness", 0/*this.toughness*/, 0));
        }

        return multimap;
    }

    @Override
    @Nullable
    public EntityEquipmentSlot getEquipmentSlot(@Nonnull ItemStack stack) {
        return EntityEquipmentSlot.MAINHAND; //Mainhand for zombies to pick it up but not wear it as armor (EntityLiving.updateEquipmentIfNeeded), default slot for all items
    }
}

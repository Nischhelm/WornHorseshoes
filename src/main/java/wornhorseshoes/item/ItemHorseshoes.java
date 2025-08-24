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

    private ResourceLocation textureLocation;

    // Not recommended. Horseshoes don't use the ArmorMaterial properties directly but other minecraft+modded mechanics do (example: enchanting)
    public ItemHorseshoes(String name) {
        this(name, ItemHorseArmor.nullMaterial);
    }

    public ItemHorseshoes(String name, ArmorMaterial material) {
        super(material, 0, EntityEquipmentSlot.FEET);
        setRegistryName(WornHorseshoes.MODID,name);
        this.setTranslationKey(name);
        this.setCreativeTab(CreativeTabs.MISC);
        setTextureLocation(new ResourceLocation(WornHorseshoes.MODID, "textures/entity/horseshoes/"+name+".png"));
    }

    public ItemHorseshoes setTextureLocation(ResourceLocation location){
        this.textureLocation = location;
        return this;
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
            Pair<HorseshoesConfig.Modifier, HorseshoesConfig.Modifier> modifiers = HorseshoesConfig.itemStats.get(this.getRegistryName().getPath());
            if(modifiers != null) {
                multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Speed modifier", modifiers.getLeft().value, modifiers.getLeft().operation));
                multimap.put("horse.jumpStrength", new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Jump Strength modifier", modifiers.getRight().value, modifiers.getRight().operation));
            }
        }

        return multimap;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    public static ResourceLocation getHorseshoesTexture(EntityLiving wearer, ItemStack stack) {
        Item item = stack.getItem();
        if(!(item instanceof ItemHorseshoes))
            return new ResourceLocation("");
        return ((ItemHorseshoes) item).textureLocation;
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

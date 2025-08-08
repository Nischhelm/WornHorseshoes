package wornhorseshoes.item;

import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import wornhorseshoes.WornHorseshoes;

import javax.annotation.Nonnull;
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

        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Speed modifier", 0.15, 2));
            multimap.put("horse.jumpStrength", new AttributeModifier(UUID.nameUUIDFromBytes("wornhorseshoes_modifier".getBytes()), "Jump Strength modifier", 0.15, 2));
        }

        return multimap;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}

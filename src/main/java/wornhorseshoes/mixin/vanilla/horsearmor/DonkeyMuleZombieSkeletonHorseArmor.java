package wornhorseshoes.mixin.vanilla.horsearmor;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import wornhorseshoes.mixin.vanilla.accessors.HorseArmorAccessor;
import wornhorseshoes.util.IHorseStackGetter;

@Mixin({EntityZombieHorse.class, EntitySkeletonHorse.class, AbstractChestHorse.class})
public abstract class DonkeyMuleZombieSkeletonHorseArmor extends AbstractHorse {
    //TODO: zombies spawn on despawnable zombie horses

    public DonkeyMuleZombieSkeletonHorseArmor(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void updateHorseSlots() {
        super.updateHorseSlots();

        if((AbstractHorse) this instanceof EntityLlama) return;
        ItemStack armorStack = this.horseChest.getStackInSlot(1);
        HorseArmorType horsearmortype = HorseArmorType.getByItemStack(armorStack);
        IHorseStackGetter.setArmorStack(this, armorStack);

        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(HorseArmorAccessor.getAmorModifierUUID());
            int armorVal = horsearmortype.getProtection();

            if (armorVal != 0)
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(HorseArmorAccessor.getAmorModifierUUID(), "Horse armor bonus", armorVal, 0)).setSaved(false));
        }
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        // Play equip sound
        Item armorBefore = IHorseStackGetter.getArmorStack(this).getItem();
        super.onInventoryChanged(invBasic);
        Item armorAfter = IHorseStackGetter.getArmorStack(this).getItem();

        if (this.ticksExisted > 20 && armorBefore != armorAfter && armorAfter != Items.AIR) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean wearsArmor() {
        return true; //TODO: config
    }

    @Override
    public boolean isArmor(ItemStack stack) {
        return HorseArmorType.isHorseArmor(stack); //TODO: config
    }
}

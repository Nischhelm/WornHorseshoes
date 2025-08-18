package wornhorseshoes.mixin.vanilla.horseencounters;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.handlers.RegistrationHandler;
import wornhorseshoes.mixin.vanilla.horseshoeslot.AbstractHorseAccessor;

import java.util.Random;

@Mixin(StructureVillagePieces.Church.class)
public abstract class ChurchEncounter extends StructureComponent {
    @Inject(method = "addComponentParts", at = @At("TAIL"))
    private void whs_encounterHorseshoedHorseRandomly(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, CallbackInfoReturnable<Boolean> cir){
        if(randomIn.nextFloat() >= ModConfigHandler.acquisition.wellEncounterChance / 100F) return;

        EntityHorse juan = new EntityHorse(worldIn);
        juan.setCustomNameTag("Juan");
        juan.setLocationAndAngles(this.getXWithOffset(2, 2) + 0.5D, this.getYWithOffset(10), this.getZWithOffset(2, 2) + 0.5D, 0.0F, 0.0F);

        /*
        lower 8 bits are color
        (horseVar & 0b11111111) % 0b111 == 0b11               //color
        higher 8 bits are marking
        ((horseVar & 0b1111111100000000) / 256) % 0b101 == 0  //markings

        actual variant: 0b(0000 0000)(0000 0011) = 3
         */
        juan.setHorseVariant(3);

        ItemStack shoes = new ItemStack(RegistrationHandler.GOLD_HORSESHOE);
        EnchantmentHelper.addRandomEnchantment(randomIn, shoes, 20, false);
        ((AbstractHorseAccessor) juan).getHorseChest().setInventorySlotContents(2, shoes);

        worldIn.spawnEntity(juan);
    }
}

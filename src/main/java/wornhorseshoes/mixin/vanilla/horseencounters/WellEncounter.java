package wornhorseshoes.mixin.vanilla.horseencounters;

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
import wornhorseshoes.config.folders.AcquisitionConfig;
import wornhorseshoes.handlers.RegistrationHandler;
import wornhorseshoes.item.ItemHorseshoes;
import wornhorseshoes.mixin.vanilla.horseshoeslot.AbstractHorseAccessor;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(StructureVillagePieces.Well.class)
public abstract class WellEncounter extends StructureComponent {
    @Inject(method = "addComponentParts", at = @At("TAIL"))
    private void whs_encounterHorseshoedHorseRandomly(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, CallbackInfoReturnable<Boolean> cir){
        if(randomIn.nextFloat() >= ModConfigHandler.acquisition.wellEncounterChance) return;

        EntityHorse horse = new EntityHorse(worldIn);
        horse.setLocationAndAngles(this.getXWithOffset(3, 0) + 0.5D, this.getYWithOffset(12), this.getZWithOffset(3, 0), 0.0F, 0.0F);

        List<ItemHorseshoes> shoes = RegistrationHandler.registeredHorseshoes.stream()
                .filter(item -> item.getRegistryName() != null && !AcquisitionConfig.wellEncounterBlacklistSet.contains(item.getRegistryName().toString()))
                .collect(Collectors.toList());

        ((AbstractHorseAccessor) horse).getHorseChest().setInventorySlotContents(2, new ItemStack(shoes.get(randomIn.nextInt(shoes.size()))));

        worldIn.spawnEntity(horse);
    }
}

package wornhorseshoes.handlers;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.config.folders.AcquisitionConfig;

public class HorseshoeAcquisition {
    public static void addHorseshoeTrade() {
        if(!ModConfigHandler.acquisition.leatherworkerSells) return;
        if(AcquisitionConfig.leatherWorkerTrades.isEmpty()) return;

        VillagerRegistry.VillagerProfession butcherProfession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:butcher"));
        if (butcherProfession == null){
            WornHorseshoes.LOGGER.warn("WornHorseshoes unable to find Leatherworker Profession. You won't be able to buy Horseshoes from Leatherworkers");
            return;
        }
        VillagerRegistry.VillagerCareer leatherWorkerCareer = butcherProfession.getCareer(1);

        leatherWorkerCareer.addTrade(3, (merchant, recipeList, random) -> {
            AcquisitionConfig.TradeEntry entry = WeightedRandom.getRandomItem(random, AcquisitionConfig.leatherWorkerTrades);

            ItemStack emeralds = new ItemStack(Items.EMERALD, MathHelper.getInt(random, entry.minPrice, entry.maxPrice), 0);
            ItemStack horseshoes = new ItemStack(entry.item);

            recipeList.add(new MerchantRecipe(emeralds, horseshoes));
        });
    }
}

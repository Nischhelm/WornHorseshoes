package wornhorseshoes.handlers;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import wornhorseshoes.WornHorseshoes;

public class HorseshoeAcquisition {
    public static void addHorseshoeTrade() {
        VillagerRegistry.VillagerProfession butcherProfession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:butcher"));
        if (butcherProfession == null){
            WornHorseshoes.LOGGER.warn("WornHorseshoes unable to find Leatherworker Profession. You won't be able to buy Horseshoes from Leatherworkers");
            return;
        }
        VillagerRegistry.VillagerCareer leatherWorkerCareer = butcherProfession.getCareer(1);

        int minPrice = 8;
        int maxPrice = 10;

        leatherWorkerCareer.addTrade(3, (merchant, recipeList, random) -> {
            int price = MathHelper.getInt(random, minPrice, maxPrice);

            ItemStack emeralds = new ItemStack(Items.EMERALD, price, 0);
            ItemStack horseshoes;

            //TODO: make this config-ready
            switch (random.nextInt(3)) {
                case 0: horseshoes = new ItemStack(RegistrationHandler.DIAMOND_HORSESHOE, 1); break;
                case 1: horseshoes = new ItemStack(RegistrationHandler.GOLD_HORSESHOE, 1); break;
                case 2: default: horseshoes = new ItemStack(RegistrationHandler.IRON_HORSESHOE, 1); break;
            }

            recipeList.add(new MerchantRecipe(emeralds, horseshoes));
        });
    }
}

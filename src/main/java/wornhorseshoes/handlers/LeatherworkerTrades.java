package wornhorseshoes.handlers;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wornhorseshoes.config.WHSConfig;

/** Journeyman (level 3) leatherworkers sell one pair of horseshoes, picked by weight from [acquisition]. */
public final class LeatherworkerTrades {

    private LeatherworkerTrades() {}

    @SubscribeEvent
    public static void onTrades(VillagerTradesEvent event) {
        if (event.getType() != VillagerProfession.LEATHERWORKER) return;
        event.getTrades().get(3).add(LeatherworkerTrades::offer);
    }

    /** Picks the trade when the villager gets it; null (no offer) if turned off or no valid entries. */
    private static MerchantOffer offer(net.minecraft.world.entity.Entity trader, RandomSource random) {
        if (!WHSConfig.LEATHERWORKER_SELLS.get()) return null;
        WHSConfig.ensureLoaded();
        int total = WHSConfig.trades.stream().mapToInt(WHSConfig.Trade::weight).sum();
        if (total <= 0) return null;
        int pick = random.nextInt(total);
        for (WHSConfig.Trade trade : WHSConfig.trades) {
            pick -= trade.weight();
            if (pick >= 0) continue;
            ItemStack price = new ItemStack(Items.EMERALD, Mth.nextInt(random, trade.minPrice(), trade.maxPrice()));
            ItemStack shoes = new ItemStack(trade.item());
            if (trade.minEnch() >= 0) {
                int max = trade.maxEnch() < 0 ? trade.minEnch() : trade.maxEnch();
                shoes = EnchantmentHelper.enchantItem(random, shoes, Mth.nextInt(random, trade.minEnch(), max), true);
            }
            // 7 uses (what 1.12 trades had), villager XP like other level-3 trades
            return new MerchantOffer(price, shoes, 7, 10, 0.05F);
        }
        return null;
    }
}

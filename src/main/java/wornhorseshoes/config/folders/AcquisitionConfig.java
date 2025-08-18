package wornhorseshoes.config.folders;

import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.item.ItemHorseshoes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcquisitionConfig {
    @Config.Comment("Global toggle for Horseshoe trade at Leatherworker Villager")
    @Config.Name("Leatherworker sells Horseshoes")
    public boolean leatherworkerSells = true;

    @Config.Comment("Trades defined per horseshoe. Each line consists of\n" +
            "itemid, minPrice, maxPrice, weight\n" +
            "Where the price is in emeralds and the weight defines how often this specific trade is chosen. Each leatherworker only has one horseshoe trade")
    @Config.Name("Leatherworker Horseshoe trades")
    public String[] leatherworkerTradeEntries = {
            "horseshoes_diamond, 8, 10, 1",
            "horseshoes_gold, 8, 10, 1",
            "horseshoes_iron, 8, 10, 1"
    };

    @Config.Comment("Chance to encounter an untamed horse wearing random horseshoes on any village well")
    @Config.Name("Well Encounter Chance")
    public float wellEncounterChance = 0.3F;

    @Config.Comment("All registered horseshoes have equal likelihood of being chosen for horses that generate on village wells. Disable specific horseshoe items by naming their itemids here.")
    @Config.Name("Well encounter blacklisted Horseshoes")
    public String[] wellEncounterBlacklist = {};

    public static void init() {
        for(String configLine : ModConfigHandler.acquisition.leatherworkerTradeEntries)
            new TradeEntry(configLine);


        for(String s : ModConfigHandler.acquisition.wellEncounterBlacklist){
            if(!s.contains(":")) wellEncounterBlacklistSet.add(WornHorseshoes.MODID+":"+s);
            else wellEncounterBlacklistSet.add(s);
        }
    }
    public static void reset() {
        leatherWorkerTrades.clear();
        wellEncounterBlacklistSet.clear();
    }

    public static final Set<String> wellEncounterBlacklistSet = new HashSet<>();
    public static final List<TradeEntry> leatherWorkerTrades = new ArrayList<>();

    public static class TradeEntry extends WeightedRandom.Item {
        public ItemHorseshoes item;
        public int minPrice, maxPrice;

        public TradeEntry(String configLine){
            super(0);
            String[] split = configLine.split(",");
            if(split.length != 4) return;

            String itemid = split[0].trim();
            if(!itemid.contains(":")) itemid = WornHorseshoes.MODID + ":" + itemid;

            try {
                this.item = (ItemHorseshoes) Item.getByNameOrId(itemid);
                if(this.item == null) return;
                this.minPrice = Integer.parseInt(split[1].trim());
                this.maxPrice = Integer.parseInt(split[2].trim());
                this.itemWeight = Integer.parseInt(split[3].trim());

                //add to list of valid entries
                leatherWorkerTrades.add(this);
            } catch (Exception e) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Leatherworker trade entry: {}", configLine);
            }
        }
    }
}

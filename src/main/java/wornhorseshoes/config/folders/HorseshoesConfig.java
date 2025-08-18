package wornhorseshoes.config.folders;

import net.minecraftforge.common.config.Config;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.ModConfigHandler;
import wornhorseshoes.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class HorseshoesConfig {

    //TODO: what mobs can get horseshoes

    //TODO register new horseshoes

    @Config.Comment("Stats per material for horseshoes. Pattern: materialName, speedMod, jumpBoostMod\n" +
            "By default uses operation2 (MULT_TOTAL), use @operationNumber (0/1/2) to change")
    @Config.Name("Horseshoe Material stats")
    public String[] materialStatsEntries = {
            "iron, 0.3, 0.3",
            "diamond, 0.2, 0.5",
            "gold, 0.5, 0.2"
    };

    public static void init() {
        for (String line : ModConfigHandler.horseshoes.materialStatsEntries) {
            String[] split = line.split(",");
            if (split.length != 3) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Material stats line {}, expected three entries", line);
                continue;
            }
            String matName = split[0].trim();
            try {
                double speedMod = Double.parseDouble(split[1].trim());
                double jumpMod = Double.parseDouble(split[2].trim());
                materialStats.put(matName, new Pair<>(speedMod, jumpMod));
            } catch (Exception e) {
                WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Material stats line {}, expected numbers in second and third entry", line);
            }
        }
    }
    public static void reset(){
        materialStats.clear();
    }

    public static final Map<String, Pair<Double, Double>> materialStats = new HashMap<>();
}

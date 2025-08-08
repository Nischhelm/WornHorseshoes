package wornhorseshoes.config;

import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class ModConfigProvider {
	public static void reset(){
		materialStats.clear();
		init();
	}

	public static void init(){
		initMaterialStats();
	}

	public static final Map<String, Pair<Double, Double>> materialStats = new HashMap<>();

	public static void initMaterialStats() {
		for(String line : ModConfigHandler.materialStats){
			String[] split = line.split(",");
			if(split.length != 3) {
				WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Material stats line {}, expected three entries", line);
				continue;
			}
			String matName = split[0].trim();
			try {
				double speedMod = Double.parseDouble(split[1].trim());
				double jumpMod = Double.parseDouble(split[2].trim());
				materialStats.put(matName, new Pair<>(speedMod, jumpMod));
			} catch (Exception e){
				WornHorseshoes.LOGGER.warn("WornHorseshoes unable to parse Material stats line {}, expected numbers in second and third entry", line);
			}
		}
	}

}
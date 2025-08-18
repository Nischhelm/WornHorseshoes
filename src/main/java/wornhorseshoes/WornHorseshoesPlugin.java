package wornhorseshoes;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class WornHorseshoesPlugin implements IFMLLoadingPlugin {

	public WornHorseshoesPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.json");
		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.horseshoesslot.json");

		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.enchantablesaddle.json"); //TODO: WIP, no enchants added yet
		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.horseelytra.json"); //TODO: WIP, no handling yet
		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.wellencounter.json"); //TODO: early config reader mixin disable

		FermiumRegistryAPI.enqueueMixin(true, "mixins.wornhorseshoes.bblsohmy.json", () -> Loader.isModLoaded("bblsom"));
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
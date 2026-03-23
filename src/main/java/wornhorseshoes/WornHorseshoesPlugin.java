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

		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.json"); //sync stack is pretty much the backbone of everything, needs to always be enabled
		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.accessors.json");
		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.rearingfix.json");

		FermiumRegistryAPI.enqueueMixin(false, "mixins.wornhorseshoes.vanilla.elytra.json"); //TODO: WIP, no handling yet

		FermiumRegistryAPI.enqueueMixin(true, "mixins.wornhorseshoes.bblsohmy.json", () -> Loader.isModLoaded("bblsom"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.wornhorseshoes.grapplemod.json", () -> Loader.isModLoaded("grapplemod"));
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
package wornhorseshoes.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.enchantment.TramplingEnchantment;
import wornhorseshoes.item.HorseshoeMaterial;
import wornhorseshoes.item.HorseshoesItem;

import java.util.List;

/** Items and enchantments (same registry names as the 1.12.2 mod). */
public final class ModRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WornHorseshoes.MODID);
    private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, WornHorseshoes.MODID);

    public static final RegistryObject<HorseshoesItem> IRON_HORSESHOES = horseshoes("horseshoes_iron", HorseshoeMaterial.IRON);
    public static final RegistryObject<HorseshoesItem> GOLD_HORSESHOES = horseshoes("horseshoes_gold", HorseshoeMaterial.GOLD);
    public static final RegistryObject<HorseshoesItem> DIAMOND_HORSESHOES = horseshoes("horseshoes_diamond", HorseshoeMaterial.DIAMOND);
    /** Every horseshoe item, in tier order (iron, gold, diamond). */
    public static final List<RegistryObject<HorseshoesItem>> HORSESHOES = List.of(IRON_HORSESHOES, GOLD_HORSESHOES, DIAMOND_HORSESHOES);

    // Always registered (an enchantment registry entry can't come and go with a config), but it does nothing and
    // can't be obtained while [enchantments.trampling] enabled is false.
    public static final RegistryObject<TramplingEnchantment> TRAMPLING = ENCHANTMENTS.register("trampling", TramplingEnchantment::new);

    private ModRegistry() {}

    private static RegistryObject<HorseshoesItem> horseshoes(String name, HorseshoeMaterial material) {
        return ITEMS.register(name, () -> new HorseshoesItem(name, material, new Item.Properties()));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        ENCHANTMENTS.register(modBus);
    }
}

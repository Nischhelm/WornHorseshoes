package wornhorseshoes.proxy;

import net.minecraft.item.Item;

public interface IProxy {
    default void registerItemRenderer(Item item, int meta) {};
}
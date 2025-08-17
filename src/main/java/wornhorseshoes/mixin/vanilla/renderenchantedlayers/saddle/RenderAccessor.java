package wornhorseshoes.mixin.vanilla.renderenchantedlayers.saddle;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Render.class)
public interface RenderAccessor<T extends Entity> {
    @Invoker(value = "getEntityTexture")
    ResourceLocation invokeGetEntityTexture(T entity);
}

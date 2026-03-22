package wornhorseshoes.mixin.modcompat.grapplemod;

import com.yyon.grapplinghook.CommonProxyClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CommonProxyClass.class)
public class CommonProxyClassMixin {
    @ModifyArg(
            method = "preInit",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ObfuscationReflectionHelper;findMethod(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"),
            remap = false
    )
    private static String findMethod(String srgName) {
        return "captureCurrentPosition"; // just for this mod working indev, but works in prod as well
    }
}

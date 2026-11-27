package wornhorseshoes.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.config.WHSConfig;
import wornhorseshoes.mixin.client.HorseInventoryScreenAccessor;

import java.text.DecimalFormat;

/** Client game-bus events: the horseshoe slot background, the stats line in the horse screen and horse armor tooltips. */
@Mod.EventBusSubscriber(modid = WornHorseshoes.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    private static final ResourceLocation HORSE_GUI = new ResourceLocation("textures/gui/container/horse.png");
    private static final ResourceLocation SHOE_SLOT = new ResourceLocation(WornHorseshoes.MODID, "textures/gui/container/horseshoes_background.png");
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private ClientEvents() {}

    /** Background (absolute screen coordinates): draw an empty slot and the horseshoe silhouette under the new slot. */
    @SubscribeEvent
    public static void onBackground(ContainerScreenEvent.Render.Background event) {
        if (!(event.getContainerScreen() instanceof HorseInventoryScreen screen)) return;
        AbstractHorse horse = ((HorseInventoryScreenAccessor) screen).whs$getHorse();
        if (!WHSConfig.canShoe(horse)) return;
        GuiGraphics g = event.getGuiGraphics();
        int x = screen.getGuiLeft() + 7, y = screen.getGuiTop() + 53;
        g.blit(HORSE_GUI, x, y, 0, screen.getYSize(), 18, 18);         // a plain slot from the chest grid texture
        g.blit(SHOE_SLOT, x, y, 0, 0, 18, 18);                          // horseshoe silhouette (256x256 sheet)
    }

    /** Foreground (already moved to the screen's top-left): "^jump" left and ">speed" right of the title row. */
    @SubscribeEvent
    public static void onForeground(ContainerScreenEvent.Render.Foreground event) {
        if (!WHSConfig.DRAW_STATS.get() || !(event.getContainerScreen() instanceof HorseInventoryScreen screen)) return;
        AbstractHorse horse = ((HorseInventoryScreenAccessor) screen).whs$getHorse();
        Font font = net.minecraft.client.Minecraft.getInstance().font;
        GuiGraphics g = event.getGuiGraphics();
        String jump = "^" + (int) wornhorseshoes.util.HorseGear.jumpHeight(horse);
        String speed = ">" + (int) wornhorseshoes.util.HorseGear.speed(horse);
        g.drawString(font, jump, 27, 19, 0xAAAAAA, false);
        g.drawString(font, speed, 78 - font.width(speed), 19, 0xAAAAAA, false);
    }

    /** Horse armor has no tooltip stats in vanilla; show what it gives a horse (protection is the configured value). */
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof HorseArmorItem armor)) return;
        WHSConfig.ensureLoaded();
        WHSConfig.ArmorStats stats = WHSConfig.armorStats.get(armor);
        double armorValue = stats != null ? stats.armor() : armor.getProtection();
        double toughness = stats != null ? stats.toughness() : 0;
        if (armorValue <= 0 && toughness <= 0) return;
        event.getToolTip().add(Component.empty());
        event.getToolTip().add(Component.translatable("wornhorseshoes.tooltip.on_horse").withStyle(ChatFormatting.GRAY));
        if (armorValue > 0) event.getToolTip().add(Component.translatable("attribute.modifier.plus.0", FORMAT.format(armorValue),
                Component.translatable("attribute.name.generic.armor")).withStyle(ChatFormatting.BLUE));
        if (toughness > 0) event.getToolTip().add(Component.translatable("attribute.modifier.plus.0", FORMAT.format(toughness),
                Component.translatable("attribute.name.generic.armor_toughness")).withStyle(ChatFormatting.BLUE));
    }
}

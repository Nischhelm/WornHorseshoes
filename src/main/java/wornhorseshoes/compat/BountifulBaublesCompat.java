package wornhorseshoes.compat;

import cursedflames.bountifulbaubles.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wornhorseshoes.WornHorseshoes;
import wornhorseshoes.util.IHorseStackGetter;

public class BountifulBaublesCompat {
    public static boolean isLuckyHorseshoe(Item item) {
        return item == ModItems.trinketLuckyHorseshoe;
    }

    public static final ResourceLocation luckyShoes = new ResourceLocation(WornHorseshoes.MODID, "textures/entity/horseshoes/horseshoes_gold.png");
    public static ResourceLocation getLuckyHorseshoeTexture(){
        return luckyShoes;
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        //All passengers and super-passengers of an abstract horse wearing lucky horseshoes will not take fall dmg
        if(event.getSource() != DamageSource.FALL) return;
        EntityLivingBase entity = event.getEntityLiving();
        if(entity == null) return;
        Entity lowestEntity = entity.getLowestRidingEntity();
        if(!(lowestEntity instanceof AbstractHorse)) return;
        AbstractHorse horse = (AbstractHorse) lowestEntity;
        if(isLuckyHorseshoe(((IHorseStackGetter) horse).whs$getHorseshoesStack().getItem()))
            event.setCanceled(true);
    }
}

package wornhorseshoes.util;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;

public class HorseStatUtil {
    public static double getJumpHeight(AbstractHorse horse) {
        double roundedJumpHeight = horse.getHorseJumpStrength();
        return -0.6238977 + 2.9998898 * roundedJumpHeight  + 2.8384039 * roundedJumpHeight * roundedJumpHeight; // approximating with a quadratic [0.1 : 0.1 : 1, 1.5] map to [1.125 1.5 2.125 2.875 3.625 4.375 5.25 10.25]
    }

    public static double getSpeed(AbstractHorse horse) {
        return horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 43.17; //apparently 1 speed is about 43.17 blocks per sec
    }
}

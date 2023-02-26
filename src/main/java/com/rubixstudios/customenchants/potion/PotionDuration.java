package com.rubixstudios.customenchants.potion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PotionDuration {
   private Map<PotionEffectType, PotionInfo> cache = new HashMap<>();

    /**
     * Add potion duration to cache
     * @param potionEffectType of the potion
     * @param duration of the potion
     * @param amplifier of the potion
     */
    public void addPotionDurationToCache(PotionEffectType potionEffectType, int duration, int amplifier){
        cache.put(potionEffectType, new PotionInfo(duration, amplifier));
    }

    /**
     * Does the potion already contains to the cache
     * @param potionEffectType of the potion
     * @return true or false
     */
    public boolean isPotionDurationInCache(PotionEffectType potionEffectType) {
        return cache.containsKey(potionEffectType);
    }

    /**
     * Get the potion from the cache
     * @param potionEffectType of the potion
     * @return PotionInfo
     */
    public PotionInfo getPotionDurationFromCache(PotionEffectType potionEffectType){
        if (!isPotionDurationInCache(potionEffectType)) return null;
       return cache.get(potionEffectType);
    }

    /**
     * Remove the potion from the cache
     * @param potionEffectType of the potion
     */
    public void removePotionDurationFromCache(PotionEffectType potionEffectType){
        this.cache.remove(potionEffectType);
    }

    @AllArgsConstructor @Getter
    public class PotionInfo {
        private int duration;
        private int amplifier;
    }
}

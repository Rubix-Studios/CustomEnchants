package com.rubixstudios.customenchants.utils;

import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

public class PotionUtil {

    public static MobEffect getMobEffect(final PotionEffectType potionEffectType, final LivingEntity livingEntity) {
        final CraftLivingEntity craftLivingEntity = (CraftLivingEntity) livingEntity;
        if (!craftLivingEntity.getHandle().effects.containsKey(potionEffectType.getId())) return null;
        return craftLivingEntity.getHandle().effects.get(potionEffectType.getId());
    }




}

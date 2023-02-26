package com.rubixstudios.customenchants.enchantments.customentity;

import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.Player;

public class PoisonArrow extends EntityArrow {

    private final @Getter Player shooter;
    private final @Getter
    CustomEnchantment customEnchantment;

    public PoisonArrow(World world, EntityLiving entityliving, float f, Player shooter, CustomEnchantment customEnchantment) {
        super(world, entityliving, f);

        this.shooter = shooter;
        this.customEnchantment = customEnchantment;
    }
}

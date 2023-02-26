package com.rubixstudios.customenchants.utils;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class DebugUtil {

    public static void broadcastEnchants(ItemStack itemStack) {
        for (Enchantment ench : itemStack.getEnchantments().keySet()) {
//            Bukkit.broadcastMessage("Enchantment: " + ench.getName());
        }
    }
}

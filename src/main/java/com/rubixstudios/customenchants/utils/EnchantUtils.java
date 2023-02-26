package com.rubixstudios.customenchants.utils;

import com.rubixstudios.customenchants.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;

import java.util.Arrays;

public class EnchantUtils {
    public static boolean hasArmorEnchant(Enchantment enchantment, ItemStack[] armorContents, ItemStack... ignoreItems){
        for (ItemStack armorContent : armorContents) {
            if (Arrays.asList(ignoreItems).contains(armorContent)) continue;
            if (armorContent.getEnchantments().containsKey(enchantment)) return true;
        }

        return false;
    }

    public static boolean hasItemStackEnchantment(final ItemStack itemStack, final Enchantment enchantment){
        if (itemStack.getEnchantments() == null || itemStack.getEnchantments().isEmpty()) return false;
        return itemStack.getEnchantments().containsKey(enchantment);

    }

    public static boolean hasItemMetaEnchantment(final ItemMeta itemMeta, final Enchantment enchantment){
        return itemMeta.getEnchants().containsKey(enchantment);
    }

    public static boolean isItemUnenchantable(final ItemStack itemStack){
        return hasItemStackEnchantment(itemStack, CustomEnchants.getInstance().getUnEnchantableEnch());
    }

    public static boolean enchantComparer(Enchantment enchantment, Enchantment enchantment2){
        return enchantment == enchantment2;
    }

    /**
     * Checks if item is a enchanted book or name tag and if it has a custom enchantment
     * @param itemStack
     * TODO Make the function name more readable it is a kinda bad name
     */
    public static boolean isBookOrNametagCustomEnchant(final ItemStack itemStack){
        if (itemStack.getType().equals(Material.ENCHANTED_BOOK) || itemStack.getType().equals(Material.NAME_TAG)) {
            for (Enchantment ench : itemStack.getEnchantments().keySet()) {
                if (ench instanceof CustomEnchantment) {
                    return true;
                }
            }
        }
        return false;
    }
}

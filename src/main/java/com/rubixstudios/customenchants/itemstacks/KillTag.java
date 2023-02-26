package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KillTag extends Item{

    public KillTag(){
        super("killtag");
    }

    @Override
    public ItemStack getItemStack() {
            final Enchantment ench = CustomEnchants.getInstance().getKillEnch();

            final ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            final ItemMeta meta = item.getItemMeta();
            final ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + ench.getName() + " " + StringUtils.getEnchantmentLevelName(ench.getMaxLevel()));
            lore.add(ChatColor.DARK_GREEN + "Use this to add kill counter");
            meta.setDisplayName(ChatColor.YELLOW + "Kill counter");
            meta.setLore(lore);
            item.setItemMeta(meta);
            item.addUnsafeEnchantment(ench, ench.getMaxLevel());
            return item;
    }
}

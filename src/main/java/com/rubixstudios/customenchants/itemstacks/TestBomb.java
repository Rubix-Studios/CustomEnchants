package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;

import java.util.List;

public class TestBomb extends Item{

    public TestBomb(){
        super("testbomb");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getTestBomb();
        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        final ItemStack item = new ItemStack(Material.DIAMOND);

        item.addUnsafeEnchantment(usageEnch, usageEnch.getMaxLevel());
        item.addUnsafeEnchantment(ench, ench.getMaxLevel());

        usageEnch.setMaxUses(3, item);

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Thomas " + ChatColor.DARK_RED + ChatColor.BOLD + "Test Bomb");


        final LoreBuilder builder = new LoreBuilder(item);

        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}

package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Thelep extends Item {
    public Thelep(){
        super("thelep");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getPoisonEnch();
        final Enchantment unenchant = CustomEnchants.getInstance().getUnEnchantableEnch();
        final UsageEnchantment usage = CustomEnchants.getInstance().getUsageEnch();

        final ItemStack item = new ItemStack(Material.STICK);

        item.addUnsafeEnchantment(ench, ench.getMaxLevel());
        item.addUnsafeEnchantment(unenchant, unenchant.getMaxLevel());
        item.addUnsafeEnchantment(usage, 1);

        usage.setMaxUses(40, item);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Thelep stick");


        final LoreBuilder builder = new LoreBuilder(item);
        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();
        lore.add("");
        lore.add(Color.translate("&7When you hit a player with this, he will"));
        lore.add(Color.translate("&7receive poison for 10 seconds."));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

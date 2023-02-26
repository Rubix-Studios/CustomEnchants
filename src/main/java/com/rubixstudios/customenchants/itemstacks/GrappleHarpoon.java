package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GrappleHarpoon extends Item{

    public GrappleHarpoon(){
        super("grappleharpoon");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getGrappleHarpoonEnch();
        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();
        final Enchantment unenchant = CustomEnchants.getInstance().getUnEnchantableEnch();

        final ItemStack item = new ItemStack(Material.BOW);

        item.addUnsafeEnchantment(ench, ench.getMaxLevel());
        item.addUnsafeEnchantment(usageEnch, 1);
        item.addUnsafeEnchantment(unenchant, 1);

        usageEnch.setMaxUses(64, item);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Grapple Harpoon");

        final LoreBuilder builder = new LoreBuilder(item);
        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();
        lore.add("");
        lore.add(Color.translate("&7When you shoot an arrow, you will be pulled"));
        lore.add(Color.translate("&7towards the arrow."));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}

package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BleedBomb extends Item{

    public BleedBomb(){
        super("bleedbomb");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getBleedBombEnchantment();
        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        final ItemStack item = new ItemStack(Material.BLAZE_POWDER);

        item.addUnsafeEnchantment(usageEnch, usageEnch.getMaxLevel());
        item.addUnsafeEnchantment(ench, ench.getMaxLevel());

        usageEnch.setMaxUses(3, item);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("" + ChatColor.DARK_RED + ChatColor.BOLD + "Bleed Bomb");

        final LoreBuilder builder = new LoreBuilder(item);
        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();
        lore.add("");
        lore.add(Color.translate("&7When you throw this bomb, the players inside"));
        lore.add(Color.translate("&7the 5 block range will receive bleed effects."));
        lore.add(Color.translate("&7You and your faction will do more damage on them."));

        meta.setLore(lore);

        meta.addEnchant(ItemUtils.FAKE_GLOW, 2, true);
        item.setItemMeta(meta);

        return item;
    }
}

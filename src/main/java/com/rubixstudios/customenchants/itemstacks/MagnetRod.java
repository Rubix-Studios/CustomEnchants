package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
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

public class MagnetRod extends Item{

    public MagnetRod(){
        super("magnetrod");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getMagnetEnch();
        final UsageEnchantment usage = CustomEnchants.getInstance().getUsageEnch();
        final Enchantment unenchant = CustomEnchants.getInstance().getUnEnchantableEnch();

        final ItemStack item = new ItemStack(Material.FISHING_ROD);

        item.addUnsafeEnchantment(ench, ench.getMaxLevel());
        item.addUnsafeEnchantment(usage, usage.getMaxLevel());
        item.addUnsafeEnchantment(unenchant, 1);

        usage.setMaxUses(32, item);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Magnet Rod");
        meta.addEnchant(ItemUtils.FAKE_GLOW, 2, true);

        final LoreBuilder builder = new LoreBuilder(item);
        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();
        lore.add("");
        lore.add(Color.translate("&7When you use this magnet rod on a player"));
        lore.add(Color.translate("&7he will be pulled towards you."));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}

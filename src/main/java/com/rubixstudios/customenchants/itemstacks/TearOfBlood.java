package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;

import java.util.List;

public class TearOfBlood extends Item{

    public TearOfBlood() {
        super("tearofblood");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getBleedEnch();
        final Enchantment unenchant = CustomEnchants.getInstance().getUnEnchantableEnch();

        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        final ItemStack item = new ItemStack(Material.SPIDER_EYE);

        item.addUnsafeEnchantment(unenchant, unenchant.getMaxLevel());
        item.addUnsafeEnchantment(usageEnch, 1);
        item.addUnsafeEnchantment(ench, ench.getMaxLevel());

        usageEnch.setMaxUses(32, item);


        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Tear of " + ChatColor.BOLD + "" + ChatColor.DARK_RED  + "Blood");
        meta.addEnchant(ItemUtils.FAKE_GLOW, 2, true);

        final LoreBuilder builder = new LoreBuilder(item);

        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();

        final int duration = CustomEnchants.getInstance().getConfig().getInt("blood-effect.seconds");

        lore.add("");
        lore.add(Color.translate("&7If you hit someone with this item you"));
        lore.add(Color.translate("&7will receive extra damage for <duration>s.".replace("<duration>", "" + duration)));

        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}

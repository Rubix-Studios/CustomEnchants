package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;

import java.util.List;

public class SmokeBomb extends Item{

    public SmokeBomb(){
        super("smokebomb");
    }

    @Override
    public ItemStack getItemStack() {
        final Enchantment ench = CustomEnchants.getInstance().getSmokeBombEnch();
        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        final ItemStack item = new ItemStack(Material.FIREBALL);

        item.addUnsafeEnchantment(usageEnch, usageEnch.getMaxLevel());
        item.addUnsafeEnchantment(ench, usageEnch.getMaxLevel());

        usageEnch.setMaxUses(3, item);

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GRAY + "Smoke bomb");
        meta.addEnchant(ItemUtils.FAKE_GLOW, 2, true);

        final LoreBuilder builder = new LoreBuilder(item);

        final List<String> lore = builder.withEnchantments().withUsages().getWarning().buildLore();
        lore.add("");
        lore.add(Color.translate("&7Throw this smoke bomb, when you walk thru"));
        lore.add(Color.translate("&7the smoke you and your armor will be"));
        lore.add(Color.translate("&7invisible for 10s."));

        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}

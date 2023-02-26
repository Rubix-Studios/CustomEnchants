package com.rubixstudios.customenchants.itemstacks;

import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.repair.RepairEnchantment;

import java.util.ArrayList;
import java.util.List;

public class BattleAxe extends Item{

    public BattleAxe(){
        super("battleaxe");
    }

    @Override
    public ItemStack getItemStack() {
        final RepairEnchantment repairEnch = CustomEnchants.getInstance().getRepairEnch();

        final ItemStack item = new ItemStack(Material.DIAMOND_AXE);
        final ItemMeta meta = item.getItemMeta();

        final LoreBuilder builder = new LoreBuilder(item);
        final List<String> lore = builder.getWarning().buildLore();
        meta.setLore(lore);

        meta.getLore();

        meta.setDisplayName(ChatColor.AQUA + "Battle Axe");

        item.setItemMeta(meta);

        repairEnch.setMaxRepairs(3, item);

        item.addUnsafeEnchantment(repairEnch, 1);
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);


        return item;
    }
}

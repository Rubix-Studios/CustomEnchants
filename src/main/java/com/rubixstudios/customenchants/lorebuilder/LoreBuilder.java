package com.rubixstudios.customenchants.lorebuilder;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.repair.RepairEnchantment;
import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.StringUtils;
import com.rubixstudios.customenchants.utils.item.NBTUtils;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import com.rubixstudios.customenchants.value.IEnchantmentRepairValues;
import com.rubixstudios.customenchants.value.IEnchantmentUsageValues;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoreBuilder implements Builder {
    private List<String> lore = new ArrayList<>();
    private ItemStack item;

    private IEnchantmentRepairValues repairValues = EnchantmentValue.repairValues;
    private IEnchantmentUsageValues usageValues = EnchantmentValue.usageValues;

    public LoreBuilder(ItemStack item) {
        this.item = item;
    }

    @Override
    public List<String> buildLore() {
        return this.lore;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public LoreBuilder withEnchantments() {
        // TODO loop through enchantments and add them to lore list
        final Map<Enchantment, Integer> enchantments = this.item.getEnchantments();

        if (enchantments.size() == 0) {
            return this;
        }

        for(Enchantment ench : enchantments.keySet()){
            final boolean isCustomEnchantment = ench instanceof CustomEnchantment;

            if (!isCustomEnchantment) continue;
            if (!((CustomEnchantment)ench).shouldBeDisplayedOnItemLore()) continue;

            this.lore.add(ChatColor.GRAY + ench.getName() + " " + StringUtils.getEnchantmentLevelName(ench.getMaxLevel()));
        }
        return this;
    }

    @Override
    public LoreBuilder withUsages() {
        // TODO Check if item has usages, add this usage to the lore list
        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        if (!EnchantUtils.hasItemStackEnchantment(this.item, usageEnch)){
            return this;
        }
        final boolean hasUsages = NBTUtils.hasItemData(this.item, usageValues.getKey());
        final int currentUsages = hasUsages ? NBTUtils.getItemDataInt(this.item, usageValues.getKey()) : usageEnch.getMaxUses(this.item);

        this.lore.add(ChatColor.WHITE + "Usages: " + currentUsages + "/" + usageEnch.getMaxUses(this.item));
        return this;
    }

    @Override
    public LoreBuilder withKillcount() {
        final Enchantment killEnch = CustomEnchants.getInstance().getKillEnch();

        if (!EnchantUtils.hasItemStackEnchantment(this.item, killEnch)){
            return this;
        }
        final boolean hasKills = NBTUtils.hasItemData(this.item, EnchantmentValue.KILLCOUNTKEY);
        int currentKillcount = hasKills ? NBTUtils.getItemDataInt(this.item, EnchantmentValue.KILLCOUNTKEY) : killEnch.getMaxLevel();

        if (!hasKills){
           currentKillcount = 0;
        }

        this.lore.add(ChatColor.WHITE + "Kills: " + ChatColor.RED + currentKillcount);
        return this;
    }

    @Override
    public LoreBuilder withRepairs() {
        final RepairEnchantment repairEnch = CustomEnchants.getInstance().getRepairEnch();

        if (!EnchantUtils.hasItemStackEnchantment(this.item, repairEnch)){
            return this;
        }
        final boolean hasRepairs = NBTUtils.hasItemData(this.item, repairValues.getKey());
        final int currentRepairs = hasRepairs ? NBTUtils.getItemDataInt(this.item, repairValues.getKey()) : repairEnch.getMaxRepairs(this.item);

        this.lore.add(ChatColor.WHITE + "Repairs: " + currentRepairs + "/" + repairEnch.getMaxRepairs(this.item));

        return this;
    }

    @Override
    public LoreBuilder getWarning() {
        this.lore.add("");
        this.lore.add(ChatColor.RED + "!!WARNING!!");
        this.lore.add(ChatColor.RED + "Do not edit this item in an anvil!");
        this.lore.add(ChatColor.RED + "Use /rename instead");

        return this;
    }

    @Override
    public LoreBuilder withAll() {
        withEnchantments();
        withUsages();
        withRepairs();
        withKillcount();
        getWarning();
        return this;
    }
}

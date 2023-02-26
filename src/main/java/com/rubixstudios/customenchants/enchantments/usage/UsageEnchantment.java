package com.rubixstudios.customenchants.enchantments.usage;

import com.rubixstudios.customenchants.utils.item.NBTUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.DataEnchantment;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import com.rubixstudios.customenchants.value.IEnchantmentUsageValues;
import com.rubixstudios.customenchants.utils.EnchantUtils;

public class UsageEnchantment extends DataEnchantment {


    private final IEnchantmentUsageValues usageValues = EnchantmentValue.usageValues;
    private final int maxUses = usageValues.getDefaultMaxUsages();

    public UsageEnchantment(int id) {
        super(id);
        this.throwErrorWhenMaxLevelIsFaulty();
    }

    @Override
    public String getCurrentKey() {
        return usageValues.getKey();
    }

    @Override
    public int getIntOfKey(ItemStack itemStack) {
        return NBTUtils.getItemDataInt(itemStack, getCurrentKey());
    }

    @Override
    public String getName() {
        return "Usage";
    }

    @Override
    public int getMaxLevel() {
        return maxUses;
    }

    public void setMaxUses(int maxUses, ItemStack itemStack) {
        if (NBTUtils.hasItemData(itemStack, usageValues.getMaxKey())){
//            Bukkit.broadcastMessage("Already has maxuses");
            return;
        }

        NBTUtils.setItemDataInt(itemStack, usageValues.getMaxKey(), maxUses);

    }

    public int getMaxUses(ItemStack itemStack){
        if (!NBTUtils.hasItemData(itemStack, usageValues.getMaxKey())) return maxUses;

        return NBTUtils.getItemDataInt(itemStack, usageValues.getMaxKey());
    }


    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return false;
    }

    private void throwErrorWhenMaxLevelIsFaulty() {
        if (this.getMaxLevel() < 1) {
            throw new NullPointerException("Max level of usages enchantment cannot be lower than 1");
        }
    }




}

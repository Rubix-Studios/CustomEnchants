package com.rubixstudios.customenchants.enchantments.repair;

import com.rubixstudios.customenchants.utils.item.NBTUtils;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.DataEnchantment;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import com.rubixstudios.customenchants.value.IEnchantmentRepairValues;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;

public class RepairEnchantment extends DataEnchantment {

    private final @Getter IEnchantmentRepairValues repairValues = EnchantmentValue.repairValues;
    private int maxRepairs = repairValues.getDefaultMaxRepairs();

    public RepairEnchantment(int id) {
        super(id);
    }

    @Override
    public String getCurrentKey() {
        return repairValues.getKey();
    }

    @Override
    public int getIntOfKey(ItemStack itemStack) {
        return NBTUtils.getItemDataInt(itemStack, getCurrentKey());
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return false;
    }

    @Override
    public String getName() {
        return "Repair";
    }

    @Override
    public int getMaxLevel() {
        return maxRepairs;
    }

    public void setMaxRepairs(int maxRepairs, ItemStack itemStack) {
        if (NBTUtils.hasItemData(itemStack, repairValues.getMaxKey())){
//            Bukkit.broadcastMessage("Already has maxuses");
            return;
        }

        NBTUtils.setItemDataInt(itemStack, repairValues.getMaxKey(), maxRepairs);

    }

    public int getMaxRepairs(ItemStack itemStack){
        if (!NBTUtils.hasItemData(itemStack, repairValues.getMaxKey())) return maxRepairs;

        return NBTUtils.getItemDataInt(itemStack, repairValues.getMaxKey());
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return ItemUtils.isArmor(itemStack.getType()) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }


}
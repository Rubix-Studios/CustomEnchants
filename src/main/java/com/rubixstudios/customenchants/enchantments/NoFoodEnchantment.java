package com.rubixstudios.customenchants.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;

public class NoFoodEnchantment extends CustomEnchantment implements Listener {
    public NoFoodEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @EventHandler
    private void FoodChangeEvent(FoodLevelChangeEvent event){
        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        if (player.getInventory().getHelmet() == null) return;

        if(EnchantUtils.hasItemStackEnchantment(player.getInventory().getHelmet(), this)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEquip(EquipmentSetEvent event){
        final Player player = (Player) event.getHumanEntity();

        if (EnchantUtils.hasItemStackEnchantment(event.getNewItem(), this)){
            player.setFoodLevel(20);
        }
    }

    @Override
    public String getName() {
        return "No Food";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
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
        return ItemUtils.isArmorHelmet(itemStack.getType()) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}

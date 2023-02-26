package com.rubixstudios.customenchants.enchantments.repair;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;
import com.rubixstudios.customenchants.utils.item.NBTUtils;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import com.rubixstudios.customenchants.value.IEnchantmentRepairValues;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;
import java.util.Map;

public class RepairEvent implements Listener {

    // TODO REFACTOR THIS CLASS, EVERY FUNCTION THAT USES NBT IMPLEMENT THAT IN REPAIRENCHANTMENT CLASS

    final RepairEnchantment repairEnch = CustomEnchants.getInstance().getRepairEnch();
    final IEnchantmentRepairValues repairValues = EnchantmentValue.repairValues;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRepair(final InventoryClickEvent e){
        // check whether the event has been cancelled by another plugin
        if(e.isCancelled()) return;

        if(!isInvOpenerAPlayer(e.getWhoClicked())) return;

        final Player player = (Player)e.getWhoClicked();
        Inventory inv = e.getInventory();

// see if we are talking about an anvil here
        if(!isAnvilInventory(inv)) return;
        final int rawSlot = e.getRawSlot();

// compare raw slot to the inventory view to make sure we are in the upper inventory
        if(!isSameSlot(e)) return;

        // 2 = result slot
        if(rawSlot != 2) return;

        final ItemStack[] anvilContents = anvilContent(e);

        final ItemStack oldItem = anvilContents[0];

        final ItemStack secondItem = anvilContents[1];

// I do not know if this is necessary
        if(areSlotsAir(anvilContents)) return;

// check if there is an item in the result slot
        if(isResultNull(e)) return;

        final ItemStack itemStack = e.getCurrentItem();

        final ItemMeta meta = e.getCurrentItem().getItemMeta();

        // check if player is repairing
   //     if (!isPlayerRepairing(anvilContents)) return;

        // get the repairable interface to obtain the repair cost
        if(!isItemRepairable(meta)) return;

// can the player afford to repair the item
        if(!canPlayerAffordRepair(player, meta)) return;

        if (oldItem != null && EnchantUtils.isItemUnenchantable(oldItem) || secondItem != null && EnchantUtils.isItemUnenchantable(secondItem)){
            e.setCancelled(true);
            return;
        }

        // TODO NEEDS TO BE REFACTORED ASAP, this is scuffed af
        if (!ItemUtils.isArmor(itemStack.getType()) && !itemStack.getType().equals(Material.DIAMOND_AXE)){
            if (oldItem != null) addCustomEnchants(itemStack, oldItem);
            createItemLore(itemStack);
            return;
        }

        if (!EnchantUtils.hasItemStackEnchantment(itemStack, repairEnch)) {
            addRepairEnchantment(itemStack);
        }

        if (oldItem != null) addCustomEnchants(itemStack, oldItem);

        if (secondItem != null && secondItem.getType() != Material.ENCHANTED_BOOK) {
            setRepairNBTTag(itemStack);
            useRepair(itemStack);
        }

        if (isOnMaxRepair(itemStack)){
            e.setCancelled(true);
            return;
        }

        createItemLore(itemStack);
    }

    private void addCustomEnchants(ItemStack itemStack, ItemStack toRepair){
        final Map<Enchantment, Integer> oldItemEnchants = toRepair.getEnchantments();


        for(Enchantment ench : oldItemEnchants.keySet()){
            if (ench instanceof CustomEnchantment){
                itemStack.addUnsafeEnchantment(ench, ench.getStartLevel());
            }
        }
    }

    private void addRepairEnchantment(ItemStack itemStack){
        itemStack.addUnsafeEnchantment(repairEnch, 1);
    }


    private void useRepair(ItemStack itemStack){
        int currentRepairs = getIntOfKey(itemStack);

        currentRepairs--;

        NBTUtils.setItemDataInt(itemStack, repairValues.getKey(), currentRepairs);
    }

    private void createItemLore(ItemStack itemStack){
        final ItemMeta meta = itemStack.getItemMeta();

        final LoreBuilder builder = new LoreBuilder(itemStack);

        final List<String> lore = builder.withEnchantments().withUsages().withRepairs().withKillcount().getWarning().buildLore();

        meta.setLore(lore);

        itemStack.setItemMeta(meta);
    }

    private void setRepairNBTTag(ItemStack itemStack){
        if (hasItemRepairs(itemStack)) return;

        NBTUtils.setItemDataInt(itemStack, repairValues.getKey(), repairValues.getDefaultMaxRepairs());
    }

    private int getIntOfKey(ItemStack itemStack){
        return repairEnch.getIntOfKey(itemStack);
    }

    private boolean isOnMaxRepair(ItemStack itemStack){
        return getIntOfKey(itemStack) < 0;
    }

    private boolean isSameSlot(InventoryClickEvent event){
        return event.getRawSlot() == event.getView().convertSlot(event.getRawSlot());
    }

    private boolean areSlotsAir(ItemStack[] anvilContents){
        return anvilContents[0] == null && anvilContents[1] == null;
    }

    private ItemStack[] anvilContent(InventoryClickEvent event){
        final Inventory inv = event.getInventory();
        final AnvilInventory anvil = (AnvilInventory) inv;

        return anvil.getContents();
    }

    private boolean isResultNull(InventoryClickEvent event){
        return event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null;
    }

    private boolean isItemRepairable(ItemMeta itemMeta) {
        return itemMeta instanceof Repairable;
    }

    private boolean isAnvilInventory(Inventory inventory){
        return inventory instanceof AnvilInventory;
    }

    private boolean isInvOpenerAPlayer(HumanEntity humanEntity){
        return humanEntity instanceof Player;
    }

    private boolean isRepairingWithDiamonds(ItemStack[] anvilContents){
        final Material firstSlot = anvilContents[0].getType();
        final Material secondSlot = anvilContents[1].getType();

        return firstSlot != null && secondSlot == Material.DIAMOND;
    }

    private boolean isRepairingWithSameItem(ItemStack[] anvilContents){
        final Material firstSlot = anvilContents[0].getType();
        final Material secondSlot = anvilContents[1].getType();

        return firstSlot == secondSlot;
    }

    private boolean isPlayerRepairing(ItemStack[] anvilContents){
        return isRepairingWithDiamonds(anvilContents) || isRepairingWithSameItem(anvilContents);
    }
    private boolean canPlayerAffordRepair(Player player, ItemMeta meta) {
        final Repairable repairable = (Repairable) meta;

        if (player.getGameMode() == GameMode.getByValue(1)) return true;

        return player.getLevel() >= repairable.getRepairCost();
    }

    private boolean hasItemRepairs(ItemStack itemStack){
        return NBTUtils.hasItemData(itemStack, repairValues.getKey());
    }
}

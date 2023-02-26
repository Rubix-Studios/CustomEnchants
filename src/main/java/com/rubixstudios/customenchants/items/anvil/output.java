package com.rubixstudios.customenchants.items.anvil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.EnchantUtils;

import java.util.List;

public class output { // TODO Recode without runnables (this is not necessary in this case, performance wise etc..)

    private final CustomEnchants customEnchants = CustomEnchants.getInstance();
    private final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

    /**
     * Updates player inventory
     * @param player the player
     */
    private void updatePlayerInventory(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(CustomEnchants.getInstance(), 0);
    }

    /**
     * Calculate which item you need to get when combining items
     * @param inventory the inventory
     * @param player the player
     */
    private void calculate(Inventory inventory, Player player) {
        // Get Items
        ItemStack item1 = inventory.getItem(10);
        ItemStack item2 = inventory.getItem(12);

        if (EnchantUtils.isItemUnenchantable(item1)) return;

        for (Enchantment allCustomEnchantment : customEnchants.getAllCustomEnchantments()) {
            if (allCustomEnchantment.canEnchantItem(item1) && EnchantUtils.hasItemStackEnchantment(item2, allCustomEnchantment)) {

                final int level = item2.getEnchantmentLevel(allCustomEnchantment);
                final ItemStack result = newItemStack(item1, allCustomEnchantment, level);


                //TODO improve the possibility that if a player wants to enchant more then one item it returns null
                if (result == null){
                    eventManager.callCantCreateCustomItem(player);
                    return;
                }

                // CALL EVENT
                eventManager.callCreateCustomItem(player);

                 // book level
                inventory.setItem(16, result); // ITEM PLAYER GETS

                break; // Found our enchantment
            } else {
                // Called when combination isnt valid
                eventManager.callCantCreateCustomItem(player);
            }
        }

        // Update Visuals
        updatePlayerInventory(player);
    }

    /**
     * Creates newly generated item
     * @param item1 the item
     * @param enchantment the enchant
     * @param level the enchant level
     * @return returns new itemstack
     */
    private ItemStack newItemStack(ItemStack item1, Enchantment enchantment, int level){
        final ItemStack finalItem = item1.clone();

        if (finalItem.getAmount() > 1){
//            Bukkit.broadcastMessage("Can't enchant multiple items with one book");
            return null;
        }

        finalItem.addUnsafeEnchantment(enchantment, level);

        final LoreBuilder builder = new LoreBuilder(finalItem);

        final List<String> lore = builder.withEnchantments().withUsages().withRepairs().withKillcount().getWarning().buildLore();

        final ItemMeta meta = finalItem.getItemMeta();

        meta.setLore(lore);

        finalItem.setItemMeta(meta);

        return finalItem;
    }

    /**
     * Sets default item output for custom anvil
     * @param inventory the inventory
     * @param player the player
     */
    private void defaultOut(Inventory inventory, Player player) {
        final ItemStack clayBall = new ItemStack(Material.CLAY_BALL);
        final ItemMeta itemMeta = clayBall.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_RED + "Anvil Output");
        clayBall.setItemMeta(itemMeta);
        inventory.setItem(16, clayBall);
        updatePlayerInventory(player);
    }

    /**
     * Update player inventory, checks if a combination has to be calculated
     * @param inventory the inventory
     * @param player the player
     */
    public void update(Inventory inventory, Player player) {
        // Check if slot 10 & 12 have items
        new BukkitRunnable() {
            @Override
            public void run() {
                if (inventory.getItem(10) != null && inventory.getItem(12) != null) {
                    if (inventory.getItem(10).getType() != Material.AIR && inventory.getItem(12).getType() != Material.AIR) {
                        calculate(inventory, player);
                    }else {
                        defaultOut(inventory, player);
                    }
                }else {
                    defaultOut(inventory, player);
                }
            }
        }.runTaskLater(CustomEnchants.getInstance(), 0);
    }

    /**
     * Update player inventory, checks if a combination has to be calculated
     * @param inventory the inventory
     * @param player the player
     */
    public void uncertainUpdate(Inventory inventory, Player player) {
        // Check if slot 10 & 12 have items
        new BukkitRunnable() {
            @Override
            public void run() {
                if (inventory.getItem(10) != null && inventory.getItem(12) != null) {
                    if (inventory.getItem(10).getType() != Material.AIR && inventory.getItem(12).getType() != Material.AIR) {
                        calculate(inventory, player);
                    }else {
                        defaultOut(inventory, player);
                    }
                }else {
                    defaultOut(inventory, player);
                }
            }
        }.runTaskLater(CustomEnchants.getInstance(), 0);
    }

    /**
     * Remove inputs
     * @param inventory the inventory
     * @param player the player
     */
    private void removeInputs(Inventory inventory, Player player) {
        inventory.setItem(10, null);
        inventory.setItem(12, null);
        defaultOut(inventory, player);
    }

    /**
     * Out checks
     * @param inventory the inventory
     * @param player the player
     */
    private void outCheck(Inventory inventory, Player player) {
        if (inventory.getItem(16) == null) {
            removeInputs(inventory, player);
        }else if (inventory.getItem(16).getType() == Material.AIR) {
            removeInputs(inventory, player);
        }
    }

    /**
     * Update possible output
     * @param inventory the inventory
     * @param player the player
     */
    public void possibleOutputUpdate(Inventory inventory, Player player) {
        if (inventory.getItem(16) != null) {
            if (inventory.getItem(16).getType() != Material.AIR) {
                // There is an item in output, need to check if action takes it!
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        outCheck(inventory, player);
                    }
                }.runTaskLater(CustomEnchants.getInstance(), 0);
            }
        }

    }
}

package com.rubixstudios.customenchants.uhcf;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.itemstacks.Item;
import com.rubixstudios.uhcf.abilities.AbilitiesManager;
import com.rubixstudios.uhcf.abilities.AbilityItem;
import com.rubixstudios.uhcf.abilities.AbilityType;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Djorr
 * @created 10/12/2021 - 12:00 AM
 * @project custom-enchants
 */
public class AnvilOpenEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)) return;

        if (event.getClick() == ClickType.SHIFT_LEFT && isCustomItem(event.getCurrentItem())) event.setCancelled(true);
        if (event.getClick() == ClickType.SHIFT_RIGHT && isCustomItem(event.getCurrentItem())) event.setCancelled(true);
        if (isCustomItem(event.getCurrentItem())) event.setCancelled(true);
        if (isCustomItem(event.getCursor())) event.setCancelled(true);
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent event) {
        if (isCustomItem(event.getItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)) return;

        if (isCustomItem(event.getCursor())) event.setCancelled(true);
    }

    private boolean isCustomItem(ItemStack itemStack) {
        if (CustomEnchants.getInstance().getCustomItems().stream().anyMatch(item -> item.getItemStack().equals(itemStack))) return true;
        if (Arrays.stream(CustomEnchants.getInstance().getAllCustomEnchantments()).anyMatch(enchantment -> itemStack.getEnchantments().containsKey(enchantment))) return true;

        return false;
    }
}

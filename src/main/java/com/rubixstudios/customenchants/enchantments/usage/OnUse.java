package com.rubixstudios.customenchants.enchantments.usage;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.CustomItem.CustomItemUseEvent;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class OnUse implements Listener {

    @EventHandler
    private void onUse(CustomItemUseEvent event){
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        final UsageEnchantment usageEnch = CustomEnchants.getInstance().getUsageEnch();

        final String currentKey = usageEnch.getCurrentKey();
        if (!EnchantUtils.hasItemStackEnchantment(itemStack, usageEnch)) return;
        if (!NBTUtils.hasItemData(itemStack, currentKey)){
            NBTUtils.setItemDataInt(itemStack, currentKey, usageEnch.getMaxUses(itemStack));
        }


        int currentUsages = NBTUtils.getItemDataInt(itemStack, currentKey);
        currentUsages--;

        NBTUtils.setItemDataInt(itemStack, currentKey, currentUsages);

        if (currentUsages <= 0){
            player.setItemInHand(null);
            player.updateInventory();
            return;
        }

        final ItemMeta meta = itemStack.getItemMeta();

        final LoreBuilder builder = new LoreBuilder(itemStack);

        final List<String> lore = builder.withEnchantments().withUsages().withKillcount().withRepairs().getWarning().buildLore();

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        player.updateInventory();
    }
}

package com.rubixstudios.customenchants.command;

import com.rubixstudios.customenchants.itemstacks.Item;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.CustomEnchants;

import java.util.ArrayList;

public class GiveCustomItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) return true;
        if (commandSender.hasPermission(CustomEnchants.getInstance().getPermissionPrefix() + "admin") || commandSender instanceof ConsoleCommandSender) {
            final Player player = (Player) commandSender;

            if (strings[0].equalsIgnoreCase("giveall")) {
                for (final Item enchant : CustomEnchants.getInstance().getCustomItems()) {
                    final ItemStack itemStack = new ItemStack(enchant.getItemStack());
                    player.getInventory().addItem(itemStack);
                }

                for (final Enchantment enchant : CustomEnchants.getInstance().getAllCustomEnchantments()) {
                    final ItemStack itemStack = new ItemStack(specialBook(enchant, 1));
                    player.getInventory().addItem(itemStack);
                }
                return true;
            }

            if (strings[0].equalsIgnoreCase("give")) {
                if (strings.length != 4) {
                    player.sendMessage(Color.translate("&aCE &8» &eUsage: &f/ce give <player> <enchant> <amount>"));
                    return true;
                }

                final String targetName = strings[1];
                final Player targetPlayer = Bukkit.getPlayer(targetName);

                final String enchantName = strings[2].toLowerCase();

                int amount = 1;
                if (strings[3] != null) {
                    amount = Integer.parseInt(strings[3]);
                }

                for (final Item enchant : CustomEnchants.getInstance().getCustomItems()) {
                    if (enchant.getName().equalsIgnoreCase(enchantName)) {
                        final ItemStack itemStack = new ItemStack(enchant.getItemStack());
                        itemStack.setAmount(amount);
                        targetPlayer.getInventory().addItem(itemStack);
                        return true;
                    }
                    player.sendMessage(Color.translate("&aCE &8» &cEnchant <name> doesn't exists!".replace("<name>", enchantName)));
                }

                return true;
            }

            if (strings[0].equalsIgnoreCase("giveBook")) {
                if (strings.length != 4) {
                    player.sendMessage(Color.translate("&aCE &8» &eUsage: &f/ce giveBook <player> <enchant> <amount>"));
                    return true;
                }

                final String targetName = strings[1];
                final Player targetPlayer = Bukkit.getPlayer(targetName);

                final String enchantName = strings[2].toLowerCase();

                final char ch = ' ';

                final String enchantmentName = enchantName.replace('_', ch);

                int amount = 1;
                if (strings[3] != null) {
                    amount = Integer.parseInt(strings[3]);
                }

                for (final Enchantment enchant : CustomEnchants.getInstance().getAllCustomEnchantments()) {
                    if (enchantmentName.equalsIgnoreCase(enchant.getName())) {
                        final ItemStack itemStack = new ItemStack(specialBook(enchant, amount));
                        targetPlayer.getInventory().addItem(itemStack);
                        return true;
                    }
                    player.sendMessage(Color.translate("&aCE &8» &cEnchant <name> doesn't exists!".replace("<name>", enchantmentName)));
                }
                return true;
            }
        }
        return false;
    }

    private ItemStack specialBook(final Enchantment ench, int amount){
        final ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        item.setAmount(amount);
        final ItemMeta meta = item.getItemMeta();

        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + ench.getName() + " I");
        if (ench.getItemTarget() != null) {
            lore.add(ChatColor.GRAY + "Applicable to: " + ChatColor.GOLD + ench.getItemTarget().toString());
        }
        lore.add(ChatColor.DARK_PURPLE + "Use this book in a anvil!");
        meta.setDisplayName(ChatColor.GOLD + ench.getName() + " book");

        meta.setLore(lore);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(ench, ench.getMaxLevel());

        return item;
    }

}
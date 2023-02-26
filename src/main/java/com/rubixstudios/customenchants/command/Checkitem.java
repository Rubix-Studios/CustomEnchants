package com.rubixstudios.customenchants.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import com.rubixstudios.customenchants.CustomEnchants;

public class Checkitem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player player = ((Player) commandSender).getPlayer();

        if (!player.hasPermission(CustomEnchants.getInstance().getPermissionPrefix() + "admin")) return true;

        player.getItemInHand().getEnchantments().keySet();
        for (Enchantment ench : player.getItemInHand().getEnchantments().keySet()) {
            player.sendMessage("Detected enchantment: " + ench.getName());
        }

        return true;
    }
}

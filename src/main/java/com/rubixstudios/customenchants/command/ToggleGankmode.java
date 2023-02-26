package com.rubixstudios.customenchants.command;

import com.rubixstudios.customenchants.damage.GankType;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.damage.GankDamage;

public class ToggleGankmode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        final Player player = ((Player) commandSender).getPlayer();

        if (!player.hasPermission(CustomEnchants.getInstance().getPermissionPrefix() + "admin")) return true;

        if (strings.length != 1) {
            commandSender.sendMessage(Color.translate("&aCE &8» &eUsage: &f/toggleGank <type> | envoy - koth - all"));
            return true;
        }

        final GankType gankType = GankType.getByName(strings[0]);
        if (gankType == null) {
            commandSender.sendMessage(Color.translate("&aCE &8» &cThis gank type doesn't exist!"));
            return true;
        }

        GankDamage.getInstance().toggleGankmode(commandSender, gankType);
        return true;
    }
}

package com.rubixstudios.customenchants.command;

import com.rubixstudios.uhcf.utils.Color;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.value.GankValue;

public class GankCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;

        if (!player.hasPermission(CustomEnchants.getInstance().getPermissionPrefix() + "admin")) return true;

        if (args.length != 2) {
            commandSender.sendMessage(Color.translate("&cCurrent gank values:"));
            commandSender.sendMessage(Color.translate(" - Stage 1: " + GankValue.getStage1() + "%"));
            commandSender.sendMessage(Color.translate(" - Stage 2: " + GankValue.getStage1() + "%"));
            commandSender.sendMessage(Color.translate(" - Stage 3: " + GankValue.getStage1() + "%"));
            commandSender.sendMessage(Color.translate(""));

            commandSender.sendMessage(Color.translate("&aCE &8» &eUsage: &f/gank stage1/stage2/stage3 <percentageDamage>"));
            return true;
        }

            if (args[0].equalsIgnoreCase("stage1")){
                final double newPercentage = Double.parseDouble(args[1]);
                GankValue.setStage1(newPercentage);
                CustomEnchants.getInstance().getConfig().set("gank-stage.1", newPercentage);
                CustomEnchants.getInstance().saveConfig();
                player.sendMessage("Changed gankdamage stage1 to: " + GankValue.getStage1());
                return true;
            } else if (args[0].equalsIgnoreCase("stage2")){
                final double newPercentage = Double.parseDouble(args[1]);
                GankValue.setStage2(newPercentage);
                CustomEnchants.getInstance().getConfig().set("gank-stage.2", newPercentage);
                CustomEnchants.getInstance().saveConfig();
                player.sendMessage("Changed gankdamage stage2 to: " + GankValue.getStage2());
                return true;
            } else if (args[0].equalsIgnoreCase("stage3")){
                final double newPercentage = Double.parseDouble(args[1]);
                GankValue.setStage3(newPercentage);
                CustomEnchants.getInstance().getConfig().set("gank-stage.3", newPercentage);
                CustomEnchants.getInstance().saveConfig();
                player.sendMessage("Changed gankdamage stage3 to: " + GankValue.getStage3());
                return true;
            }

        return false;
    }
}

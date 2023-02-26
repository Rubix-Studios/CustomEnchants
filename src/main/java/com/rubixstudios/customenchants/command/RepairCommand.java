package com.rubixstudios.customenchants.command;

import com.rubixstudios.customenchants.enchantments.repair.RepairEnchantment;
import com.rubixstudios.customenchants.utils.item.NBTUtils;
import com.rubixstudios.uhcf.timer.TimerManager;
import com.rubixstudios.uhcf.utils.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepairCommand implements CommandExecutor {

    final RepairEnchantment repairEnchantment = CustomEnchants.getInstance().getRepairEnch();
    private TimerManager timerManager;


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) return true;
        final Player player = (Player) commandSender;
        if (!player.hasPermission(CustomEnchants.getInstance().getPermissionPrefix() + "donor.repair")) {
            player.sendMessage(Color.translate("&bRepair &8» &cGet access to this command, buy your rank at store.swiftpvp.eu"));
            return true;
        }

        if (CustomEnchants.softDepend) {
            timerManager = TimerManager.getInstance();
            if (timerManager.getCombatTagTimer().isActive(player.getUniqueId())) {
                player.sendMessage(Color.translate("&bRepair &8» &cYou can't repair while in combat."));
                return true;
            }
        }

        if (strings.length < 1) {

            if (CustomEnchants.softDepend){
                if (timerManager.getRepairItemCooldownTimer().isActive(player.getUniqueId())){
                    final String timeLeft = timerManager.getRepairItemCooldownTimer().getDynamicTimeLeft(player);

                    player.sendMessage(Color.translate("&bRepair &8» &cYou can use &f/repair &cagain over &f<time>".replace("<time>", "" + timeLeft)));
                    return true;
                }
            }

            final ItemStack item = player.getItemInHand();

            if(item == null || item.getType() == Material.AIR || !ItemUtils.isRepairable(item.getType())) {
                player.sendMessage(Color.translate("&bRepair &8» &cCan't repair this item."));
                return true;
            }

            if (item.getDurability() == 0){
                player.sendMessage(Color.translate("&bRepair &8» &aAlready maximum durability!"));
                return true;
            }

            if (EnchantUtils.hasItemStackEnchantment(item, repairEnchantment)){
                int currentRepairs = repairEnchantment.getIntOfKey(item);
                currentRepairs--;
                if (currentRepairs < 0) {
                    player.sendMessage(Color.translate("&bRepair &8» &cCan't repair this item."));
                    return true;
                }

                createItemLore(item);
                item.setDurability((short) 0);

                player.sendMessage(Color.translate("&bRepair &8» &aRepaired item: <itemName>".replace("<itemName>", "" + item.getType().name())));
                if (CustomEnchants.softDepend) {
                    timerManager.getRepairItemCooldownTimer().activate(player.getUniqueId(), GetRepairCooldown(player) * 60 * 60);
                }
                return true;
            }

            if (ItemUtils.isArmor(item.getType())){
                addRepairEnchantment(item);
                NBTUtils.setItemDataInt(item, repairEnchantment.getCurrentKey(), repairEnchantment.getRepairValues().getDefaultMaxRepairs());
                createItemLore(item);
                item.setDurability((short) 0);
                player.sendMessage(Color.translate("&bRepair &8» &aRepaired item: <itemName>".replace("<itemName>", "" + item.getType().name())));

                if (CustomEnchants.softDepend) {
                    timerManager.getRepairItemCooldownTimer().activate(player.getUniqueId(), GetRepairCooldown(player) * 60 * 60);
                }
                return true;
            }

            item.setDurability((short) 0);

            if (CustomEnchants.softDepend) {
                timerManager.getRepairItemCooldownTimer().activate(player.getUniqueId(), GetRepairCooldown(player) * 60 * 60);
            }

            player.sendMessage(Color.translate("&bRepair &8» &aRepaired item: <itemName>".replace("<itemName>", "" + item.getType().name())));
            return true;
        }

        final List<String> itemNames = new ArrayList<>();
        final List<ItemStack> itemsToCheck = new ArrayList<>();
        itemsToCheck.addAll(Arrays.asList(player.getInventory().getContents()));
        itemsToCheck.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        if(strings[0].contains("all")) {

            if (CustomEnchants.softDepend){
                if (timerManager.getRepairAllItemsCooldownTimer().isActive(player.getUniqueId())){
                    final String timeLeft = timerManager.getRepairAllItemsCooldownTimer().getDynamicTimeLeft(player);
                    player.sendMessage(Color.translate("&bRepair &8» &cYou can use &f/repair all &cagain over &f<time>".replace("<time>", "" + timeLeft)));
                    return true;
                }
            }

            for (final ItemStack itemStack : itemsToCheck) {
                if (itemStack == null || itemStack.getType() == Material.AIR || !ItemUtils.isRepairable(itemStack.getType())) continue;
                if (itemStack.getDurability() == 0) continue;
                if (EnchantUtils.hasItemStackEnchantment(itemStack, repairEnchantment)){
                    int currentRepairs = repairEnchantment.getIntOfKey(itemStack);
                    currentRepairs--;
                    if (currentRepairs < 0) continue;
                    NBTUtils.setItemDataInt(itemStack, repairEnchantment.getCurrentKey(), currentRepairs);
                    createItemLore(itemStack);
                    itemStack.setDurability((short) 0);
                    itemNames.add(itemStack.getType().name());
                    continue;
                }

                if (ItemUtils.isArmor(itemStack.getType())){
                    addRepairEnchantment(itemStack);
                    NBTUtils.setItemDataInt(itemStack, repairEnchantment.getCurrentKey(), repairEnchantment.getRepairValues().getDefaultMaxRepairs());
                    createItemLore(itemStack);
                    itemStack.setDurability((short) 0);
                    itemNames.add(itemStack.getType().name());
                    continue;
                }
            }

            if (itemNames.isEmpty()) {
                player.sendMessage(Color.translate("&bRepair &8» &cYou didn't repair anything."));
                return true;
            }

            if (CustomEnchants.softDepend) {
                timerManager.getRepairAllItemsCooldownTimer().activate(player.getUniqueId(), GetRepairAllCooldown(player) * 60 * 60);
            }

            final StringBuilder items = new StringBuilder();
            short i = 0;
            for (final String string : itemNames) {
                i++;
                String newString = string + ", ";
                if (itemNames.size() == i){
                    newString = string + ".";
                }
                items.append(newString);
            }

            player.sendMessage(Color.translate("&bRepair &8» &aRepaired: &f<repairedItems>".replace("<repairedItems>", items)));

//            Stream.of(player.getInventory().getContents()).filter(item -> item != null && item.getType() != Material
//                    .AIR && ItemUtils.isRepairable(item.getType())).forEach(item -> item.setDurability((short) 0));
//
//            Stream.of(player.getInventory().getArmorContents()).filter(item -> item != null && item.getType() !=
//                    Material.AIR).forEach(item -> item.setDurability((short) 0));
//
//            player.sendMessage(Language.PREFIX + Language.REPAIR_REPAIRED_ALL);
        }


        return true;
    }

    private void createItemLore(final ItemStack itemStack){
        final ItemMeta meta = itemStack.getItemMeta();

        final LoreBuilder builder = new LoreBuilder(itemStack);

        final List<String> lore = builder.withEnchantments().withUsages().withRepairs().withKillcount().getWarning().buildLore();

        meta.setLore(lore);

        itemStack.setItemMeta(meta);
    }

    private void addRepairEnchantment(final ItemStack itemStack){
        itemStack.addUnsafeEnchantment(repairEnchantment, 1);
    }



    private int GetRepairCooldown(final Player player){
        for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            final String permission = perm.getPermission();
            if (permission.startsWith(CustomEnchants.getInstance().getPermissionPrefix() + "donor.repair")) {
                String[] args = permission.split("\\.");
                int number;
                if (args.length == 3) {
                    try { number = Integer.parseInt(args[2]); }
                    catch (NumberFormatException e) { number = 0; } }
                else { number = 0; }

                return number;
            }
        }

        return 0;
    }

    private int GetRepairAllCooldown(final Player player){
        for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            final String permission = perm.getPermission();
            if (permission.startsWith(CustomEnchants.getInstance().getPermissionPrefix() + "donor.repairall")) {
                String[] args = permission.split("\\.");
                int number;
                if (args.length == 3) {
                    try { number = Integer.parseInt(args[2]); }
                    catch (NumberFormatException e) { number = 0; } }
                else { number = 0; }

                return number;
            }
        }

        return 0;
    }
}

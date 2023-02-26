package com.rubixstudios.customenchants.enchantments;

import com.rubixstudios.uhcf.factions.FactionsManager;
import com.rubixstudios.uhcf.factions.claim.ClaimManager;
import com.rubixstudios.uhcf.factions.type.PlayerFaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.utils.EnchantUtils;

public class ExplosiveEnchantment extends CustomEnchantment implements Listener {

    public ExplosiveEnchantment(int id) {
        super(id);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Location loce = event.getBlock().getLocation();

        if (!(EnchantUtils.hasItemStackEnchantment(player.getItemInHand(), this))) return;

        if (CustomEnchants.softDepend) {
            final PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player);
            if (playerFaction == null) return;
            if (ClaimManager.getInstance().getFactionAt(loce).getId() != playerFaction.getId()) return;
        }

        final Block block = event.getBlock();
        final boolean requiredBlocksToBreak = (event.getBlock().getType() == Material.STONE);
        if (!(requiredBlocksToBreak)) return;

        final Location loc = block.getLocation();

        final double locX = loc.getBlockX();
        final double locY = loc.getBlockY();
        final double locZ = loc.getBlockZ();

        final int radius = 1;

        final double minX = locX - radius;
        final double maxX = locX + radius + 1.0D;
        final double minY = locY - radius;
        final double maxY = locY + radius + 1.0D;
        final double minZ = locZ - radius;
        final double maxZ = locZ + radius + 1.0D;
        for (double x = minX; x < maxX; x++) {
            for (double y = minY; y < maxY; y++) {
                for (double z = minZ; z < maxZ; z++) {
                    Location location = new Location(block.getWorld(), x, y, z);
                    if (location.getBlock().getType() == Material.BEDROCK) return;
                    location.getBlock().getDrops().forEach(item -> player.getInventory().addItem(item));
                    location.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @Override
    public String getName() {
        return "Explosive";
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
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return (itemStack.getType() == Material.WOOD_PICKAXE || itemStack.getType() == Material.STONE_PICKAXE || itemStack.getType() == Material.GOLD_PICKAXE
        || itemStack.getType() == Material.IRON_PICKAXE || itemStack.getType() == Material.DIAMOND_PICKAXE) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}

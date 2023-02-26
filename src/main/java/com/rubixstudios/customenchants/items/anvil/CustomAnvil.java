package com.rubixstudios.customenchants.items.anvil;

import com.rubixstudios.customenchants.CustomEnchants;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.rubixstudios.customenchants.events.CustomAnvil.CantCreateCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.CreateCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.PlayerGetCustomItem;
import com.rubixstudios.customenchants.events.CustomAnvil.PlayerOpensCustomAnvil;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.particles.ParticleEffect;

public class CustomAnvil implements Listener {

    private final output output = new output();

    private ItemStack clayBall = new ItemStack(Material.CLAY_BALL);
    private ItemStack whiteGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);

    private final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

    public CustomAnvil(){
        Bukkit.getPluginManager().registerEvents(this, CustomEnchants.getInstance());
    }

    /**
     * Creates custom inventory for players
     * @param player the player
     */
    private void openEnchantmentInventory(Player player) {
        Inventory Inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Turtle smith");
        for (int i = 0; i < Inv.getSize(); i++) {
            if (i == 10 || i == 12) {
                ItemStack air = new ItemStack(Material.AIR);
                Inv.setItem(i, air);
            }else if (i == 16) {
                ItemMeta itemMeta = clayBall.getItemMeta();
                itemMeta.setDisplayName(ChatColor.DARK_RED + "Anvil Output");
                clayBall.setItemMeta(itemMeta);
                Inv.setItem(i, clayBall);
            }else {
                Inv.setItem(i, whiteGlassPane);
            }
        }
        player.openInventory(Inv);
    }

    /**
     * Fires when the player closes the inventory
     * @param event the close event
     */
    @EventHandler
    private void invClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Turtle smith")) {
            final Inventory Inv = event.getInventory();
            final Player player = (Player) event.getPlayer();

            ItemStack leftItem = Inv.getItem(10);
            ItemStack rightItem = Inv.getItem(12);

            if (leftItem != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftItem);
                Inv.setItem(10, null);
            }
            if (rightItem != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), rightItem);
                Inv.setItem(12, null);
            }
        }
    }

    /**
     * When you open anvil while holding enchanted book you get a custom inventory.
     * @param event TODO Check if ItemInHand is a special book
     */
    @EventHandler
    private void OnAnvilOpen(InventoryOpenEvent event){
        final Player player = (Player) event.getPlayer();

        if (event.getInventory().getType() != InventoryType.ANVIL) return;

        final ItemStack itemInHand = player.getItemInHand();

        if (!EnchantUtils.isBookOrNametagCustomEnchant(itemInHand)) return;

        event.setCancelled(true);
        // EVENT
        eventManager.callPlayerOpensCustomAnvil(player);

        openEnchantmentInventory(player);
    }

    /**
     * When the player drags in event
     * @param event drag
     */
    @EventHandler
    private void invDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Turtle smith")) {
            for (int i : event.getRawSlots()) {
                if (i != 10 && i != 12 && i < 27) {
                    event.setCancelled(true);
                }else if (i == 10 || i == 12) {
                    output.uncertainUpdate(event.getView().getTopInventory(), (Player) event.getWhoClicked());
                }
            }
        }
    }

    /**
     * Fires when a player open a custom anvil
     * @param event open custom anvil
     */
    @EventHandler
    private void playerOpensCustomAnvil(PlayerOpensCustomAnvil event){
        final Player player = event.getPlayer();

        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 4f, 1f);

    }

    /**
     * this runs when the player puts in a invalid combination
     * @param event createcustom item
     */
    @EventHandler
    private void onInvalidCombination(CantCreateCustomItem event){
        final Player player = event.getPlayer();

        player.playSound(player.getLocation(), Sound.STEP_GRAVEL, 1f, 4f);
    }

    /**
     * Animation play for when player clicks on item
     * @param event player get custom item
     */
    @EventHandler
    private void onPlayerGetItem(PlayerGetCustomItem event){
        final Player player = event.getPlayer();

        final World world = player.getWorld();

        final ParticleEffect flameEffect = ParticleEffect.FLAME;
        final ParticleEffect fireworkSpark = ParticleEffect.FIREWORKS_SPARK;

        final Location loc = player.getLocation().add(0, 1, 0);

        flameEffect.display(0f, 0f, 0f, .1f, 100, loc, 16);
        fireworkSpark.display(0f, 0f, 0f, .1f, 100, loc, 16);

        // location, sound, volume, pitch
        world.playSound(loc, Sound.EXPLODE, 4f, 1f);
        world.playSound(loc, Sound.FIREWORK_TWINKLE2, 4f, 1f);
    }

    /**
     * Play sound effect when item is created
     * @param event CreateCustomItem
     */
    @EventHandler
    private void onCreate(CreateCustomItem event){
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        // location, sound, volume, pitch
        player.playSound(loc, Sound.ANVIL_LAND, 1f, 2f);
    }

    /**
     * Fires when player clicks in inventory. At line 173 is the call when player clicks on the generated item
     * @param event Inventory click
     */
    @EventHandler
    public void invClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Turtle smith")) {
            Player player = (Player) event.getWhoClicked();
            InventoryAction action = event.getAction();
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getHolder() == null) {
                    // Custom inventory
                    if (event.getSlot() == 10 || event.getSlot() == 12) {
                        if (action == InventoryAction.PLACE_ALL) {
                            output.update(event.getClickedInventory(), (Player) event.getWhoClicked());
                        }else if (action == InventoryAction.PLACE_ONE) {
                            output.update(event.getClickedInventory(), (Player) event.getWhoClicked());
                        }else if (action == InventoryAction.PLACE_SOME) {
                            output.update(event.getClickedInventory(), (Player) event.getWhoClicked());
                        }else if (action == InventoryAction.SWAP_WITH_CURSOR) {
                            output.update(event.getClickedInventory(), (Player) event.getWhoClicked());
                        }else {
                            output.uncertainUpdate(event.getView().getTopInventory(), player);
                        }
                    }else if (event.getSlot() == 16){

                        if (event.getCurrentItem() != null) {
                            if (event.getCurrentItem().getType() != clayBall.getType()) {
                                if (action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.HOTBAR_MOVE_AND_READD || action ==
                                        InventoryAction.SWAP_WITH_CURSOR) {
                                    event.setCancelled(true);
                                }else {  // ON CLICK NEW ITEM
                                    output.possibleOutputUpdate(event.getClickedInventory(), player);

                                    eventManager.callPlayerGetCustomItem(player);

                                }
                            }else {
                                event.setCancelled(true);
                            }
                        }else {
                            event.setCancelled(true);
                        }
                    }else {
                        // Shift-Click
                        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                            if (event.getCurrentItem() != null) {
                                Material ItemType = event.getCurrentItem().getType();
                                if (ItemType == whiteGlassPane.getType() || ItemType == clayBall.getType()) {
                                    event.setCancelled(true);
                                }else {
                                    output.uncertainUpdate(event.getView().getTopInventory(), player);
                                }
                            }else {
                                event.setCancelled(true);
                            }
                        }else {
                            event.setCancelled(true);
                        }

                    }
                }else {
                    if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (event.getCurrentItem() != null) {
                            Material ItemType = event.getCurrentItem().getType();
                            if (ItemType == whiteGlassPane.getType() || ItemType == clayBall.getType()) {
                                event.setCancelled(true);
                            }else {
                                Boolean needUpdate = true;
                                if (event.getView().getTopInventory().getItem(16) != null) {
                                    if (event.getCurrentItem() != null) {
                                        if (event.getView().getTopInventory().getItem(16).isSimilar(event.getCurrentItem())) {
                                            needUpdate = false;
                                            event.setCancelled(true);
                                        }
                                    }

                                }
                                if (needUpdate) {
                                    output.uncertainUpdate(event.getView().getTopInventory(), player);
                                }
                            }
                        }
                    }
                }
            }else {
                // Not Clicked On Inventory, Don't Handle Anything
            }
        }
    }
}

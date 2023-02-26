package com.rubixstudios.customenchants.enchantments.handlers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.events.Bomb.BombCooldownTimerEvent;
import com.rubixstudios.customenchants.events.CallableEventListener;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.ItemBuilder;

import static com.rubixstudios.customenchants.utils.CallableFunctionUtil.getEventExecutor;


public abstract class BombEnchantment extends CustomEnchantment implements Listener {
    private @Getter @Setter Material material;
    private @Getter @Setter String metadata;

    private Item item;

    public BombEnchantment(int id) {
        super(id);
        this.registerListeners();

    }

    private EventExecutor getInteractListenerExecutor() {
        return getEventExecutor(new CallableEventListener<PlayerInteractEvent>() {
            @Override
            public void call(PlayerInteractEvent eventListener) {
                onInteract(eventListener);
            }
        });
    }

    private EventExecutor getPickupListenerExecutor() {
        return getEventExecutor(new CallableEventListener<PlayerPickupItemEvent>() {
            @Override
            public void call(PlayerPickupItemEvent eventListener) {
                onBombPickup(eventListener);
            }
        });
    }

    private EventExecutor getItemMergeListenerExecutor() {
        return getEventExecutor(new CallableEventListener<ItemMergeEvent>() {
            @Override
            public void call(ItemMergeEvent eventListener) {
                onMergeBombOnGround(eventListener);
            }
        });
    }

    /**
     * Registers listeners
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, this, EventPriority.NORMAL, getInteractListenerExecutor(), CustomEnchants.getInstance());
        Bukkit.getPluginManager().registerEvent(PlayerPickupItemEvent.class, this, EventPriority.NORMAL, getPickupListenerExecutor(), CustomEnchants.getInstance());
        Bukkit.getPluginManager().registerEvent(ItemMergeEvent.class, this, EventPriority.NORMAL, getItemMergeListenerExecutor(), CustomEnchants.getInstance());
    }

    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();

        if (!((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)  /* itemInHand.getType().equals(itemStack.getType()) */ &&  EnchantUtils.hasItemStackEnchantment(itemInHand, this))){
            return;
        }
        event.setCancelled(true); // Cancels the fire_charge fire if you right click on ground or any block.

        if (itemInHand.getAmount() >= 2){
            player.sendMessage(ChatColor.RED + "You cannot throw multiple bombs!");
            return;
        }

        if (CustomEnchants.softDepend){
            final BombCooldownTimerEvent bombCooldownTimerEvent = new BombCooldownTimerEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(bombCooldownTimerEvent);
            if (bombCooldownTimerEvent.isCancelled()) return;
        }

        CustomEnchants.getInstance().getEventManager().callCustomItemUseEvent(player);

        final ItemStack bombItem = new ItemBuilder(material).build();
        final Item bomb = player.getWorld().dropItemNaturally(player.getEyeLocation(), bombItem); // Drop smokeBomb

        this.item = bomb;

        final Vector vector = player.getLocation().getDirection(); // Give an direction for shooting the fireball.
        bomb.setVelocity(vector);
        bomb.setMetadata(metadata, new FixedMetadataValue(CustomEnchants.getInstance(), true)); // Add metadata to bomb to reconize is easier

        this.checkIfBombIsOnGround(player, bomb);
    }

    private BukkitTask checkIfBombIsOnGround(Player dropper, Item bomb) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!bomb.isOnGround()) return;
                if (!hasBombMeta(bomb)) return;

                CustomEnchants.getInstance().getEventManager().callBombActivateEvent(dropper, bomb);

                cancel();
            }
        }.runTaskTimer(CustomEnchants.getInstance(), 0L, 20L);
    }

    public void onBombPickup(PlayerPickupItemEvent event) {
        final Item bomb = event.getItem();
        if (!hasBombMeta(bomb)) return;
        event.setCancelled(true);
    }

    public void onMergeBombOnGround(ItemMergeEvent event) {
        final Item bomb = event.getEntity();
        if (!hasBombMeta(bomb)) return;
        event.setCancelled(true);
    }

    private boolean hasBombMeta(Item Bomb) {
        return Bomb.hasMetadata(metadata) && Bomb.getMetadata(metadata).get(0).asBoolean(); // Check if the fireball has the meta above
    }
}

package com.rubixstudios.customenchants.enchantments.handlers;

import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.lorebuilder.LoreBuilder;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.item.NBTUtils;
import com.rubixstudios.customenchants.value.EnchantmentValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;

import java.util.List;

public abstract class KillEnchantment extends CustomEnchantment implements Listener {

	private final EventExecutor damageExecutor;

	public KillEnchantment(int id) {
		super(id);
		this.damageExecutor = ((listener, event) -> {
			if (event instanceof PlayerDeathEvent) {
				PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent)event;
				onHit(playerDeathEvent);
			}
		});
		registerListeners();
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvent(PlayerDeathEvent.class, this, EventPriority.NORMAL, this.damageExecutor, CustomEnchants.getInstance());
	}


	private void onHit(PlayerDeathEvent event){
		if (event.getEntity().getKiller() == null) return;

		final Player killer = event.getEntity().getKiller();
		final ItemStack weapon = killer.getItemInHand();
		if (weapon == null) return;

//		//Set KillCount
		if(!EnchantUtils.hasItemStackEnchantment(weapon, this))return;

		int killCount = NBTUtils.getItemDataInt(weapon, EnchantmentValue.KILLCOUNTKEY);
		killCount++;
		NBTUtils.setItemDataInt(weapon, EnchantmentValue.KILLCOUNTKEY, killCount);

//		//lore toevoegen.
		final ItemMeta meta = weapon.getItemMeta();

		final LoreBuilder builder = new LoreBuilder(weapon);

		final List<String> lore = builder.withEnchantments().withKillcount().getWarning().buildLore();

		meta.setLore(lore);

		weapon.setItemMeta(meta);
	}
}
package com.rubixstudios.customenchants.objects;

import com.rubixstudios.uhcf.utils.nms.NmsUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class SmokeBombObject {

    private final Item item;
    private final Player shooter;
    private final Location landLoc;
    private final int smokeRange;
    private final List<UUID> invisPlayers;
    private final List<UUID> uniquePlayers;
    private final long timeThrown;

    private @Setter boolean isActive = false;

    public SmokeBombObject(Player shooter, Item item) {
        this.shooter = shooter;


        this.smokeRange = 5;
        this.invisPlayers = new ArrayList<>();
        this.uniquePlayers = new ArrayList<>();

        this.timeThrown = System.currentTimeMillis();

        this.item = item;
        this.landLoc = item.getLocation();
    }

    public void addInvisPlayer(Player player) {
        if (this.invisPlayers.contains(player.getUniqueId())) return;
        this.invisPlayers.add(player.getUniqueId());

        NmsUtils.getInstance().updateArmor(player, true);
    }

    public void removeInvisPlayer(Player player) {
        if (!this.invisPlayers.contains(player.getUniqueId())) return;
        this.invisPlayers.remove(player.getUniqueId());

        NmsUtils.getInstance().updateArmor(player, false);
    }

    public void removeInvisPlayerByUUID(UUID uuid) {
        if (!this.invisPlayers.contains(uuid)) return;
        this.invisPlayers.remove(uuid);
    }

    public boolean hasInvisPlayer(Player player){
        return invisPlayers.contains(player.getUniqueId());
    }

    public boolean hasUniquePlayer(Player player){
        return uniquePlayers.contains(player.getUniqueId());
    }

    public void addUniquePlayer(Player player) {
        if (this.uniquePlayers.contains(player.getUniqueId())) return;
        this.uniquePlayers.add(player.getUniqueId());
    }

    public void removeUniquePlayer(Player player) {
        if (!this.uniquePlayers.contains(player.getUniqueId())) return;
        this.uniquePlayers.remove(player.getUniqueId());
    }

}

package com.rubixstudios.customenchants.damage;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class GPlayer {
    private Player victim;
    private Map<UUID, Long> damagers;

    public GPlayer() {
        this.damagers = new HashMap<>();
    }

    public void addNewDamager(Player damager, Long currentTime) {
        this.damagers.put(damager.getUniqueId(), currentTime);
    }

    public void removeDamagerByPlayer(Player damager){
        this.damagers.remove(damager.getUniqueId());
    }

    public void removeDamagerByUUID(UUID damager) { this.damagers.remove(damager); }

    public void replaceValueOfDamager(Player damager, Long currentTime){
        this.damagers.replace(damager.getUniqueId(), currentTime);
    }

    public void clearMap(){
        this.damagers.clear();
    }

    public int getDamagerCount(){
        return damagers.size();
    }

    public long getLastTimeStamp(){
        return damagers.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
    }

    public long getLowestTimeStamp(){
        return damagers.entrySet().stream().min(Map.Entry.comparingByValue()).get().getValue();
    }

    // TODO wijzig functie naam dat het voor iedereen duidelijk is
    public UUID getLateHitter(){
        return damagers.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
    }
}

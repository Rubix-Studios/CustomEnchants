package com.rubixstudios.customenchants.utils;

import com.rubixstudios.uhcf.UHCF;
import com.rubixstudios.uhcf.factions.Faction;
import com.rubixstudios.uhcf.factions.FactionsManager;
import com.rubixstudios.uhcf.factions.claim.ClaimManager;
import com.rubixstudios.uhcf.factions.type.KothFaction;
import com.rubixstudios.uhcf.factions.type.PlayerFaction;
import com.rubixstudios.uhcf.games.envoy.RunningEnvoy;
import com.rubixstudios.uhcf.games.koth.RunningKoth;
import org.bukkit.entity.Player;

public class LazarusUtil {
    public static boolean isAtKoth(Player player){
        final Faction factionAt = ClaimManager.getInstance().getFactionAt(player.getLocation());
        if (factionAt == null) return false;
        return factionAt instanceof KothFaction;
    }

    public static boolean isPlayerLocAtFaction(final Player player, final Faction faction){
        final Faction factionAt = ClaimManager.getInstance().getFactionAt(player.getLocation());
        if (faction == null || factionAt == null) return false;

        return faction.equals(factionAt);
    }

    public static boolean arePlayersTeamMates(Player player1, Player player2){
        final PlayerFaction playerFaction = FactionsManager.getInstance().getPlayerFaction(player1);
        final PlayerFaction playerFaction2 = FactionsManager.getInstance().getPlayerFaction(player2);
        if (playerFaction == null || playerFaction2 == null) return false;

        return playerFaction.equals(playerFaction2);
    }

    public static boolean isPlayerAtActiveKoth(final Player player) {
        for (final RunningKoth runningKoth: UHCF.getInstance().getKothManager().getRunningKoths()) {
            if (LazarusUtil.isPlayerLocAtFaction(player, runningKoth.getKothData().getFaction())) return true;
        }

        return false;
    }

    public static boolean isPlayerAtActiveEnvoy(final Player player) {
        for (final RunningEnvoy runningEnvoy: UHCF.getInstance().getEnvoyController().getRunningEnvoys()) {
            if (LazarusUtil.isPlayerLocAtFaction(player, runningEnvoy.getEnvoyData().getFaction())) return true;
        }

        return false;
    }
}

package com.rubixstudios.customenchants.damage;

import com.rubixstudios.uhcf.broadcast.type.BroadcastType;
import com.rubixstudios.uhcf.factions.Faction;
import com.rubixstudios.uhcf.factions.type.KothFaction;
import com.rubixstudios.uhcf.factions.type.SystemFaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Djorr
 * @created 02/01/2022 - 5:29 AM
 * @project custom-enchants
 */

@Getter
@AllArgsConstructor
public enum GankType {
    ENVOY("Envoy", false, new HashMap<>()),
    KOTH("KoTH", false, new HashMap<>()),
    OVERALL("All", false, new HashMap<>());

    private final String name;
    @Setter private boolean enabled;
    private Map<UUID, GPlayer> gankMap;

    private static final Map<String, GankType> BY_NAME;

    public static GankType getByName(String name) {
        return BY_NAME.get(name);
    }

    static {
        BY_NAME = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for(GankType gankType : values()) {
            BY_NAME.put(gankType.name, gankType);
        }
    }

}

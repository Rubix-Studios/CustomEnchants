package com.rubixstudios.customenchants.lorebuilder;

import java.util.List;

public interface Builder {

    LoreBuilder withEnchantments();

    LoreBuilder withUsages();
    LoreBuilder withRepairs();
    LoreBuilder withKillcount();
    LoreBuilder withAll();
    List<String> buildLore();
    List<String> getLore();

    LoreBuilder getWarning();
}

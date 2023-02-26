package com.rubixstudios.customenchants.value;

import com.rubixstudios.customenchants.CustomEnchants;
import lombok.Getter;
import lombok.Setter;

public class GankValue {

    private static double configStage1 = CustomEnchants.getInstance().getConfig().getDouble("gank-stage.1");
    private static double configStage2 = CustomEnchants.getInstance().getConfig().getDouble("gank-stage.2");
    private static double configStage3 = CustomEnchants.getInstance().getConfig().getDouble("gank-stage.3");

    public @Getter @Setter static double stage1 = configStage1;
    public @Getter @Setter static double stage2 = configStage2;
    public @Getter @Setter static double stage3 = configStage3;
}

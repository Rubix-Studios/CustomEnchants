package com.rubixstudios.customenchants.value;

import com.rubixstudios.customenchants.CustomEnchants;
import lombok.Getter;
import lombok.Setter;

public class BleedValue {
    public @Getter @Setter static int BleedPercentage = CustomEnchants.getInstance().getConfig().getInt("blood-effect.damage");
}

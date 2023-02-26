package com.rubixstudios.customenchants.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern ALPHANUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");

    public static final String[] ENCHANTMENT_NAMES = new String[] { "AQUA_AFFINITY", "BANE_OF_ARTHROPODS", "BLAST_PROTECTION",
            "DURABILITY", "EFFICIENCY", "FEATHER_FALLING", "FIRE_ASPECT", "FIRE_PROTECTION", "FLAME", "FORTUNE", "INFINITY", "KNOCKBACK",
            "LOOTING", "LUCK", "LURE", "POWER", "PROJECTILE_PROTECTION", "PROTECTION", "PUNCH", "RESPIRATION", "SHARPNESS",
            "SILK_TOUCH", "SMITE", "THORNS", "UNBREAKING" };

    public static String getEnchantmentLevelName(int level) {
        switch (level) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
        return "<3";
    }

    private static String getPotionEffectName(PotionEffectType type) {
        switch(type.getName()) {
            case "ABSORPTION": return "Absorption";
            case "BLINDNESS": return "Blindness";
            case "CONFUSION": return "Confusion";
            case "DAMAGE_RESISTANCE": return "Resistance";
            case "FAST_DIGGING": return "Haste";
            case "FIRE_RESISTANCE": return "Fire Resistance";
            case "HARM": return "Instant Damage";
            case "HEAL": return "Instant Health";
            case "HEALTH_BOOST": return "Health Boost";
            case "HUNGER": return "Hunger";
            case "INCREASE_DAMAGE": return "Strength";
            case "INVISIBILITY": return "Invisibility";
            case "JUMP": return "Jump";
            case "NIGHT_VISION": return "Night Vision";
            case "POISON": return "Poison";
            case "REGENERATION": return "Regeneration";
            case "SATURATION": return "Saturation";
            case "SLOW": return "Slowness";
            case "SLOW_DIGGING": return "Slow Digging";
            case "SPEED": return "Speed";
            case "WATER_BREATHING": return "Water Breathing";
            case "WEAKNESS": return "Weakness";
            case "WITHER": return "Wither";
            default: return "Unknown potion effect";
        }
    }

    private static String getPotionEffectLevel(PotionEffect effect) {
        switch(effect.getAmplifier()) {
            case 0: return "";
            case 1: return "II";
            case 2: return "III";
            case 3: return "IV";
            case 4: return "V";
            case 5: return "VI";
            case 6: return "VII";
            case 7: return "VIII";
            case 8: return "IX";
            case 9: return "X";
            default: return "Unknown potion effect level";
        }
    }

    public static String getEnchantName(String enchant) {
        switch(enchant) {
            case "PROTECTION": return "PROTECTION_ENVIRONMENTAL";
            case "FIRE_PROTECTION": return "PROTECTION_FIRE";
            case "FEATHER_FALLING": return "PROTECTION_FALL";
            case "BLAST_PROTECTION": return "PROTECTION_EXPLOSIONS";
            case "PROJECTILE_PROTECTION": return "PROTECTION_PROJECTILE";
            case "RESPIRATION": return "OXYGEN";
            case "AQUA_AFFINITY": return "WATER_WORKER";
            case "THORNS": return "THORNS";
            case "SHARPNESS": return "DAMAGE_ALL";
            case "SMITE": return "DAMAGE_UNDEAD";
            case "BANE_OF_ARTHROPODS": return "DAMAGE_ARTHROPODS";
            case "KNOCKBACK": return "KNOCKBACK";
            case "FIRE_ASPECT": return "FIRE_ASPECT";
            case "LOOTING": return "LOOT_BONUS_MOBS";
            case "EFFICIENCY": return "DIG_SPEED";
            case "SILK_TOUCH": return "SILK_TOUCH";
            case "UNBREAKING": return "DURABILITY";
            case "DURABILITY": return "DURABILITY";
            case "FORTUNE": return "LOOT_BONUS_BLOCKS";
            case "POWER": return "ARROW_DAMAGE";
            case "PUNCH": return "ARROW_KNOCKBACK";
            case "FLAME": return "ARROW_FIRE";
            case "INFINITY": return "ARROW_INFINITE";
            case "LUCK": return "LUCK";
            case "LURE": return "LURE";
            default: return "Unknown enchantment";
        }
    }

    public static String getEnchantName(Enchantment enchantment) {
        switch(enchantment.getName()) {
            case "PROTECTION_ENVIRONMENTAL": return "Protection";
            case "PROTECTION_FIRE": return "Fire Protection";
            case "PROTECTION_FALL": return "Feather Falling";
            case "PROTECTION_EXPLOSIONS": return "Blast Protection";
            case "PROTECTION_PROJECTILE": return "Projectile Protection";
            case "OXYGEN": return "Respiration";
            case "WATER_WORKER": return "Aqua Affinity";
            case "THORNS": return "Thorns";
            case "DAMAGE_ALL": return "Sharpness";
            case "DAMAGE_UNDEAD": return "Smite";
            case "DAMAGE_ARTHROPODS": return "Bane of Arthropods";
            case "KNOCKBACK": return "Knockback";
            case "FIRE_ASPECT": return "Fire Aspect";
            case "LOOT_BONUS_MOBS": return "Looting";
            case "DIG_SPEED": return "Efficiency";
            case "SILK_TOUCH": return "Silk Touch";
            case "DURABILITY": return "Unbreaking";
            case "LOOT_BONUS_BLOCKS": return "Fortune";
            case "ARROW_DAMAGE": return "Power";
            case "ARROW_KNOCKBACK": return "Punch";
            case "ARROW_FIRE": return "Flame";
            case "ARROW_INFINITE": return "Infinity";
            case "LUCK": return "Luck";
            case "LURE": return "Lure";
            default: return "Unknown enchantment";
        }
    }
}
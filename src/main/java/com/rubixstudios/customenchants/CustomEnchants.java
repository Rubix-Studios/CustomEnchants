package com.rubixstudios.customenchants;

import com.rubixstudios.customenchants.command.*;
import com.rubixstudios.customenchants.damage.GankDamage;
import com.rubixstudios.customenchants.enchantments.*;
import com.rubixstudios.customenchants.enchantments.Bomb.*;
import com.rubixstudios.customenchants.enchantments.Bomb.TestBomb;
import com.rubixstudios.customenchants.enchantments.bleed.BleedEnchantment;
import com.rubixstudios.customenchants.enchantments.repair.RepairEnchantment;
import com.rubixstudios.customenchants.enchantments.usage.UsageEnchantment;
import com.rubixstudios.customenchants.itemstacks.*;
import com.rubixstudios.customenchants.manager.*;
import com.rubixstudios.customenchants.uhcf.AnvilOpenEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import com.rubixstudios.customenchants.command.GankCommand;
import com.rubixstudios.customenchants.damage.GankParticles;
import com.rubixstudios.customenchants.enchantments.bleed.BleedDamage;
import com.rubixstudios.customenchants.enchantments.bleed.BleedEvent;
import com.rubixstudios.customenchants.enchantments.BowPoisonEnchantment;
import com.rubixstudios.customenchants.enchantments.repair.RepairEvent;
import com.rubixstudios.customenchants.enchantments.repair.UnEnchantableEnchantment;
import com.rubixstudios.customenchants.enchantments.usage.OnUse;
import com.rubixstudios.customenchants.items.anvil.CustomAnvil;
import com.rubixstudios.customenchants.potion.PotionDuration;
import com.rubixstudios.customenchants.uhcf.ArcherTagEvent;
import com.rubixstudios.customenchants.utils.EnchantUtils;

import java.lang.reflect.Field;
import java.util.*;

public class CustomEnchants extends JavaPlugin implements Listener  {

    private static @Getter CustomEnchants instance;

    private @Getter
    FireResEnchantment fireResEnch;
    private @Getter
    SpeedEnchantment speedEnch;
    private @Getter
    BlindnessEnchantment blindnessEnch;
    private @Getter
    BleedEnchantment bleedEnch;
    private @Getter
    SlownessEnchantment slowEnch;
    private @Getter
    PoisonEnchantment poisonEnch;
    private @Getter KillOnItemEnchantment killEnch;
    private @Getter NoFoodEnchantment noFoodEnch;
    private @Getter MagnetEnchantment magnetEnch;
    private @Getter
    UsageEnchantment usageEnch;
    private @Getter
    RepairEnchantment repairEnch;
    private @Getter RegenEnchantment regenEnch;
    private @Getter
    BowPoisonEnchantment bowPoisonEnch;
    private @Getter ExplosiveEnchantment explosiveEnch;
    private @Getter TripleShotEnchantment tripleShotEnch;
    private @Getter
    SmokeBombEnchantment smokeBombEnch;
    private @Getter GrappleHarpoonEnchantment grappleHarpoonEnch;
    private @Getter UnEnchantableEnchantment unEnchantableEnch;
    private @Getter
    BleedBombEnchantment bleedBombEnchantment;
    private @Getter BowWitherEnchantment bowWitherEnchantment;
    private @Getter
    TestBomb testBomb;
    private @Getter
    MagnetManager magnetManager;

    private @Getter
    EventManager eventManager;
    private @Getter
    SmokeBombManager smokeBombManager;
    private @Getter
    BleedBombManager bleedBombManager;
    private @Getter
    ParticleTrailManager particleTrailManager;

    private @Getter Enchantment[] allCustomEnchantments;

    private @Getter final Map<UUID, PotionDuration> potionEffectDur = new HashMap<>();

    public static boolean softDepend = false;

    private final FileConfiguration config = this.getConfig();

    private @Getter final List<Item> customItems;

    private @Getter String permissionPrefix;

    public CustomEnchants(){
        instance = this;

        this.customItems = new ArrayList<>();

    }

    private void setupEnchants() {
        customItems.add(new BattleAxe());
        customItems.add(new BleedBomb());
        customItems.add(new GrappleHarpoon());
        customItems.add(new KillTag());
        customItems.add(new MagnetRod());
        customItems.add(new SmokeBomb());
        customItems.add(new TearOfBlood());
        customItems.add(new Thelep());
    }

    @SneakyThrows
    @Override
    public void onEnable(){
        initializeConfig();
        this.registerEnchantments();
        this.eventManager = new EventManager();
        this.smokeBombManager = new SmokeBombManager();
        this.bleedBombManager = new BleedBombManager();
        this.particleTrailManager = new ParticleTrailManager();
        this.magnetManager = new MagnetManager();
        loadEnchantments();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(smokeBombManager, this);
        Bukkit.getPluginManager().registerEvents(new CustomAnvil(), this);
        Bukkit.getPluginManager().registerEvents(new GankDamage(), this);
        Bukkit.getPluginManager().registerEvents(new GankParticles(), this);
        Bukkit.getPluginManager().registerEvents(new RepairEvent(), this);

        Bukkit.getPluginManager().registerEvents(new BleedDamage(), this);
        Bukkit.getPluginManager().registerEvents(new BleedEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OnUse(), this);
        Bukkit.getPluginManager().registerEvents(new OnActivate(), this);
        Bukkit.getPluginManager().registerEvents(new BombCooldown(), this);
        Bukkit.getPluginManager().registerEvents(noFoodEnch, this);
        Bukkit.getPluginManager().registerEvents(magnetEnch, this);
        Bukkit.getPluginManager().registerEvents(smokeBombEnch, this);
        Bukkit.getPluginManager().registerEvents(tripleShotEnch, this);
        Bukkit.getPluginManager().registerEvents(regenEnch, this);
        Bukkit.getPluginManager().registerEvents(explosiveEnch, this);
        Bukkit.getPluginManager().registerEvents(grappleHarpoonEnch, this);
        Bukkit.getPluginManager().registerEvents(bleedBombEnchantment, this);

        this.getCommand("checkEnchant").setExecutor(new Checkitem());
        this.getCommand("toggleGank").setExecutor(new ToggleGankmode());
        this.getCommand("gank").setExecutor(new GankCommand());
        this.getCommand("ce").setExecutor(new GiveCustomItemCommand());
        this.getCommand("repair").setExecutor(new RepairCommand());

        softDepend = Bukkit.getServer().getPluginManager().getPlugin("UHCF") != null;
        if (softDepend){
            System.out.println("softDepending on UHCF!");
            Bukkit.getPluginManager().registerEvents(new ArcherTagEvent(), this);
            Bukkit.getPluginManager().registerEvents(new AnvilOpenEvent(), this);
        }

        permissionPrefix = getConfig().getString("permission.prefix");

        this.setupEnchants();
    }

    @Override
    public void onDisable(){
        this.smokeBombManager.removePotionEffectsFromPlayersInCache();
        this.smokeBombManager.addPotionEffectsFromPotionCache();
        removeSpecialPotionEffects();
        disable();
    }

    private void initializeConfig(){
        config.options().copyDefaults(true);
        config.addDefault("gank-stage.1", 0.09);
        config.addDefault("gank-stage.2", 0.12);
        config.addDefault("gank-stage.3", 0.13);

        config.addDefault("blood-effect.damage", 1.1);
        config.addDefault("blood-effect.seconds", 10);

        config.addDefault("regeneration-effect.seconds", 6);
        config.addDefault("regeneration-effect.amplifier", 2);

        config.addDefault("poisonbow-effect.seconds", 5);
        config.addDefault("poisonbow-effect.amplifier", 1);
        config.addDefault("poisonbow-effect-minimal.chance", 0);
        config.addDefault("poisonbow-effect-maximal.chance", 8);

        config.addDefault("witherbow-effect.seconds", 4);
        config.addDefault("witherbow-effect.amplifier", 1);
        config.addDefault("witherbow-effect-minimal.chance", 0);
        config.addDefault("witherbow-effect-maximal.chance", 8);

        config.addDefault("poisonStick-effect.seconds", 5);
        config.addDefault("poisonStick-effect.amplifier", 1);

        config.addDefault("slowness-effect.seconds", 3);
        config.addDefault("slowness-effect.amplifier", 0);
        config.addDefault("slowness-effect-minimal.chance", 0);
        config.addDefault("slowness-effect-maximal.chance", 30);

        config.addDefault("blindness-effect.seconds", 3);
        config.addDefault("blindness-effect.amplifier", 0);
        config.addDefault("blindness-effect-minimal.chance", 0);
        config.addDefault("blindness-effect-maximal.chance", 30);

        config.addDefault("gank-damage.ThresholdInSeconds", 7);

        config.addDefault("permission.prefix", "kunai.");
        saveConfig();
    }

    private void removeSpecialPotionEffects(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getInventory().getArmorContents() == null) continue;
            for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                // TODO REFACTOR THIS
                if (EnchantUtils.hasItemStackEnchantment(itemStack, fireResEnch)) {
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                }

                if (EnchantUtils.hasItemStackEnchantment(itemStack, speedEnch)) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }
    }

    /**
     * Get the potion duration from the player
     * @param uuid the player uuid
     * @return returns the PotionDuration
     */
    public PotionDuration getPotionDuration(UUID uuid){
        if (!potionEffectDur.containsKey(uuid)){
            potionEffectDur.put(uuid, new PotionDuration());
        }

        return potionEffectDur.get(uuid);
    }

    public void disable() {
        // Plugin shutdown logic
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);

            for (Enchantment allCustomEnchantment : this.allCustomEnchantments) {
                if (byId.containsKey(allCustomEnchantment.getId())) {
                    byId.remove(allCustomEnchantment.getId());
                }

                if (byName.containsKey(allCustomEnchantment.getName())) {
                    byName.remove(allCustomEnchantment.getName());
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void registerEnchantments() {
        this.fireResEnch = new FireResEnchantment(101);
        this.speedEnch = new SpeedEnchantment(102);
        this.blindnessEnch = new BlindnessEnchantment(103);
        this.bleedEnch = new BleedEnchantment(104);
        this.slowEnch = new SlownessEnchantment(105);
        this.poisonEnch = new PoisonEnchantment(106);
        this.killEnch = new KillOnItemEnchantment(107);
        this.noFoodEnch = new NoFoodEnchantment(108);
        this.magnetEnch = new MagnetEnchantment(109);
        this.usageEnch = new UsageEnchantment(110);
        this.repairEnch = new RepairEnchantment(111);
        this.regenEnch = new RegenEnchantment(112);
        this.bowPoisonEnch = new BowPoisonEnchantment(113);
        this.tripleShotEnch = new TripleShotEnchantment(114);
        this.explosiveEnch = new ExplosiveEnchantment(115);
        this.smokeBombEnch = new SmokeBombEnchantment(116);
        this.grappleHarpoonEnch = new GrappleHarpoonEnchantment(117);
        this.unEnchantableEnch = new UnEnchantableEnchantment(118);
        this.bleedBombEnchantment = new BleedBombEnchantment(119);
        this.bowWitherEnchantment = new BowWitherEnchantment(120);
        this.testBomb = new TestBomb(121);

        this.allCustomEnchantments = new Enchantment[] {
                fireResEnch, speedEnch, blindnessEnch, bleedEnch, slowEnch, poisonEnch,
                killEnch, noFoodEnch, magnetEnch, usageEnch, repairEnch, regenEnch, bowPoisonEnch,
                tripleShotEnch, explosiveEnch, smokeBombEnch, grappleHarpoonEnch, unEnchantableEnch,
                bleedBombEnchantment, bowWitherEnchantment, testBomb
        };
    }

    /**
     * Logic for loading customItems
     */
    private void loadEnchantments() {
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (Enchantment allCustomEnchantment : this.allCustomEnchantments) {
                    Enchantment.registerEnchantment(allCustomEnchantment);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

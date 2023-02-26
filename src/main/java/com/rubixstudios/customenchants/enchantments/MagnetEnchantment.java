package com.rubixstudios.customenchants.enchantments;

import com.rubixstudios.uhcf.timer.TimerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.CustomEnchants;
import com.rubixstudios.customenchants.enchantments.handlers.CustomEnchantment;
import com.rubixstudios.customenchants.manager.EventManager;
import com.rubixstudios.customenchants.objects.MagnetData;
import com.rubixstudios.customenchants.utils.EnchantUtils;
import com.rubixstudios.customenchants.utils.LazarusUtil;

public class MagnetEnchantment extends CustomEnchantment implements Listener {
    public MagnetEnchantment(int id) {
        super(id);
    }

    @Override
    public boolean shouldBeDisplayedOnItemLore() {
        return true;
    }

    @EventHandler
    private void onHit(PlayerFishEvent event){
        if (!(event.getCaught() instanceof Player)) return;
        final Player caughtPlayer = (Player) event.getCaught();
        final Player fisher = event.getPlayer();

        if (!EnchantUtils.hasItemStackEnchantment(fisher.getItemInHand(), this)) return;

        if (CustomEnchants.softDepend){
            final TimerManager timerManager = TimerManager.getInstance();
            if (LazarusUtil.arePlayersTeamMates(caughtPlayer, fisher)) return;
            if (!timerManager.getMagnetRodCooldownTimer().isActive(fisher)) {
                timerManager.getMagnetRodCooldownTimer().activate(fisher, 30);

            }else {

                return;
            }
        }

        final EventManager eventManager = CustomEnchants.getInstance().getEventManager();

        // FIRE EVENT BEFORE LOGIC, or else problems can occur with interfering with the itemstack
        eventManager.callCustomItemUseEvent(fisher);

        final MagnetData magnetData = new MagnetData(caughtPlayer, fisher);
        CustomEnchants.getInstance().getMagnetManager().getMagnetDataList().add(magnetData);
    }

    // Function to calculate magnitude
// of a 3 dimensional vector
    private double vectorMagnitude(Vector vector)
    {
        // Stores the sum of squares
        // of coordinates of a vector
        double sum = vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ();

        // Return the magnitude
        return Math.sqrt(sum);
    }

    public Vector getDirectionFromTo(Location loc1, Location loc2) {
        final Vector v1 = loc1.toVector(); // We converten de locations naar vectors, omdat ik toevallig weet dat player.setVelocity een vector
        final Vector v2 = loc2.toVector();
        return v2.subtract(v1).normalize(); // Als je 2 posities van elkaar aftrekt krijg je de directie. Wij willen de directie van loc1 -> loc2. Dus we doen (loc2 - loc1) = loc1 -> loc2. Vervolgens normalizen we dit getal aangezien we een getal tussen de 0-1 willen.
    }

    // TODO bandage code, fix this. I did this because if velocity is higher then 4 or lower then -4 the console throws a stacktrace for "excessive velocity detected"
    private Vector correctVelocity(Vector vector){
        if (vector.getX() >= 4){
            vector.setX(3.99);
        }
        if (vector.getY() >= 4){
            vector.setY(3.99);
        }
        if (vector.getZ() >= 4) {
            vector.setZ(3.99);
        }

        if (vector.getX() <= -4){
            vector.setX(-3.99);
        }
        if (vector.getY() <= -4){
            vector.setY(-3.99);
        }
        if (vector.getZ() <= -4) {
            vector.setZ(-3.99);
        }

        return vector;
    }

    @Override
    public String getName() {
        return "Magnet";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.FISHING_ROD;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().equals(Material.FISHING_ROD) && !EnchantUtils.hasItemStackEnchantment(itemStack, this);
    }
}

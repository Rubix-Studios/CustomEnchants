package com.rubixstudios.customenchants.manager;

import com.rubixstudios.customenchants.CustomEnchants;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import com.rubixstudios.customenchants.objects.MagnetData;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class MagnetManager {
    private BukkitRunnable setupTask;

    private @Getter List<MagnetData> magnetDataList = new ArrayList<>();

    public MagnetManager() {
        // Plugin startup logic
        this.setupTask = setupTasks();
        this.setupTask.runTaskTimer(CustomEnchants.getInstance(), 0L, 0L);
     //   Bukkit.getPluginManager().registerEvents(this, this);
    }

    private BukkitRunnable setupTasks() {
        final int timeAfterRemoval = 5; // 5 sec

        return new BukkitRunnable() {
            @Override
            public void run() {

                if (magnetDataList.isEmpty()) return; // Check if magnetData cache is empty then return.

                final List<MagnetData> magnetDataCopy = new ArrayList<>(magnetDataList);
                magnetDataCopy.forEach((magnetData) -> {
                    final Player caughtPlayer = magnetData.getCaughtPlayer();
                    final Player fishPlayer = magnetData.getFisher();
                    final Vector direction = magnetData.getDirection();
                    final Vector dirNormalized = magnetData.getNormalizedDirection();

                    double currentStep = magnetData.getSteps();
                    if (magnetData.isDone()) return;

                    final double stopAtStep = 1.5;

                    //   Bukkit.broadcastMessage("Height: " + dirNormalized.getY());

                    final double power = 5; // Kracht achter de velocity / multiplier
                    final double heightMultiplier = 0.8; // Multiplier die alleen op de Y-as werkt (zodat speler niet 10000 blokken omhoog vliegt)

                    double maxHeightVel = 1; // Dit is de max velocity op de Y-as (LET OP DIT IS NIET HOE HOOG HIJ MAXIMAAL MAG IN BLOKKEN)

                    final double minimum = 0.15;

                    double progress =  (magnetData.getMaxSteps() - currentStep) / magnetData.getMaxSteps();

                    double calcY = caughtPlayer.getLocation().toVector().normalize().getY() * heightMultiplier;

                    calcY = Math.max(calcY, minimum);
                    progress = Math.max(progress, minimum);

                    progress = 1 - progress;


                    if (currentStep <= magnetData.getMiddlePoint() - (stopAtStep * 0.5) || magnetData.isAtMiddlePoint()) {

                        calcY *= -1;
                        //  progress += 1;

                        if (!magnetData.isAtMiddlePoint()) {
                      //      Bukkit.broadcastMessage("MIDDLEPOINT");
                            magnetData.setAtMiddlePoint(true);
                        }
                    }

                    calcY *= bergParabool(progress);

                  //  Bukkit.broadcastMessage("Calc1: " + calcY);
                    //  calcY = easeOutCubic(calcY);

                    dirNormalized.setY(calcY); // Y-as wordt afhankelijk van de afstand tussen de 2 spelers. Ook willen we de power weer toevoegen om de velocity enig zins op hoogte van de x,z te zetten. Vervolgens stellen we hem bij met onze "heightMultiplier"

                    double finalYCalc = Math.min(dirNormalized.getY(), maxHeightVel);
                    //    finalYCalc = bergParabool(finalYCalc) / 10;
                    if (calcY <= 0) {
                    /*    maxHeightVel *= -1;

                        finalYCalc = Math.max(dirNormalized.getY(), maxHeightVel);
                   //     finalYCalc = bergParabool(finalYCalc) / 10;
                        finalYCalc *= -1; */
                    }

                    dirNormalized.setY(finalYCalc); // Zorgt dat de velocity op de Y-as niet hoger is dan de "maxHeightVel", zodat hij niet ineens super hoog de lucht in vliegt. Aka een cap


                   // Bukkit.broadcastMessage("Final CalcY: " + String.valueOf(dirNormalized.getY()));
                    //    dirNormalized.setY(magnetData.getHeight());

                    caughtPlayer.setFallDistance(0);

                    if (currentStep <= stopAtStep){

                        magnetData.setDone(true);
                   //     Bukkit.broadcastMessage("Steps done");
                        caughtPlayer.setVelocity(new Vector(dirNormalized.getX() * 0.20, dirNormalized.getY() * 0.20, dirNormalized.getZ() * 0.20));
                        magnetDataList.remove(magnetData);
                        return;
                    }

                   // Bukkit.broadcastMessage("Progress: " + progress);

                    caughtPlayer.setVelocity(dirNormalized);

                    currentStep -= dirNormalized.length();
                    magnetData.setSteps(currentStep);
                });
            }
        };
    }


    private double bergParabool(double x) {

        return pow(x, 2) - 4 * x + 5;
    }

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
}

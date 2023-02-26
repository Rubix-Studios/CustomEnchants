package com.rubixstudios.customenchants.objects;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MagnetData {
    private final Player caughtPlayer;
    private final Player fisher;
    private final Vector direction;
    private final Vector normalizedDirection;

    private Vector currentCaughtPlayerPos;

    private boolean isDone = false;

    private double currentSteps;

    private double maxSteps;

    private double middlePoint;

    private boolean isAtMiddlePoint;

    private int vectorSplitter = 3;

    public MagnetData(final Player caughtPlayer, final Player fisher) {
        this.caughtPlayer = caughtPlayer;
        this.fisher = fisher;

        this.direction = fisher.getLocation().toVector().subtract(caughtPlayer.getLocation().toVector());
        this.maxSteps = direction.length();
        this.normalizedDirection = new Vector(direction.getX(), direction.getY(), direction.getZ());
        this.normalizedDirection.normalize();

        this.currentCaughtPlayerPos = caughtPlayer.getLocation().toVector();

        currentSteps = maxSteps;

        middlePoint = maxSteps / 2;

    }

    public double getMaxSteps() {
        return maxSteps;
    }

    public int getVectorSplitter() {
        return vectorSplitter;
    }

    public boolean isAtMiddlePoint() {
        return isAtMiddlePoint;
    }

    public void setAtMiddlePoint(boolean atMiddlePoint) {
        isAtMiddlePoint = atMiddlePoint;
    }

    public double getMiddlePoint() {
        return middlePoint;
    }

    public double getSteps() {
        return currentSteps;
    }

    public void setSteps(double steps) {
        this.currentSteps = steps;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Player getCaughtPlayer() {
        return caughtPlayer;
    }

    public Player getFisher() {
        return fisher;
    }

    public Vector getCurrentCaughtPlayerPos() {
        return currentCaughtPlayerPos;
    }

    public void setCurrentCaughtPlayerPos(Vector currentCaughtPlayerPos) {
        this.currentCaughtPlayerPos = currentCaughtPlayerPos;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getNormalizedDirection() {
        return normalizedDirection;
    }
}

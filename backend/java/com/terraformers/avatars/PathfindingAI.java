package com.terraformers.avatars;

public class PathfindingAI implements AIController {
    private double targetX, targetY, targetZ;

    public PathfindingAI(double targetX, double targetY, double targetZ) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    @Override
    public double[] decideNextMove(double x, double y, double z) {
        double stepSize = 1.0;

        double dirX = targetX - x;
        double dirY = targetY - y;
        double dirZ = targetZ - z;

        double length = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        if (length == 0) return new double[]{x, y, z};

        return new double[]{
            x + (dirX / length) * stepSize,
            y + (dirY / length) * stepSize,
            z + (dirZ / length) * stepSize
        };
    }
}


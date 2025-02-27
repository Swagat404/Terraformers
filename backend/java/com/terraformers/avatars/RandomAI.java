package com.terraformers.avatars;

import java.util.Random;

public class RandomAI implements AIController {
    private Random random = new Random();

    @Override
    public double[] decideNextMove(double x, double y, double z) {
        double moveX = x + (random.nextDouble() * 2 - 1);
        double moveY = y + (random.nextDouble() * 2 - 1);
        double moveZ = z + (random.nextDouble() * 2 - 1);
        return new double[]{moveX, moveY, moveZ};
    }
}


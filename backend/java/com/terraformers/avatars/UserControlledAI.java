package com.terraformers.avatars;

import java.util.Scanner;

public class UserControlledAI implements AIController {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public double[] decideNextMove(double x, double y, double z) {
        System.out.println("Enter movement (dx dy dz): ");
        double moveX = scanner.nextDouble();
        double moveY = scanner.nextDouble();
        double moveZ = scanner.nextDouble();

        return new double[]{x + moveX, y + moveY, z + moveZ};
    }
}


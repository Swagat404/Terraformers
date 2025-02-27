package com.terraformers.avatars;

import java.util.Scanner;

public class AvatarTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Start with RandomAI
        AIController randomAI = new RandomAI();
        AvatarController avatar = new AvatarController(randomAI);

        System.out.println("Using Random AI:");
        avatar.moveAvatar(0, 0, 0);

        // Switch to PathfindingAI
        AIController pathAI = new PathfindingAI(5, 5, 5);
        avatar.setAIBrain(pathAI);
        System.out.println("Switching to Pathfinding AI:");
        avatar.moveAvatar(0, 0, 0);

        // Switch to UserControlledAI
        AIController userAI = new UserControlledAI();
        avatar.setAIBrain(userAI);
        System.out.println("Switching to User-Controlled AI:");
        
        while (true) {
            System.out.println("Move your avatar manually (Enter dx dy dz or 'exit' to quit): ");
            if (scanner.hasNext("exit")) break;
            avatar.moveAvatar(0, 0, 0);
        }

        scanner.close();
    }
}


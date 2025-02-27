package com.terraformers.avatars;

public class AvatarController {
    private AIController aiBrain;  // AI brain object

    // Constructor accepting AIController type
    public AvatarController(AIController initialBrain) {
        this.aiBrain = initialBrain;
    }

    public void setAIBrain(AIController newBrain) {
        this.aiBrain = newBrain;
        System.out.println("AI brain switched!");
    }

    public void moveAvatar(double x, double y, double z) {
        double[] newPosition = aiBrain.decideNextMove(x, y, z);
        System.out.println("AI moved avatar to: (" + newPosition[0] + ", " + newPosition[1] + ", " + newPosition[2] + ")");
    }
}


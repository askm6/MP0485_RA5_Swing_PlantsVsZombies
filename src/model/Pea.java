package model;

import view.GamePanel;
import java.awt.*;

/**
 * Represents a standard Pea projectile shot by a Peashooter.
 * It moves forward across the lane and damages zombies upon collision.
 */
public class Pea {

    private int posX;             // Current horizontal position of the pea
    protected GamePanel gp;      // Reference to the game panel (needed for accessing game state)
    private int myLane;          // Lane (row) in which the pea travels

    /**
     * Constructs a Pea object with a given lane and starting X position.
     * 
     * @param parent Reference to the GamePanel
     * @param lane The lane (0-4) the pea travels through
     * @param startX Initial X-coordinate
     */
    public Pea(GamePanel parent, int lane, int startX) {
        this.gp = parent;
        this.myLane = lane;
        this.posX = startX;
    }

    /**
     * Moves the pea forward and checks for collision with zombies in its lane.
     * If it hits a zombie, it inflicts damage and removes itself from the game.
     */
    public void advance() {
        // Define the bounding box of the pea for collision detection
        Rectangle pRect = new Rectangle(posX, 130 + myLane * 120, 28, 28);

        // Loop through zombies in the same lane
        for (int i = 0; i < gp.getLaneZombies().get(myLane).size(); i++) {
            Zombie z = gp.getLaneZombies().get(myLane).get(i);
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + myLane * 120, 400, 120);

            // Check for intersection (collision)
            if (pRect.intersects(zRect)) {
                z.setHealth(z.getHealth() - 300); // Deal damage

                boolean exit = false;

                // If zombie is killed, remove it and update game progress
                if (z.getHealth() <= 0) {
                    // Remove zombie from lane
                    gp.getLaneZombies().get(getMyLane()).remove(i);
                    // Increase game progress
                    GamePanel.setProgress(10);
                    exit = true;
                }

                // Remove the pea after hitting a zombie
                gp.getLanePeas().get(myLane).remove(this);

                if (exit) {
                    break; // Exit loop to avoid concurrent modification
                }
            }
        }

        // Move the pea forward
        posX += 15;
    }

    // Getters and setters

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public void setMyLane(int myLane) {
        this.myLane = myLane;
    }
}

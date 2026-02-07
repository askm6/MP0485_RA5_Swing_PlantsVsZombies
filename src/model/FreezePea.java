package model;

import view.GamePanel;
import java.awt.*;

/**
 * Represents a FreezePea projectile.
 *
 * FreezePeas are shot by FreezePeashooters and deal damage while slowing
 * zombies.
 */
public class FreezePea extends Pea {

    /**
     * Constructor for the FreezePea.
     *
     * @param parent The GamePanel this projectile is part of.
     * @param lane The lane in which the projectile travels.
     * @param startX The starting horizontal position (X coordinate).
     */
    public FreezePea(GamePanel parent, int lane, int startX) {
        super(parent, lane, startX);
    }

    /**
     * Moves the projectile forward and handles collision detection with
     * zombies.
     */
    @Override
    public void advance() {
        // Define the hitbox of the FreezePea
        Rectangle pRect = new Rectangle(getPosX(), 130 + getMyLane() * 120, 28, 28);

        // Loop through all zombies in the same lane
        for (int i = 0; i < gp.getLaneZombies().get(getMyLane()).size(); i++) {
            Zombie z = gp.getLaneZombies().get(getMyLane()).get(i);

            // Define the hitbox of the zombie
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + getMyLane() * 120, 400, 120);

            // If FreezePea collides with the zombie
            if (pRect.intersects(zRect)) {
                // Deal 300 damage
                z.setHealth(z.getHealth() - 300);

                // Apply the slowing effect
                z.slow();

                boolean exit = false;

                // If zombie is killed, remove it and update game progress
                if (z.getHealth() <= 0) {
                    // Remove zombie from lane
                    gp.getLaneZombies().get(getMyLane()).remove(i);
                    // Increase game progress
                    GamePanel.setProgress(10);
                    exit = true;
                }

                // Remove this FreezePea from the game
                gp.getLanePeas().get(getMyLane()).remove(this);

                if (exit) {
                    break; // Stop checking more zombies after removal
                }
            }
        }

        // Move the projectile forward
        setPosX(getPosX() + 15);
    }
}

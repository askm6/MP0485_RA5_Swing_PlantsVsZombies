package model;

import model.Pea;
import view.GamePanel;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Represents a Peashooter plant that periodically fires peas
 * when there are zombies in its lane.
 */
public class Peashooter extends Plant {

    public Timer shootTimer; // Timer to control the shooting interval

    /**
     * Constructs a Peashooter at the specified grid position.
     *
     * @param parent Reference to the GamePanel (game state manager)
     * @param x The column index where the plant is placed
     * @param y The row index (lane) where the plant is placed
     */
    public Peashooter(GamePanel parent, int x, int y) {
        super(parent, x, y);

        // Timer triggers every 2000 milliseconds (2 seconds)
        shootTimer = new Timer(2000, (ActionEvent e) -> {
            // Check if there are zombies in the same lane (row)
            if (getGp().getLaneZombies().get(y).size() > 0) {
                // If so, create and add a new Pea projectile to that lane
                getGp().getLanePeas().get(y).add(
                    new Pea(getGp(), y, 103 + this.getX() * 100)
                );
            }
        });

        shootTimer.start(); // Begin firing on a regular interval
    }

    /**
     * Stops the Peashooter's firing timer (used when removed from the field).
     */
    @Override
    public void stop() {
        shootTimer.stop();
    }
}

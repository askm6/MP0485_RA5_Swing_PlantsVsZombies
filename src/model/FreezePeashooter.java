package model;

import view.GamePanel;
import model.FreezePea;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Represents a FreezePeashooter plant that shoots FreezePeas at regular
 * intervals.
 *
 * FreezePeas damage zombies and slow them down.
 */
public class FreezePeashooter extends Plant {

    private Timer shootTimer;

    /**
     * Constructor for the FreezePeashooter.
     *
     * @param parent The GamePanel where this plant is placed.
     * @param x The column position in the grid.
     * @param y The row (lane) position in the grid.
     */
    public FreezePeashooter(GamePanel parent, int x, int y) {
        super(parent, x, y);

        // Creates a timer that shoots every 2 seconds
        shootTimer = new Timer(2000, (ActionEvent e) -> {
            // Only shoot if there are zombies in the current lane
            if (getGp().getLaneZombies().get(y).size() > 0) {
                // Add a new FreezePea to the lane starting from the plant's position
                getGp().getLanePeas().get(y).add(new FreezePea(getGp(), y, 103 + this.getX() * 100));
            }
        });

        // Start shooting
        shootTimer.start();
    }

    /**
     * Stops the shooting timer, typically when the plant is destroyed.
     */
    @Override
    public void stop() {
        shootTimer.stop();
    }

}

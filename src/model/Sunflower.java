package model;

import view.Sun;
import model.Plant;
import view.GamePanel;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Represents the Sunflower plant, which periodically generates sun points.
 *
 * Sun points are used as a resource to plant other units.
 */
public class Sunflower extends Plant {

    // Timer that controls sun production every 15 seconds
    private Timer sunProduceTimer;

    /**
     * Constructor for the Sunflower.
     *
     * @param parent The GamePanel this plant belongs to.
     * @param x The column position of the sunflower.
     * @param y The row position of the sunflower.
     */
    public Sunflower(GamePanel parent, int x, int y) {
        super(parent, x, y);

        // Set up a timer to generate a sun every 15,000 milliseconds (15 seconds)
        sunProduceTimer = new Timer(15000, (ActionEvent e) -> {
            // Create a new sun at the appropriate coordinates
            Sun sta = new Sun(getGp(), 60 + x * 100, 110 + y * 120, 130 + y * 120);

            // Add the sun to the list of active suns in the game panel
            getGp().getActiveSuns().add(sta);

            // Add the sun to the game panel with Z-order 1 (foreground)
            getGp().add(sta, Integer.valueOf(1));
        });

        // Start the sun production timer
        sunProduceTimer.start();
    }

}

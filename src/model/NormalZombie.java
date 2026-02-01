package model;

import view.GamePanel;

/**
 * Represents a basic zombie with default health and speed.
 * This class inherits all behavior from the Zombie class.
 */
public class NormalZombie extends Zombie {

    /**
     * Constructor for the NormalZombie.
     *
     * @param parent The GamePanel the zombie is part of.
     * @param lane   The lane (row) where the zombie appears.
     */
    public NormalZombie(GamePanel parent, int lane) {
        super(parent, lane); // Uses the default health and speed from the Zombie class
    }

}

package model;

import view.GamePanel;

/**
 * Represents a ConeHeadZombie, a stronger variant of the regular zombie.
 *
 * ConeHeadZombies have more health than normal zombies.
 */
public class ConeHeadZombie extends Zombie {

    /**
     * Constructor for ConeHeadZombie.
     *
     * @param parent The GamePanel this zombie belongs to.
     * @param lane The lane in which the zombie moves.
     */
    public ConeHeadZombie(GamePanel parent, int lane) {
        super(parent, lane);

        // Set higher health than a normal zombie
        setHealth(1800);
    }
}

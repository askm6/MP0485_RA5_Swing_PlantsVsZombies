package model;

import view.GamePanel;
import view.Collider;
import controller.GameWindow;
import dao.LevelData;
import javax.swing.*;

/**
 * Represents a base Zombie with common behavior like movement, collision, and health.
 */
public class Zombie {

    private int health = 1000;   // Default health
    private int speed = 1;       // Default movement speed (pixels per update)
    int slowInt = 0;             // Counter to manage freeze/slow effect duration

    private GamePanel gp;        // Reference to the game panel this zombie belongs to

    private int posX = 1000;     // Starting X position (off-screen to the right)
    private int myLane;          // The row (lane) this zombie moves in
    private boolean isMoving = true; // Movement flag

    /**
     * Constructs a Zombie in the specified lane and game panel.
     * @param parent
     * @param lane
     */
    public Zombie(GamePanel parent, int lane) {
        this.gp = parent;
        myLane = lane;
    }

    /**
     * Moves the zombie forward or attacks a plant if in its path.
     * Ends the game if the zombie reaches the left edge of the screen.
     */
    public void advance() {
        if (isMoving) {
            boolean isCollides = false;
            Collider collided = null;

            // Check if zombie intersects with any plant collider in its lane
            for (int i = myLane * 9; i < (myLane + 1) * 9; i++) {
                if (gp.getColliders()[i].assignedPlant != null &&
                    gp.getColliders()[i].isInsideCollider(posX)) {
                    isCollides = true;
                    collided = gp.getColliders()[i];
                }
            }

            if (!isCollides) {
                // Handle slow effect
                if (slowInt > 0) {
                    if (slowInt % 2 == 0) {
                        posX--;
                    }
                    slowInt--;
                } else {
                    posX -= this.speed;
                }
            } else {
                // Attack the plant
                collided.assignedPlant.setHealth(collided.assignedPlant.getHealth() - 10);
                if (collided.assignedPlant.getHealth() <= 0) {
                    collided.removePlant();
                }
            }

            // End game if zombie reaches the left end
            if (posX < 0) {
                isMoving = false;
                JOptionPane.showMessageDialog(gp, "ZOMBIES ATE YOUR BRAIN!" + '\n' + "GAME OVER!");
                LevelData.write("1");  // Reset to the first level
                GameWindow.gw.dispose();
                System.exit(0); // Exit application
            }
        }
    }

    /**
     * Applies a slow effect for a short duration (used by FreezePeas).
     */
    public void slow() {
        slowInt = 50;
        health=0;
    }

    /**
     * Factory method to get specific zombie types by name.
     */
    public static Zombie getZombie(String type, GamePanel parent, int lane) {
        Zombie z = new Zombie(parent, lane);
        switch (type) {
            case "NormalZombie":
                z = new NormalZombie(parent, lane);
                break;
            case "ConeHeadZombie":
                z = new ConeHeadZombie(parent, lane);
                break;
            case "RunnerZombie":
                z = new RunnerZombie(parent, lane);
                break;
        }
        return z;
    }

    // Getters and setters for various attributes
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public GamePanel getGp() {
        return gp;
    }

    public void setGp(GamePanel gp) {
        this.gp = gp;
    }

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

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getSlowInt() {
        return slowInt;
    }

    public void setSlowInt(int slowInt) {
        this.slowInt = slowInt;
    }
}

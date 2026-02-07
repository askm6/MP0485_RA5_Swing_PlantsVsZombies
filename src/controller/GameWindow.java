package controller;

import view.PlantCard;
import view.Menu;
import view.GamePanel;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Armin on 6/25/2016.
 */
public class GameWindow extends JFrame {

    public enum PlantType {
        None(0),
        Sunflower(50),
        Peashooter(100),
        FreezePeashooter(175),
        Wallnut(50);

        private final int cost;

        // Constructor to set the cost
        PlantType(int cost) {
            this.cost = cost;
        }

        // Getter for the cost
        public int getCost() {
            return cost;
        }
    }

    //PlantType activePlantingBrush = PlantType.None;
    public GameWindow() {
        setSize(1012, 785);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel sun = new JLabel("SUN");
        sun.setLocation(37, 80);
        sun.setSize(60, 20);

        GamePanel gp = new GamePanel(sun);
        gp.setLocation(0, 0);
        getLayeredPane().add(gp, Integer.valueOf(0));

        PlantCard sunflower = new PlantCard(new ImageIcon(this.getClass().getResource("/images/cards/card_sunflower.png")).getImage());
        sunflower.setLocation(110, 8);
        sunflower.setToolTipText("Sunflower: Generates sun every 15 seconds.");
        sunflower.setAction((ActionEvent e) -> {
            gp.setActivePlantingBrush(PlantType.Sunflower);
        });
        getLayeredPane().add(sunflower, Integer.valueOf(3));

        PlantCard peashooter = new PlantCard(new ImageIcon(this.getClass().getResource("/images/cards/card_peashooter.png")).getImage());
        peashooter.setLocation(175, 8);
        peashooter.setToolTipText("Peashooter: Shoots peas at zombies.");
        peashooter.setAction((ActionEvent e) -> {
            gp.setActivePlantingBrush(PlantType.Peashooter);
        });
        getLayeredPane().add(peashooter, Integer.valueOf(3));

        PlantCard freezepeashooter = new PlantCard(new ImageIcon(this.getClass().getResource("/images/cards/card_freezepeashooter.png")).getImage());
        freezepeashooter.setLocation(240, 8);
        freezepeashooter.setToolTipText("Freeze Peashooter: Shoots slowing peas at zombies");
        freezepeashooter.setAction((ActionEvent e) -> {
            gp.setActivePlantingBrush(PlantType.FreezePeashooter);
        });
        getLayeredPane().add(freezepeashooter, Integer.valueOf(3));

        PlantCard wallnut = new PlantCard(new ImageIcon(this.getClass().getResource("/images/cards/card_wallnut.png")).getImage());
        wallnut.setLocation(305, 8);
        wallnut.setToolTipText("Wall-nut: Use to protect your other plants");
        wallnut.setAction((ActionEvent e) -> {
            gp.setActivePlantingBrush(PlantType.Wallnut);
        });
        getLayeredPane().add(wallnut, Integer.valueOf(3));

        getLayeredPane().add(sun, Integer.valueOf(2));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public GameWindow(boolean b) {
        Menu menu = new Menu();
        menu.setLocation(0, 0);
        setSize(1012, 785);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getLayeredPane().add(menu, Integer.valueOf(0));
        menu.repaint();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static GameWindow gw;

    public static void begin() {
        gw.dispose();
        gw = new GameWindow();
    }

    public static void main(String[] args) {
        gw = new GameWindow(true);
    }

}

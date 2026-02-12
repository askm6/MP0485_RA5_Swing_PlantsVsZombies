package view;

import dao.LevelData;
import model.Zombie;
import model.Sunflower;
import model.Plant;
import model.Peashooter;
import model.Pea;
import model.NormalZombie;
import model.FreezePeashooter;
import model.FreezePea;
import model.ConeHeadZombie;
import controller.GameWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import model.RunnerZombie;
import model.Wallnut;

/**
 * GamePanel class that manages the game board and its components such as
 * plants, zombies, and sun score.
 */
public class GamePanel extends JLayeredPane implements MouseMotionListener {

    private static GamePanel currentPanel;

    // Image variables for the background and different plant/zombie images
    private Image bgImage;
    private Image peashooterImage;
    private Image freezePeashooterImage;
    private Image sunflowerImage;
    private Image wallnutImage;
    private Image peaImage;
    private Image freezePeaImage;
    private Image normalZombieImage;
    private Image coneHeadZombieImage;
    private Image runnerZombieImage;

    // Collider objects to detect plant positions on the grid
    private Collider[] colliders;

    // Lists for storing zombies, peas, and suns in different lanes
    private ArrayList<ArrayList<Zombie>> laneZombies;
    private ArrayList<ArrayList<Pea>> lanePeas;
    private ArrayList<Sun> activeSuns;

    // Timers for regular game events such as redraw and advancing game states
    private Timer redrawTimer;
    private Timer advancerTimer;
    private Timer sunProducer;
    private Timer zombieProducer;

    // Sun scoreboard to display the player's sun score
    private JLabel sunScoreboard;

    // Plant type currently being placed by the player
    private GameWindow.PlantType activePlantingBrush = GameWindow.PlantType.None;

    // Mouse position variables for interaction
    private int mouseX, mouseY;

    // Current sun score
    private int sunScore;

    public int getSunScore() {
        return sunScore;
    }

    public void setSunScore(int sunScore) {
        this.sunScore = sunScore;
        sunScoreboard.setText(String.valueOf(sunScore)); // Update the scoreboard display
    }

    // Constructor for initializing the game panel
    public GamePanel(JLabel sunScoreboard) {
        progress = 0;  // Reset progress

        // 🔥 stop old panel if it exists
        if (currentPanel != null) {
            currentPanel.stopGame();
        }
        currentPanel = this;

        setSize(1000, 752);  // Set the size of the game panel
        setLayout(null);  // Set layout to null for absolute positioning
        addMouseMotionListener(this);  // Listen for mouse movement
        this.sunScoreboard = sunScoreboard;
        setSunScore(1000);  // Initialize the sun score

        // Load images for background, plants, zombies, and other game elements
        bgImage = new ImageIcon(this.getClass().getResource("/images/mainBG.png")).getImage();
        peashooterImage = new ImageIcon(this.getClass().getResource("/images/plants/peashooter.gif")).getImage();
        freezePeashooterImage = new ImageIcon(this.getClass().getResource("/images/plants/freezepeashooter.gif")).getImage();
        sunflowerImage = new ImageIcon(this.getClass().getResource("/images/plants/sunflower.gif")).getImage();
        wallnutImage = new ImageIcon(this.getClass().getResource("/images/plants/wallnut.gif")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("/images/pea.png")).getImage();
        freezePeaImage = new ImageIcon(this.getClass().getResource("/images/freezepea.png")).getImage();
        normalZombieImage = new ImageIcon(this.getClass().getResource("/images/zombies/NormalZombie.png")).getImage();
        coneHeadZombieImage = new ImageIcon(this.getClass().getResource("/images/zombies/ConeHeadZombie.png")).getImage();
        runnerZombieImage = new ImageIcon(this.getClass().getResource("/images/zombies/RunnerZombie.png")).getImage();

        // Initialize the lists for storing zombies and peas in different lanes
        laneZombies = new ArrayList<>();
        laneZombies.add(new ArrayList<>()); // lane 1
        laneZombies.add(new ArrayList<>()); // lane 2
        laneZombies.add(new ArrayList<>()); // lane 3
        laneZombies.add(new ArrayList<>()); // lane 4
        laneZombies.add(new ArrayList<>()); // lane 5

        lanePeas = new ArrayList<>();
        lanePeas.add(new ArrayList<>()); // lane 1
        lanePeas.add(new ArrayList<>()); // lane 2
        lanePeas.add(new ArrayList<>()); // lane 3
        lanePeas.add(new ArrayList<>()); // lane 4
        lanePeas.add(new ArrayList<>()); // lane 5

        // Initialize colliders for detecting plant positions on the grid
        colliders = new Collider[45];
        for (int i = 0; i < 45; i++) {
            Collider a = new Collider();
            a.setLocation(44 + (i % 9) * 100, 109 + (i / 9) * 120); // Position each collider on the grid
            a.setAction(new PlantActionListener((i % 9), (i / 9))); // Assign action listener for planting plants
            colliders[i] = a;
            add(a, Integer.valueOf(0));  // Add collider to the panel
        }

        activeSuns = new ArrayList<>(); // Initialize active suns list

        // Timer to update the game visuals regularly
        redrawTimer = new Timer(25, (ActionEvent e) -> {
            repaint();
        });
        redrawTimer.start();

        // Timer to advance the game state (move zombies, peas, etc.)
        advancerTimer = new Timer(60, (ActionEvent e) -> advance());
        advancerTimer.start();

        // Timer to periodically spawn sun items at random positions
        sunProducer = new Timer(5000, (ActionEvent e) -> {
            Random rnd = new Random();
            Sun sta = new Sun(this, rnd.nextInt(800) + 100, 0, rnd.nextInt(300) + 200);
            activeSuns.add(sta);
            add(sta, 1);  // Add sun to the panel
        });
        sunProducer.start();

        // Timer to periodically spawn zombies at random positions and in random lanes
        zombieProducer = new Timer(7000, (ActionEvent e) -> {
            Random rnd = new Random();
            LevelData lvl = new LevelData();
            String[] Level = lvl.LEVEL_CONTENT[Integer.parseInt(lvl.LEVEL_NUMBER) - 1];
            int[][] LevelValue = lvl.LEVEL_VALUE[Integer.parseInt(lvl.LEVEL_NUMBER) - 1];
            int l = rnd.nextInt(5);  // Randomly select a lane
            int t = rnd.nextInt(150);  // Random value for zombie type
            Zombie z = null;
            for (int i = 0; i < LevelValue.length; i++) {
                if (t >= LevelValue[i][0] && t <= LevelValue[i][1]) {
                    z = Zombie.getZombie(Level[i], GamePanel.this, l);  // Create a zombie based on level data
                }
            }
            laneZombies.get(l).add(z);  // Add the zombie to the selected lane
        });
        zombieProducer.start();
    }

    // Advances the state of zombies, peas, and suns (e.g., moving them forward)
    private void advance() {
        for (int i = 0; i < 5; i++) {
            for (Zombie z : laneZombies.get(i)) {
                z.advance();  // Move the zombie forward
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                p.advance();  // Move the pea forward
            }

        }

        // Advance all active suns
        for (int i = 0; i < activeSuns.size(); i++) {
            activeSuns.get(i).advance();
        }
    }

    // Paints the game elements on the screen (plants, zombies, peas, etc.)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);  // Draw the background
        // 🔥 draw level number
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(new Color(255, 215, 0));
        g.drawString("Level " + LevelData.LEVEL_NUMBER, 875, 20);

        // Draw plants
        for (int i = 0; i < 45; i++) {
            Collider c = colliders[i];
            if (c.assignedPlant != null) {
                Plant p = c.assignedPlant;
                if (p instanceof Peashooter) {
                    g.drawImage(peashooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof FreezePeashooter) {
                    g.drawImage(freezePeashooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof Sunflower) {
                    g.drawImage(sunflowerImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (p instanceof Wallnut) {
                    g.drawImage(wallnutImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
            }
        }

        // Draw zombies
        for (int i = 0; i < 5; i++) {
            for (Zombie z : laneZombies.get(i)) {
                if (z instanceof NormalZombie) {
                    g.drawImage(normalZombieImage, z.getPosX(), 109 + (i * 120), null);
                } else if (z instanceof ConeHeadZombie) {
                    g.drawImage(coneHeadZombieImage, z.getPosX(), 109 + (i * 120), null);
                } else if (z instanceof RunnerZombie) {
                    g.drawImage(runnerZombieImage, z.getPosX(), 109 + (i * 120), null);
                }
            }

            // Draw peas
            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea pea = lanePeas.get(i).get(j);
                if (pea instanceof FreezePea) {
                    g.drawImage(freezePeaImage, pea.getPosX(), 130 + (i * 120), null);
                } else {
                    g.drawImage(peaImage, pea.getPosX(), 130 + (i * 120), null);
                }
            }
        }
    }

    // Action listener for planting plants in the grid
    private class PlantActionListener implements ActionListener {

        int x, y;

        public PlantActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activePlantingBrush != GameWindow.PlantType.None) {
                int cost = activePlantingBrush.getCost();  // Get the cost of the selected plant

                // Check if the player has enough sun score to plant the selected plant
                if (getSunScore() >= cost) {
                    // Create the plant based on the selected plant type
                    switch (activePlantingBrush) {
                        case Sunflower:
                            colliders[x + y * 9].setPlant(new Sunflower(GamePanel.this, x, y));
                            break;
                        case Peashooter:
                            colliders[x + y * 9].setPlant(new Peashooter(GamePanel.this, x, y));
                            break;
                        case FreezePeashooter:
                            colliders[x + y * 9].setPlant(new FreezePeashooter(GamePanel.this, x, y));
                            break;
                        case Wallnut:
                            colliders[x + y * 9].setPlant(new Wallnut(GamePanel.this, x, y));
                            break;
                        default:
                            break;
                    }

                    // Deduct the sun score for the selected plant
                    setSunScore(getSunScore() - cost);
                }
            }
            activePlantingBrush = GameWindow.PlantType.None;  // Reset the planting brush
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used, but required for MouseMotionListener
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();  // Update mouse position
        mouseY = e.getY();
    }

    static int progress = 0;

    // Updates progress and handles level transitions
    public static void setProgress(int num) {
        progress = progress + num;

        if (progress >= 150) {
            if ("1".equals(LevelData.LEVEL_NUMBER) || "2".equals(LevelData.LEVEL_NUMBER)) {
                JOptionPane.showMessageDialog(null, "Level " + LevelData.LEVEL_NUMBER + " Completed!" + '\n' + "Starting next level");
                currentPanel.stopGame();
                GameWindow.gw.dispose();  // Dispose the current window

                if ("1".equals(LevelData.LEVEL_NUMBER)) {
                    LevelData.write("2");  // Move to next level

                } else {
                    LevelData.write("3");  // Move to next level
                }

                GameWindow.gw = new GameWindow();  // Open a new game window

            } else {
                LevelData.write("1");  // Reset to the first level
                showFinalInput();
                System.exit(0);  // Exit the game
            }
        }
    }

    private void stopGame() {
        redrawTimer.stop();
        advancerTimer.stop();
        sunProducer.stop();
        zombieProducer.stop();
    }

    // Getter and setter methods for active plant brush and lane data
    public GameWindow.PlantType getActivePlantingBrush() {
        return activePlantingBrush;
    }

    public void setActivePlantingBrush(GameWindow.PlantType activePlantingBrush) {
        this.activePlantingBrush = activePlantingBrush;
    }

    public ArrayList<ArrayList<Zombie>> getLaneZombies() {
        return laneZombies;
    }

    public void setLaneZombies(ArrayList<ArrayList<Zombie>> laneZombies) {
        this.laneZombies = laneZombies;
    }

    public ArrayList<ArrayList<Pea>> getLanePeas() {
        return lanePeas;
    }

    public void setLanePeas(ArrayList<ArrayList<Pea>> lanePeas) {
        this.lanePeas = lanePeas;
    }

    public ArrayList<Sun> getActiveSuns() {
        return activeSuns;
    }

    public void setActiveSuns(ArrayList<Sun> activeSuns) {
        this.activeSuns = activeSuns;
    }

    public Collider[] getColliders() {
        return colliders;
    }

    public void setColliders(Collider[] colliders) {
        this.colliders = colliders;
    }

    /*
     * Handles the final interaction when the player reaches the escape room.
     * 
     * The entered number is combined with a predefined key to generate the
     * decoding key used to decrypt a hidden message stored in an external file.
     * If the decoding process is successful, a congratulatory message and the
     * decoded secret are displayed in the terminal.
     * 
     * If an error occurs (invalid input, missing file, or incorrect decoding key),
     * an error message is shown and the stack trace is printed to assist in
     * debugging during development.
     */
    private static void showFinalInput() {
        String input = JOptionPane.showInputDialog(
                null,
                "You completed all levels, CONGRATULATIONS! "
                + "Enter the final number\n"
                + "(Hint: sum of the 3 previous lockers)",
                "Final Exit",
                JOptionPane.QUESTION_MESSAGE
        );

        try {
            int number = Integer.parseInt(input.trim());
            String key = "roomescape-key-" + number;
            String message = decodeSecret("secret.txt", key);

            JOptionPane.showMessageDialog(null, message);

        } catch (Exception e) {
            System.err.println("Error decoding final lock");
            e.printStackTrace();
        }
    }

    /*
     * Decodes a hidden secret message stored in an external file using a key
     * derived from the player's final input.
     * 
     * The file content is expected to be a hexadecimal representation of data
     * previously encrypted using an XOR operation combined with a SHA-256 based
     * key. During execution, the method reverses this process to recover the
     * original message.
     * 
     * This mechanism is used to prevent the secret message from being readable
     * in plain text, requiring successful completion of the final puzzle in
     * order to reveal the information.
     * 
     * @param filePath path to the file containing the encrypted secret
     * @param key      decoding key generated from the player's input
     * @return the decoded secret message as a UTF-8 string
     * @throws Exception if the file cannot be read or the decoding process fails
     */
    private static String decodeSecret(String filePath, String key) throws Exception {
        String hexData = new String(Files.readAllBytes(Paths.get(filePath))).trim();
        byte[] encrypted = hexToBytes(hexData);
        byte[] keyHash = sha256(key);

        byte[] decrypted = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = (byte) (encrypted[i] ^ keyHash[i % keyHash.length]);
        }

        return new String(decrypted, "UTF-8");
    }

    private static byte[] sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes("UTF-8"));
    }

    private static byte[] hexToBytes(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            int index = i * 2;
            data[i] = (byte) Integer.parseInt(hex.substring(index, index + 2), 16);
        }
        return data;
    }
}

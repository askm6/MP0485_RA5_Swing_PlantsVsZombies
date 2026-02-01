package dao;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LevelData {

    public static String LEVEL_NUMBER = "1";
    public static final String[][] LEVEL_CONTENT = {
        {"NormalZombie"},
        {"NormalZombie", "ConeHeadZombie"},
        {"NormalZombie", "ConeHeadZombie","RunnerZombie"}
    };
    public static final int[][][] LEVEL_VALUE = {
        {{0, 150}},
        {{0, 75}, {76, 150}},
        {{0, 49}, {50, 99},{100, 150}}
    };

    private static final File LEVEL_FILE = new File("LEVEL_CONTENT.vbhv");

    public LevelData() {
        if (!LEVEL_FILE.exists()) {
            write("1");
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(LEVEL_FILE))) {
                String line = br.readLine();
                if (line != null && !line.isBlank()) {
                    LEVEL_NUMBER = line.trim();
                }
            } catch (IOException ex) {
                Logger.getLogger(LevelData.class.getName()).log(Level.SEVERE, "Error reading level file", ex);
            }
        }
    }

    public static void write(String lvl) {
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(LEVEL_FILE))) {
            bwr.write(lvl);
            LEVEL_NUMBER = lvl;
        } catch (IOException ex) {
            Logger.getLogger(LevelData.class.getName()).log(Level.SEVERE, "Error writing level file", ex);
        }
    }
}

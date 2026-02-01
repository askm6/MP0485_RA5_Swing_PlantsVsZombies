/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import view.GamePanel;

/**
 *
 * @author JoseAlbertoPortugalO
 */
public class Wallnut extends Plant {
    
    public Wallnut(GamePanel parent, int x, int y) {
        super(parent, x, y);
        
        // Set higher speed than a normal plant
        setHealth(2000);
    }
    
}

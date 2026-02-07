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
public class RunnerZombie extends Zombie {

    public RunnerZombie(GamePanel parent, int lane) {
        super(parent, lane);

        // Set higher speed than a normal zombie
        setSpeed(3);
    }

}

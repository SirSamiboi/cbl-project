// import java.awt.*;

import javax.swing.*;


class Main {
    JFrame frame;
    JPanel panel;

    /**
     * Creates the window for the game.
     */
    void createWindow() {
        frame = new JFrame("Defender");
        panel = new JPanel();

        frame.setSize(800, 600);
        frame.setLocation(100, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setVisible(true);


    }

    /**
     * No.
     */
    void drawMapOne() {
        new SetupMap1(frame, panel).setUp();

    }

    void run() {
        createWindow();
        drawMapOne();
    }

    public static void main(String[] args) {
        Main boop = new Main();
        boop.run();
    }
}



/*
 * MY PHONE RESETTED TO FACTORY DEFAULTS AND I ALMOST LOST ACCESS TO MY whatsapp
 * I am doing my best to recover it, but google is being google, all 2-factor was tied up on,
 * well, the phone. 
 * 
 * Help ._.
 */
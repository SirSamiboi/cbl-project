import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The Main class contains the GamePanel in which all game logic takes place, and maintains the
 * game's update speed, through the use of a Timer object which allows for the GamePanel's
 * updateGame function to be called once every 33ms, for a target of 30 frames per second. This
 * time unit of 33ms will henceforth be referred to as a "tick". All of the game's calculations
 * occur on steps of 33ms, i.e. every tick.
 */

class Main {
    static void run() {
        JFrame frame = new JFrame("Magic Defender");
        GamePanel panel = new GamePanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);

        // Setup game timer
        // The game runs at a targeted 30 FPS
        // In theory, 1000ms / 30FPS = 33ms per game tick is the target
        // In practice, using 1000ms / 33ms = 30ms per tick acts closer to 30FPS
        int delay = 1000 / 33;

        Timer gameTimer = new Timer(delay, new ActionListener() {
            public int gameState = 1; // 0 for not started, 1 for playing, 2 for win, 3 for loss

            @Override
            public void actionPerformed(ActionEvent e) {
                switch (gameState) {
                    case 0: // draw the starting screen
                        
                        break;
                    case 1: //draw the game frame  
                        panel.updateGame();
                        break;
                    case 2: // draw the victory screen

                        break;
                    case 3: //draw the loss screen

                        break;
                    default:
                        System.out.println("Womp Womp");
                        System.out.println("Game State is not game stating");
                }
                
            }
        });

        gameTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::run);
    }
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


class Main {
    static void run() {
        

        JFrame frame = new JFrame("Defender");
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
        // In practice, using 1000ms / 33ms = 30ms per tick is closer to 30FPS
        int delay = 1000 / 33;

        Timer gameTimer = new Timer(delay, new ActionListener() {
            @Override
            public int gameState = 0; // 0 for not started, 1 for in progress, 2 for win, 3 for loss.

            public void actionPerformed(ActionEvent e) {
                switch (gameState) {
                    case 0: // draw starting screen
                        
                        break;
                    case 1: //draw game in progress tick
                        panel.updateGame();
                        break;
                    case 2: // draw victory screen

                        break;
                    case 3: //draw losing screen

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

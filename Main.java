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
        frame.pack(); // Size the frame based on the panel's preferred size (800x640)
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);

        // Setup game timer
        // The game runs at a targeted 30 FPS, in practice this is 1000 / 33 = 30.3030... FPS
        int delay = 1000 / 30;

        Timer gameTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.updateGame();
            }
        });

        gameTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::run);
    }
}


// class Main {
//     JFrame frame;
//     JPanel panel;

//     /**
//      * Creates the window for the game.
//      */
//     void createWindow() {
//         frame = new JFrame("Defender");
//         panel = new JPanel();

//         frame.setSize(800, 640);
//         frame.setLocation(100, 50);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setContentPane(panel);
//         frame.setVisible(true);


//     }

//     /**
//      * No.
//      */
//     void drawMapOne() {
//         new SetupMap1(frame, panel).setUp();
//     }

//     void run() {
//         createWindow();
//         drawMapOne();
//     }

//     public static void main(String[] args) {
//         new Main().run();
//     }
// }
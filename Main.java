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
        // The game runs at a targeted 30 FPS
        // In theory, 1000ms / 30FPS = 33ms per game tick is the target
        // In practice, using 1000ms / 33ms = 30.3030... ms per tick is closer to 30FPS
        int delay = 1000 / 33;

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

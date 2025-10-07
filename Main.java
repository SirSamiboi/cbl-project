import javax.swing.*;

class Main {
    JFrame frame;
    JPanel panel;

    void createWindow() {
        frame = new JFrame("Defender");
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    void run() {
        createWindow();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
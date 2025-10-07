import javax.swing.*;

class Main {
    JFrame frame;
    JPanel panel;

    void createWindow() {
        frame = new JFrame("Test");
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    void run() {
        createWindow();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class GamePanel extends JPanel implements MouseListener {
    private BufferedImage mapImage;

    private int elementX = 50; // Example element position
    private int elementY = 50;
    private int dx = 2;        // Example element speed

    private int lastClickX = -1;
    private int lastClickY = -1;

    private Tower[] towerList = new Tower[100];
    // 100 is arbitrary, real max is decided by following variable

    // Other fields

    /**
     * The panel element which contains all game elements shown on-screen.
     */
    public GamePanel() {
        // Load map image if possible
        try {
            mapImage = ImageIO.read(new File("assets/map1.png"));
            
        } catch (IOException e) {
            System.out.println("Womp womp");
            System.out.println(e);
            mapImage = null;
        }
        
        // Register GamePanel to receive mouse events
        this.addMouseListener(this);
        // Set dimensions of window (800x640)
        this.setPreferredSize(new Dimension(800, 640));
        // Fallback color if map image cannot load
        this.setBackground(Color.WHITE);
    }

    /**
     * Game update logic, called by Timer.
     */
    public void updateGame() {
        // Enter game logic here
        // Example:
        elementX += dx;
        if (elementX > getWidth() - 50 || elementX < 0) {
            dx = -dx;
        }

        // Renders next frame
        repaint();
    }

    /**
     * Handles all drawing. Called via repaint().
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Draw map image
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw other elements
        // Example:
        g2d.setColor(Color.CYAN);
        g2d.fillOval(elementX, elementY, 50, 50); // Draw a 50x50 circle
        
        // Example: Draw round indicator
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Round 1/10", 10, 20);
    }

    /**
     * Called when the mouse button is pressed and released at the same location.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }

    /**
     * Called when a mouse button is pressed.
     */
    @Override
    public void mousePressed(MouseEvent e) { }

    /**
     * Called when a mouse button is released.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        lastClickX = e.getX();
        lastClickY = e.getY();
        
        System.out.println("Click registered at: (" + lastClickX + ", " + lastClickY + ")");
    }

    /**
     * Called when the mouse cursor enters the window.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Called when the mouse cursor exits the window.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}

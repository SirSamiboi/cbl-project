import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

class GamePanel extends JPanel implements MouseListener {
    private BufferedImage mapImage;

    private int lastClickX = -1;
    private int lastClickY = -1;

    public ArrayList<Tower> towerList = new ArrayList<>();
    public ArrayList<Enemy> enemyList = new ArrayList<>();
    public byte[][] perWaveEnemyTypes = {
        {0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    /**
     * The panel element which contains all game elements shown on-screen.
     * This constructor also initializes the game variables.
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

        // Placing towers
        towerList.add(new BasicTower(144, 224));
        towerList.add(new BasicTower(336, 192));
        towerList.add(new BasicTower(528, 224));
        towerList.add(new BasicTower(720, 160));
        towerList.add(new BasicTower(272, 416));
        towerList.add(new BasicTower(80, 576));
        towerList.add(new BasicTower(432, 576));
        towerList.add(new BasicTower(656, 448));


        //TEST!!! ADD ENEMY TO THE LIST
        enemyList.add(new Goblin(0, 128));
        enemyList.add(new Goblin(5, 128));
        enemyList.add(new Goblin(-5, 128));
        System.out.println(enemyList.get(0).posX);
        System.out.println(enemyList.get(0).posY);
        //END TEST
    }

    /**
     * Game update logic, called by Timer.
     */
    public void updateGame() {
        // Process ticks for towers
        for (Tower tower : towerList) {
            tower.tick(enemyList);
        }

        // Process ticks for enemies

        // ERROR OCCURS IN THIS FOR LOOP

        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).tick(enemyList);

            if (enemyList.get(i).getHp() <= 0) {
                enemyList.get(i).die(enemyList);
                i -= 1;
            }
        }

        // Remove any defeated enemies from enemyList
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i) == null) {
                enemyList.remove(i);
                System.out.println("removed " + i);
                i -= 1;
            }
        }

        // Renders next frame
        repaint();
    }

    /**
     * Handles all drawing. Called via repaint().
     */
    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Rendering hints, if program gets optimised
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        //     RenderingHints.VALUE_ANTIALIAS_ON);
        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        //     RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        // Draw map image
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        // Draw all towers
        for (Tower tower : towerList) {
            g2d.drawImage(tower.getImage(), tower.getImageX(), tower.getImageY(),
                tower.getImageWidth(), tower.getImageHeight(), this);
        }

        // Draw range visuals
        for (Tower tower : towerList) {

        }
        
        // Draw all enemies
        for (Enemy enemy : enemyList) {
            g2d.drawImage(enemy.getImage(), enemy.getPosX() - 40, enemy.getPosY() - 40,
                 80, 80, this);
        }

        // Draw round number label (not functional)
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

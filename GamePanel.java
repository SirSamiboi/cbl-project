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

    private int selectedTower = -1; // -1 when nothing selected, otherwise index of tower in list
    private UpgradeButton upgradeButton = new UpgradeButton();
    private int globalTimer = 0; // Counts the total number of ticks elapsed
    
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
            System.out.println("ERROR: Map image could not be loaded");
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
        towerList.add(new Tower(144, 224));
        towerList.add(new Tower(336, 192));
        towerList.add(new BasicTower(528, 224));
        towerList.add(new Tower(720, 160));
        towerList.add(new BasicTower(272, 416));
        towerList.add(new Tower(80, 576));
        towerList.add(new Tower(432, 576));
        towerList.add(new BasicTower(656, 448));
    }

    /**
     * Game update logic, called by Timer.
     */
    public void updateGame() {
        //TEST: Place enemies at certain times
        switch (globalTimer) {
            case 0 -> enemyList.add(new Goblin(0, 128));
            case 15 -> enemyList.add(new Goblin(0, 123));
            case 30 -> enemyList.add(new Goblin(0, 133));
            case 45 -> enemyList.add(new Goblin(0, 128));

            case 75 -> enemyList.add(new Goblin(0, 128));
            case 85 -> enemyList.add(new Goblin(0, 133));
            case 95 -> enemyList.add(new Goblin(0, 123));

            case 110 -> enemyList.add(new Goblin(0, 123));
            case 112 -> enemyList.add(new Goblin(0, 128));
            case 114 -> enemyList.add(new Goblin(0, 133));
            case 116 -> enemyList.add(new Goblin(0, 123));
            case 118 -> enemyList.add(new Goblin(0, 133));
            case 120 -> enemyList.add(new Goblin(0, 128));
            case 122 -> enemyList.add(new Goblin(0, 128));
            case 124 -> enemyList.add(new Goblin(0, 123));
            case 126 -> enemyList.add(new Goblin(0, 128));
            case 128 -> enemyList.add(new Goblin(0, 133));

            default -> { }
        }
        // TEST: Place/upgrade towers at certain times
        switch (globalTimer) {
            case 30 -> towerList.get(0).levelUp(); // do nothing
            case 60 -> towerList.get(1).placeBasic(towerList); // place basic tower
            case 90 -> towerList.get(2).levelUp(); // upgrade tower to level 1
            case 120 -> towerList.get(4).levelUp(); // upgrade tower to level 1
            case 150 -> towerList.get(4).levelUp(); // upgrade tower to level 2
            case 180 -> towerList.get(7).levelUp(); // upgrade tower to level 1
            case 210 -> towerList.get(7).levelUp(); // upgrade tower to level 2
            case 240 -> towerList.get(6).placeBasic(towerList); // place basic tower
            case 270 -> towerList.get(7).levelUp(); // do nothing

            default -> { }
        }

        // Process ticks for towers
        for (Tower tower : towerList) {
            tower.tick(enemyList);
        }

        // Process ticks for enemies
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).tick(enemyList);

            if (enemyList.get(i).getHp() <= 0) {
                enemyList.get(i).die(enemyList);
                i -= 1;
            }
        }

        // Render next frame
        repaint();

        // Increment global timer by 1
        globalTimer++;
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

        // Draw all enemies
        for (Enemy enemy : enemyList) {
            g2d.drawImage(enemy.getImage(), enemy.getPosX() - 40, enemy.getPosY() - 40,
                 80, 80, this);
        }

        // Draw all towers
        for (Tower tower : towerList) {
            g2d.drawImage(tower.getImage(), tower.getImageX(), tower.getImageY(),
                tower.getImageWidth(), tower.getImageHeight(), this);
        }

        // Draw range visuals
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setStroke(new BasicStroke(2));

        for (Tower tower : towerList) {
            int range = tower.getRange();
            g2d.drawOval(tower.getPosX() - range, tower.getPosY() - range, range * 2, range * 2);
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

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

        boolean anySelected = false;
        int i = 0;

        do {
            Tower tower = towerList.get(i);

            if (tower.isClicked(lastClickX, lastClickY)) {
                // If block runs when tower is clicked
                if (selectedTower != i) {
                    System.out.println("Tower " + i + " has been selected");
                }

                anySelected = true;
                selectedTower = i;
            }

            i += 1;
        } while (i < towerList.size() && !anySelected);

        if (!anySelected) {
            // If block runs when tower is deselected
            if (selectedTower != -1 && !anySelected) {
                System.out.println("Tower " + selectedTower + " has been deselected");
            }

            selectedTower = -1;
        }

        // Assigns the upgrade button to the selected tower
        upgradeButton.assign(towerList, selectedTower);
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

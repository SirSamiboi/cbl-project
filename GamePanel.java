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
    Random random = new Random();
    Player playerOne = new Player();

    private BufferedImage mapImage;

    private int lastClickX = -1;
    private int lastClickY = -1;

    private int selectedTower = -1; // -1 when nothing selected, otherwise index of tower in list
    private int selectedButton = -1;
    private int globalTimer = 0; // Counts the total number of ticks elapsed
    
    public ArrayList<Tower> towerList = new ArrayList<>();
    public ArrayList<Enemy> enemyList = new ArrayList<>();
    public UpgradeButton[] upgradeButtonList = {
        new UpgradeButton(-1, 0, 64),
        new UpgradeButton(0, 64, -32)
    };

    public int waveNumber = 0;
    public int lengthOfTheWave;
    public byte[][] perWaveEnemyTypes = {
        {},
        {0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private int waveEmptyTime; // This variable updates if the enemyList is empty
    // All enemies are dead
    private int waveDelayTime = 150; // 5 second delay before the next wave starts
    private int nextWaveTime; // the time in ticks when the next wave should start
    private ArrayList<Integer> enemySpawnTimes = new ArrayList<>();
    private int enemiesSpawnedInTheWave;

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
        towerList.add(new Tower(528, 224));
        towerList.add(new Tower(720, 160));
        towerList.add(new Tower(272, 416));
        towerList.add(new Tower(80, 576));
        towerList.add(new Tower(432, 576));
        towerList.add(new Tower(656, 448));
    }

    /**
     * Game update logic, called by Timer.
     */
    public void updateGame() {
        //TEST: Place enemies at certain times

        //Starting a new wave
        if (enemyList.isEmpty() && globalTimer % 300 == 0) { // if all enemies are dead
            enemySpawnTimes.clear(); // resets the spawn timings for the next wave.
            waveNumber += 1; // moves the wave counter to the next wave

            if (waveNumber > 5) {
                System.out.println("All waves beat");
                // TODO: Victory Screen
            }

            waveEmptyTime = globalTimer;
            nextWaveTime = waveEmptyTime + waveDelayTime; // Set the time, when the next wave starts
            lengthOfTheWave = perWaveEnemyTypes[waveNumber].length;
            enemiesSpawnedInTheWave = 0;

            for (int i = 0; i < lengthOfTheWave; i++) {
                // Setup the spawn times for all enemies of the next wave
                enemySpawnTimes.add(nextWaveTime + i * 15);
            }
        }

        if (enemySpawnTimes.contains(globalTimer)) {
            switch (perWaveEnemyTypes[waveNumber][enemiesSpawnedInTheWave]){
                case (byte) 0 -> enemyList.add(new Goblin(0, 125 + (random.nextInt(11) - 5)));
                default -> { }
            }
            enemiesSpawnedInTheWave += 1;
        }

        // Process ticks for towers
        for (Tower tower : towerList) {
            tower.tick(enemyList);
        }

        // Process ticks for enemies
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            enemy.tick(enemyList, playerOne);

            if (enemy.getHp() <= 0) {
                playerOne.setMoney(playerOne.getMoney() + enemy.getMoney());
                enemy.die(enemyList);
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

        if (selectedTower != -1) {
            Tower tower = towerList.get(selectedTower);

            // Draw range visual
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.GRAY);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setStroke(new BasicStroke(2));

            int range = tower.getRange();
            g2d.drawOval(tower.getPosX() - range, tower.getPosY() - range, range * 2, range * 2);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Draw buttons
            for (UpgradeButton ub : upgradeButtonList) {
                if (ub.shouldDraw()) {
                    int ubX = ub.getImageX();
                    int ubY = ub.getImageY();
                    int ubW = ub.getWidth();
                    int ubH = ub.getHeight();

                    g2d.setColor(ub.getColor());
                    g2d.fillRect(ubX, ubY, ubW, ubH);
                    
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(ubX, ubY, ubW, ubH);

                    g2d.setFont(new Font("Arial", Font.BOLD, 10));
                    g2d.drawString(ub.getText(), ubX + 4, ubY + ubH / 2 - 4);

                    if (ub.getType() == -1) { // Upgrade cost
                        g2d.drawString("Cost: " + tower.getUpgradeCost(),
                            ubX + 4, ubY + ubH / 2 + 12);
                    } else { // Build cost
                        g2d.drawString("Cost: " + ub.getBuildCost(),
                            ubX + 4, ubY + ubH / 2 + 12);
                    }
                }
            }
        }

        // Draw round number label (not functional)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(String.format("Wave %d / 5", waveNumber), 10, 20);
        g2d.drawString(String.format("Player HP: %d", playerOne.getPlayerHp()), 10, 40);
        g2d.drawString(String.format("Money™: %d", playerOne.getMoney()), 675, 20);
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
        
        // Logic for button clicks
        boolean anyButtonClicked = false;
        int i = 0;

        do {
            UpgradeButton ub = upgradeButtonList[i];

            if (ub.isClicked(lastClickX, lastClickY)) {
                // If block runs when Button is clicked
                if (selectedButton != i) {
                    System.out.println("Button " + i + " has been clicked");
                }

                anyButtonClicked = true;
                ub.click(towerList, playerOne);
            }

            i += 1;
        } while (i < upgradeButtonList.length && !anyButtonClicked);

        // Logic for tower clicks
        boolean anyTowerSelected = anyButtonClicked;
        i = 0;

        do {
            Tower tower = towerList.get(i);

            if (tower.isClicked(lastClickX, lastClickY)) {
                // If block runs when tower is clicked
                if (selectedTower != i) {
                    System.out.println("Tower " + i + " has been selected");
                }

                anyTowerSelected = true;
                selectedTower = i;
            }

            i += 1;
        } while (i < towerList.size() && !anyTowerSelected);

        if (!anyTowerSelected) {
            // If block runs when tower is deselected
            if (selectedTower != -1 && !anyTowerSelected) {
                System.out.println("Tower " + selectedTower + " has been deselected");
            }

            selectedTower = -1;
        }

        // Assign upgrade buttons to the selected tower
        for (UpgradeButton ub : upgradeButtonList) {
            ub.assign(towerList, selectedTower);
        }
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

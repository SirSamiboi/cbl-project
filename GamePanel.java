import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GamePanel is an extension of JPanel in which a game round is fully contained and controlled.
 * This includes processing game logic, handling user input and updating the screen every tick.
 */

class GamePanel extends JPanel implements MouseListener {
    Random random = new Random();
    Player playerOne = new Player(); // Used to store player statistics

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
        new UpgradeButton(0, 68, -32),
        new UpgradeButton(1, 76, 16)
    };
    public ArrayList<Animation> animationList = new ArrayList<>();

    public int waveNumber = 0;
    public int waveLength;
    // IDs of all enemies that will be spawned each wave
    public byte[][] perWaveEnemyTypes = {
        {},
        {0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    // Timestamps of when enemies spawn each wave, relative to the last enemy, in ticks
    // Index 0 is usually 0 because the first enemy spawns as soon as the wave begins
    public int[][] perWaveSpawnIntervals = {
        {},
        {60, 30},
        {0, 15, 15, 15}, // Here a goblin enemy will spawn at 0, 15, 30, 45 ticks
        {0, 10, 10, 10, 30, 10, 10, 10},
        {0, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4},
        {0, 15, 15, 15, 15, 15, 15, 15, 15, 15, 30, 10, 10, 10, 10, 10, 10, 10, 10, 10}
    };
    private int waveEmptyTime; // Timestamp of when a wave ended
    private int waveDelayTime = 120; // 4 second delay before the next wave starts
    private int nextWaveTime; // Timestamp of when the next wave should start
    private ArrayList<Integer> enemySpawnTimes = new ArrayList<>();
    private int waveEnemiesSpawned;

    /**
     * The panel element which contains all game elements shown on-screen.
     * This constructor also initializes the game variables.
     */
    public GamePanel() {
        // Load map image if possible
        try {
            this.mapImage = ImageIO.read(new File("assets/map1.png"));
            
        } catch (IOException e) {
            System.out.println("ERROR: Map image could not be loaded");
            System.out.println(e);
            this.mapImage = null;
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
        //Starting a new wave
        if (enemyList.isEmpty() && waveEnemiesSpawned == waveLength) { // if all enemies are dead
            enemySpawnTimes.clear(); // resets the spawn timings for the next wave.
            waveNumber += 1; // moves the wave counter to the next wave

            if (waveNumber > 5) {
                System.out.println("All waves beat");
                // TODO: Victory Screen
            }

            waveEmptyTime = globalTimer;
            // Sets the time at which the next wave will start
            nextWaveTime = waveEmptyTime + waveDelayTime;
            waveLength = perWaveEnemyTypes[waveNumber].length;
            waveEnemiesSpawned = 0;
            // Setup the spawn times for all enemies of the next wave
            int enemySpawnTime = nextWaveTime;

            for (int enemySpawnDelay : perWaveSpawnIntervals[waveNumber]) {
                enemySpawnTime += enemySpawnDelay;
                enemySpawnTimes.add(enemySpawnTime);
            }
        }

        // If block runs when an enemy is to be spawned
        // Does not support multiple enemies spawning at once
        if (enemySpawnTimes.contains(globalTimer)) {
            // Spawns the enemy with the matching ID
            switch (perWaveEnemyTypes[waveNumber][waveEnemiesSpawned]) {


                case (byte) 0 -> enemyList.add(new Goblin(0, 125 + (random.nextInt(11) - 5)));

                default -> { }
            }
            waveEnemiesSpawned += 1;
        }

        // Process ticks for towers
        for (Tower tower : towerList) {
            tower.tick(enemyList, animationList);
        }

        // Process ticks for enemies
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            enemy.tick(enemyList, playerOne);

            if (enemy.getHp() <= 0) {
                playerOne.setMoney(playerOne.getMoney() + enemy.getMoney());
                enemy.die(enemyList, animationList);
                i -= 1;
            }
        }

        // Remove finished animations
        for (int i = 0; i < animationList.size(); i++) {
            Animation animation = animationList.get(i);
            
            if (animation.getTimer() >= animation.getDuration()) {
                animationList.remove(animation);
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

        // Rendering hints
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Prioritise rendering speed
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_SPEED);

        // Smoothen the pixel art, since the program does not truly run in 800x640 in practice
        // This was discovered late into development, so to be completely honest,
        // we did not consider it worth fixing
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);     

        // Draw map image
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw enemies
        for (Enemy enemy : enemyList) {
            g2d.drawImage(enemy.getImage(), enemy.getImageX(), enemy.getImageY(),
                 enemy.getImageWidth(), enemy.getImageHeight(), this);
        }

    
        // Draw towers
        for (Tower tower : towerList) {
            g2d.drawImage(tower.getImage(), tower.getImageX(), tower.getImageY(),
                tower.getImageWidth(), tower.getImageHeight(), this);
        }


        // Draw animations
        for (int i = 0; i < animationList.size(); i++) {
            int[] val = animationList.get(i).step();
            
            switch (animationList.get(i).getId().substring(0, 1)) {
                // Basic tower attack (see BasicAnimation class for index values)
                case "0" -> {
                    // Outer beam
                    g2d.setColor(new Color(val[6], val[7], val[8]));
                    g2d.setStroke(new BasicStroke(val[4]));
                    g2d.drawLine(val[0], val[1], val[2], val[3]);
                    // Inner beam
                    g2d.setColor(new Color(192 + val[6] / 4, 192 + val[7] / 4, 192 + val[8] / 4));
                    g2d.setStroke(new BasicStroke(val[5]));
                    g2d.drawLine(val[0], val[1], val[2], val[3]);
                }

                // Fireball tower attack (see FireballAnimation)
                case "1" -> {
                    // Outer beam
                    g2d.setColor(new Color(val[6], val[7], val[8]));
                    g2d.setStroke(new BasicStroke(val[4]));
                    g2d.drawLine(val[0], val[1], val[2], val[3]);
                    // Explosion circle
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2d.fillOval(val[2] - val[9] / 2, val[3] - val[9] / 2, val[9], val[9]);
                    g2d.fillOval(val[2] - val[9] / 2 + 20, val[3] - val[9] / 2 + 20,
                            val[9] - 40, val[9] - 40);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                    
                    // Inner beam (unused)
                    // g2d.setColor(new Color(val[6] * 7 / 8, val[7] * 7 / 8, val[8] * 7 / 8));
                    // g2d.setStroke(new BasicStroke(val[5]));
                    // g2d.drawLine(val[0], val[1], val[2], val[3]);
                }
                
                // Enemy death animation (see DeathAnimation)
                case "X" -> {
                    g2d.setColor(new Color(255, 255, 128));
                    g2d.setFont(new Font("Arial", Font.BOLD, val[3]));
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            (float) val[4] / 100));
                    g2d.drawString("+" + val[2] + "G", val[0] - val[3], val[1] - val[3]);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }

                default -> { }
            }
        }

        // Draw health bars
        for (Enemy enemy : enemyList) {
            g2d.setColor(Color.BLACK);
            int posX = enemy.getPosX();
            int posY = enemy.getPosY();
            // Health bar size increases logarithmically with enemy health
            int width = (int) Math.pow(6 * enemy.getMaxHp() + 1, 0.6);
            g2d.fillRect(posX - width / 2, posY - enemy.getImageHeight(), width, 10);
            g2d.setColor(Color.RED);
            g2d.fillRect(posX - width / 2 + 2, posY - enemy.getImageHeight() + 2,
                (width - 4) * enemy.getHp() / enemy.getMaxHp(), 6);
        }

        // These are drawn while a tower is selected
        if (selectedTower != -1) {
            Tower tower = towerList.get(selectedTower);

            // Draw range visual
            // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            //         RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.GRAY);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setStroke(new BasicStroke(2));

            int range = tower.getRange();
            g2d.drawOval(tower.getPosX() - range, tower.getPosY() - range, range * 2, range * 2);

            // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            //         RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Draw buttons
            for (UpgradeButton ub : upgradeButtonList) {
                if (ub.shouldDraw()) {
                    int ubX = ub.getImageX();
                    int ubY = ub.getImageY();
                    int ubW = ub.getWidth();
                    int ubH = ub.getHeight();

                    // Button will be transparent if tower cannot be built
                    if (ub.getType() != -1
                        && playerOne.getMoney() < ub.getBuildCost()) {
                        g2d.setColor(ub.getColor());
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            0.5f));

                    // Button will be transparent and grayed out if upgrade cannot be bought
                    } else if (playerOne.getMoney() < tower.getUpgradeCost()) {
                        g2d.setColor(Color.GRAY);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            0.5f));

                    } else {
                        g2d.setColor(ub.getColor());
                        
                    }

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

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
        }

        // Draw labels
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString(String.format("Wave %d / 5", waveNumber), 10, 25);
        g2d.drawString(String.format("Player HP: %d", playerOne.getPlayerHp()), 10, 50);
        g2d.drawString(String.format("Gold: %d", playerOne.getMoney()), 665, 25);
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
        
        // System.out.println("Click registered at: (" + lastClickX + ", " + lastClickY + ")");
        
        // Logic for button clicks
        boolean anyButtonClicked = false;
        int i = 0;

        do {
            UpgradeButton ub = upgradeButtonList[i];

            if (ub.isClicked(lastClickX, lastClickY)) {
                // If block runs when button is clicked
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

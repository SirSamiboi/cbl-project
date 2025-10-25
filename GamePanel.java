import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
 * GamePanel is an extension of JPanel in which a game round is fully contained and controlled.
 * This includes processing game logic, handling user input and updating the screen every tick.
 */

class GamePanel extends JPanel implements MouseListener {
    public String fileSeparator = File.separator;

    Random random = new Random();
    Player player = new Player(); // Used to store player statistics

    /*
     * 0 - Game has not started yet
     * 1 - Game is in progress
     * 2 - Game is paused
     * 3 - Player has won
     * 4 - Player has lost
     */
    public int gameState = 0;

    public Button[] menuButtons = {new PlayButton(300, 330), new QuitButton(300, 400)};
    public PauseButton pauseButton = new PauseButton(385, 10);

    private BufferedImage mapImage;

    private int lastClickX = -1;
    private int lastClickY = -1;

    private int selectedTower = -1; // -1 when nothing selected, otherwise index of tower in list
    private int selectedButton = -1;
    private int globalTimer = 0; // Counts the total number of ticks elapsed
    
    public ArrayList<Tower> towerList = new ArrayList<>();
    public ArrayList<Enemy> enemyList = new ArrayList<>();
    public UpgradeButton[] upgradeButtonList = {
        new UpgradeButton(-1, 0, 56),
        new UpgradeButton(0, 0, -48),
        new UpgradeButton(1, 82, 0),
        new UpgradeButton(2, -76, 0)
    };
    public ArrayList<Animation> animationList = new ArrayList<>();

    public int waveNumber = 0; // Initialized at 0, changes to 1 upon game start
    public int waveTotal = 15; // Total number of waves
    public int waveLength;
    // IDs of all enemies that will be spawned each wave
    public byte[][] perWaveEnemyTypes = {
        {}, // Comments state expected money at the start of a wave, for the purpose of testing
        {0, 0, 0}, // 50
        {0, 0, 0, 0, 0}, // 80
        {0, 0, 0, 0, 0, 0, 0, 0}, // 130
        {0, 0, 0, 1, 0, 0, 0}, // 210
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 1}, // 295
        {2, 1, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, // 455
        {2, 2, 2, 3, 0, 0, 0, 0}, // 650
        {0, 0, 0, 1, 3, 1, 0, 0, 0, 1, 3, 1}, // 785
        {2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, // 1045
        {0, 0, 0, 3, 0, 0, 0, 3, 1, 1, 1, 4, 2, 2, 2}, // 1345
        {2, 1, 2, 3, 2, 1, 2, 4, 2, 1, 2, 3, 2, 1, 2}, // 1825
        {3, 3, 5, 1, 1, 1, 1, 1, 4, 3, 1, 5, 1, 5}, // 2345
        {0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // 2990
        {4, 2, 2, 2, 2, 4, 3, 3, 3, 4, 5, 5, 5}, // 3750
        {3, 4, 3, 3, 3, 4, 3, 3, 6,
            3, 3, 5, 5, 5, 5, 5, 3, 3, 5, 5, 5, 5, 5} // 4680
        
        // Enemy money: 0 = 10, 1 = 25, 2 = 15, 3 = 50, 4 = 200, 5 = 40
    };

    // Timestamps of when enemies spawn each wave, relative to the last enemy, in ticks
    // Index 0 is usually 0 because the first enemy spawns as soon as the wave begins
    public int[][] perWaveSpawnIntervals = {
        {},
        {60, 30, 30},
        {0, 30, 30, 30, 30}, // Here a goblin enemy will spawn at 0, 30, 60, 90, 120 ticks
        {0, 15, 15, 15, 60, 15, 15, 15},
        {0, 15, 15, 60, 60, 15, 15},
        {0, 15, 15, 15, 60, 15, 15, 15, 60, 15},
        {0, 60, 30, 30, 60, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
        {0, 15, 15, 60, 30, 15, 15, 15},
        {0, 10, 10, 60, 60, 60, 90, 10, 10, 60, 60, 60},
        {0, 10, 10, 10, 10, 10, 10, 10, 10, 10,
            90, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {0, 15, 15, 15, 90, 15, 15, 15, 90, 30, 30, 120, 180, 30, 30},
        {0, 30, 30, 60, 60, 30, 30, 60, 60, 30, 30, 60, 60, 30, 30},
        {0, 30, 30, 90, 15, 15, 15, 15, 120, 15, 60, 60, 60, 60},
        {0, 15, 15, 15, 7, 8, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 7, 8, 15, 15, 15, 15, 15, 15, 15,
            15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15},
        {0, 60, 15, 15, 15, 120, 60, 30, 30, 120, 60, 30, 30},
        {0, 30, 30, 30, 240, 30, 30, 30, 240,
            300, 15, 15, 30, 30, 30, 30, 300, 15, 15, 30, 30, 30, 30}
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
            this.mapImage = ImageIO.read(new File(String.format(
                "assets%smap1.png", fileSeparator)));
            
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
        if (gameState == 1 && pauseButton.isClicked(lastClickX, lastClickY)) {
            gameState = 2;
            lastClickX = -1;
            lastClickY = -1;
        } else if (gameState == 2 && pauseButton.isClicked(lastClickX, lastClickY)) {
            gameState = 1;
            lastClickX = -1;
            lastClickY = -1;

            for (Button button : menuButtons) {
                button.setVisible(false);
            }
        }

        switch (gameState) {
            case 0: // If the game has not started yet 
                if (menuButtons[0].getVisible()
                    && menuButtons[0].isClicked(lastClickX, lastClickY)) {
                    gameState = 1;
                    menuButtons[0].setVisible(false);
                    menuButtons[1].setVisible(false);
                } else if (menuButtons[1].getVisible()
                    && menuButtons[1].isClicked(lastClickX, lastClickY)) {
                    System.exit(0);
                }

                break;

            case 1: // The game is going on, standard logic
                if (player.getPlayerHp() <= 0) { // if player is dead
                    System.out.println("All lives lost");
                    playDefeatJingle();
                    animationList = new ArrayList<>(); // Remove all animations
                    gameState = 4; // Go to defeat screen
                    break;
                }

                if (waveNumber > waveTotal) { // If outside of the wave range
                    System.out.println("All waves beat");
                    playVictoryJingle();
                    animationList = new ArrayList<>(); // Remove all animations
                    gameState = 3; // Go to victory screen
                    break;
                }

                
                // Starting a new wave if all enemies have been defeated
                if (enemyList.isEmpty() && waveEnemiesSpawned == waveLength) {

                    enemySpawnTimes.clear(); // Resets the spawn timings for the next wave.
                    waveNumber += 1; // Moves the wave counter to the next wave

                    if (waveNumber > waveTotal) { // If outside of the wave range
                        System.out.println("All waves beat");
                        playVictoryJingle();
                        animationList = new ArrayList<>(); // Remove all animations
                        gameState = 3; // Go to the victory screen
                        break;
                    }
                    playWaveStartJingle();

                    waveEmptyTime = globalTimer;
                    // Sets the time at which the next wave will start
                    nextWaveTime = waveEmptyTime + waveDelayTime;
                    
                    if (waveNumber > waveTotal) { // If outside of the wave range
                        System.out.println("All waves beat");
                        playVictoryJingle();
                        animationList = new ArrayList<>(); // Remove all animations
                        gameState = 3; // Go to the victory screen
                        break;
                    }
                    
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
                // Does not support multiple enemies spawning on same tick
                if (enemySpawnTimes.contains(globalTimer)) {
                    // Generate a randomized spawn height
                    int spawnY = 125 + random.nextInt(11) - 5;
                    // Spawns the enemy with the matching ID
                    switch (perWaveEnemyTypes[waveNumber][waveEnemiesSpawned]) {
                        case (byte) 0 -> enemyList.add(new Goblin(0, spawnY));
                        case (byte) 1 -> enemyList.add(new Ogre(0, spawnY));
                        case (byte) 2 -> enemyList.add(new Skeleton(0, spawnY));
                        case (byte) 3 -> enemyList.add(new Orc(0, spawnY));
                        case (byte) 4 -> enemyList.add(new Golem(0, spawnY));
                        case (byte) 5 -> enemyList.add(new Necromancer(0, spawnY));
                        case (byte) 6 -> enemyList.add(new Fallen(0, spawnY));
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
                    enemy.tick(enemyList, player);

                    if (enemy.getHp() <= 0) {
                        player.setMoney(player.getMoney() + enemy.getMoney());
                        enemy.die(enemyList, animationList);
                        i -= 1;
                    }
                }

                // Process ticks for animations
                for (int i = 0; i < animationList.size(); i++) {
                    Animation animation = animationList.get(i);
                    animation.tick();
                    
                    if (animation.getTimer() >= animation.getDuration()) {
                        animationList.remove(animation);
                        i -= 1;
                    }
                }
                // Increment global timer by 1
                globalTimer++;
                break;

            case 2: // Game paused
                menuButtons[0].setVisible(true);
                menuButtons[1].setVisible(true);

                if (menuButtons[0].getVisible()
                    && menuButtons[0].isClicked(lastClickX, lastClickY)) {
                    gameState = 1;
                    menuButtons[0].setVisible(false);
                    menuButtons[1].setVisible(false);
                } else if (menuButtons[1].getVisible()
                    && menuButtons[1].isClicked(lastClickX, lastClickY)) {
                    System.exit(0);
                }
                break;

            case 3: // Victory
                menuButtons[1].setVisible(true);
                if (menuButtons[1].getVisible()
                    && menuButtons[1].isClicked(lastClickX, lastClickY)) {
                    System.exit(0);
                }
                break;

            case 4: // Defeat
                menuButtons[1].setVisible(true);
                if (menuButtons[1].getVisible()
                    && menuButtons[1].isClicked(lastClickX, lastClickY)) {
                    System.exit(0);
                }
                break;
            
            default:
                System.out.println("Invalid game state");
        }

        // Render next frame
        repaint();
    }

    /**
     * Handles all drawing. Called via repaint().
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering hints
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Prioritise rendering speed
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_SPEED);

        // Smoothen the pixel art
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);     

        // Draw map image
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw enemies
        for (Enemy enemy : enemyList) {
            if (enemy.getFacingRight()) {
                g2d.drawImage(enemy.getImage(), enemy.getImageX(), enemy.getImageY(),
                    enemy.getImageWidth(), enemy.getImageHeight(), this);
            } else { // FLip image if enemy is facing left
                g2d.drawImage(enemy.getImage(), enemy.getImageX() + enemy.getImageWidth(),
                    enemy.getImageY(), -enemy.getImageWidth(), enemy.getImageHeight(), this);
            }
        }

    
        // Draw towers
        for (Tower tower : towerList) {
            g2d.drawImage(tower.getImage(), tower.getImageX(), tower.getImageY(),
                tower.getImageWidth(), tower.getImageHeight(), this);
        }


        // Draw animations
        for (int i = 0; i < animationList.size(); i++) {
            int[] val = animationList.get(i).getValues();
            
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
                    g2d.setColor(new Color(val[5], val[6], val[7]));
                    g2d.setStroke(new BasicStroke(val[4]));
                    g2d.drawLine(val[0], val[1], val[2], val[3]);

                    // Explosion circle
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2d.fillOval(val[2] - val[8] / 2, val[3] - val[8] / 2, val[8], val[8]);
                    g2d.fillOval(val[2] - val[8] / 2 + 20, val[3] - val[8] / 2 + 20,
                            val[8] - 40, val[8] - 40);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }

                // Chill tower attack (see ChillAnimation)
                case "2" -> {
                    // Outer beam
                    g2d.setColor(new Color(val[5], val[6], val[7]));
                    g2d.setStroke(new BasicStroke(val[4]));
                    g2d.drawLine(val[0], val[1], val[2], val[3]);

                    // Chill diamond
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

                    // Store original Graphics2D state to prevent other elements from being rotated
                    AffineTransform originalTransform = g2d.getTransform();
                    g2d.translate(val[2], val[3]);
                    g2d.rotate(Math.PI / 4.0);
                    
                    g2d.drawRect(-val[8] / 2, -val[8] / 2, val[8], val[8]);
                    g2d.fillRect(-val[8] / 2 + 8, -val[8] / 2 + 8, val[8] - 16, val[8] - 16);

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                    // Restore original Graphics2D state
                    g2d.setTransform(originalTransform);
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
                        && player.getMoney() < ub.getBuildCost()) {
                        g2d.setColor(ub.getColor());
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                            0.5f));

                    // Button will be transparent and grayed out if upgrade cannot be bought
                    } else if (player.getMoney() < tower.getUpgradeCost()) {
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
                            ubX + 4, ubY + ubH / 2 + 11);
                    } else { // Build cost
                        g2d.drawString("Cost: " + ub.getBuildCost(),
                            ubX + 4, ubY + ubH / 2 + 11);
                    }

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
        }

        g2d.setFont(new Font("Arial", Font.BOLD, 22));

        // Draw labels while round is ongoing
        if (gameState == 1 || gameState == 2) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.format("Wave %d / %d", waveNumber, waveTotal), 10, 25);
            g2d.drawString(String.format("Lives: %d", player.getPlayerHp()), 10, 50);
            g2d.drawString(String.format("Gold: %d", player.getMoney()), 670, 25);
        }

        // Dim background while gameplay is not ongoing
        if (gameState != 1) {
            g2d.setColor(Color.BLACK);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.fillRect(0, 0, 800, 640);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // Draw menu buttons, if visible
        g2d.setStroke(new BasicStroke(2));
        for (Button button : menuButtons) {
            if (button.getVisible()) {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(button.getPosX(), button.getPosY(),
                    button.getWidth(), button.getHeight());
                g2d.setColor(Color.BLACK);
                g2d.drawRect(button.getPosX(), button.getPosY(),
                    button.getWidth(), button.getHeight());
                g2d.drawString(button.getText(), button.getPosX() + 16, button.getPosY() + 32);
            }
        }

        // Draw menu text while gameplay is not ongoing
        switch (gameState) {
            case 0 -> {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 44));
                g2d.drawString("Magic Defender", 235, 220);
            }

            case 2 -> {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 44));
                g2d.drawString("Game Paused", 251, 220);
            }

            case 3 -> {
                g2d.setColor(Color.GREEN);
                g2d.setFont(new Font("Arial", Font.BOLD, 60));
                g2d.drawString("VICTORY!", 250, 270);
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
                g2d.drawString("The castle was defended. Your King is pleased!", 145, 320);
            }
                
            case 4 -> {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 60));
                g2d.drawString("DEFEAT", 278, 270);
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
                g2d.drawString("The castle was invaded by the monsters.", 180, 320);
            }

            default -> { }
        }

        // Draw pause button while gameplay is ongoing
        if (gameState == 1) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(pauseButton.getPosX(), pauseButton.getPosY(),
                pauseButton.getWidth(), pauseButton.getHeight());
            g2d.setColor(Color.BLACK);
            g2d.drawRect(pauseButton.getPosX(), pauseButton.getPosY(),
                pauseButton.getWidth(), pauseButton.getHeight());
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString(pauseButton.getText(), 391, 36);
        }
        
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
     * Allows for interaction with upgrade buttons and towers.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        lastClickX = e.getX();
        lastClickY = e.getY();
        
        // Logic for tower button clicks
        boolean anyButtonClicked = false;
        int i = 0;

        // Prevent tower interaction while not playing
        if (gameState != 1) {
            return;
        }

        do {
            UpgradeButton ub = upgradeButtonList[i];

            if (ub.isClicked(lastClickX, lastClickY)) {
                // If block runs when button is clicked
                if (selectedButton != i) {
                    System.out.println("Button " + i + " has been clicked");
                }

                anyButtonClicked = true;
                ub.click(towerList, player);
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
    public void mouseEntered(MouseEvent e) { }

    /**
     * Called when the mouse cursor exits the window.
     */
    @Override
    public void mouseExited(MouseEvent e) { }

    /**
     * Plays a jingle at the start of each wave.
     */
    public static void playWaveStartJingle() {
        // Get URL for sound file (sound effects found in sounds folder)
        String fileSeparator = File.separator;
        URL soundUrl = GamePanel.class.getResource(String.format("%ssounds%s", fileSeparator, 
            fileSeparator) + "waveStart.wav");

        if (soundUrl == null) {
            System.err.println("Wave start sound file not found");
            return;
        }

        try (InputStream is = soundUrl.openStream();
            var audioStream = AudioSystem.getAudioInputStream(is)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.err.println("Failed to play wave start jingle: " + e.getMessage());
        }
    }

    /**
     * Plays a jingle upon victory.
     */
    public static void playVictoryJingle() {
        // Get URL for sound file (sound effects found in sounds folder)
        String fileSeparator = File.separator;
        URL soundUrl = GamePanel.class.getResource(String.format("%ssounds%s", fileSeparator, 
            fileSeparator) + "victory.wav");

        if (soundUrl == null) {
            System.err.println("Victory sound file not found");
            return;
        }

        try (InputStream is = soundUrl.openStream();
            var audioStream = AudioSystem.getAudioInputStream(is)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.err.println("Failed to play victory jingle: " + e.getMessage());
        }
    }

    /**
     * Plays a jingle upon defeat.
     */
    public static void playDefeatJingle() {
        // Get URL for sound file (sound effects found in sounds folder)
        String fileSeparator = File.separator;
        URL soundUrl = GamePanel.class.getResource(String.format("%ssounds%s", fileSeparator, 
            fileSeparator) + "defeat.wav");

        if (soundUrl == null) {
            System.err.println("Defeat sound file not found");
            return;
        }

        try (InputStream is = soundUrl.openStream();
            var audioStream = AudioSystem.getAudioInputStream(is)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.err.println("Failed to play defeat jingle: " + e.getMessage());
        }
    }
}

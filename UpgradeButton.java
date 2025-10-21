import java.awt.*;
import java.util.*;

class UpgradeButton {
    private int posX;
    private int posY;
    private int towerIndex = -1;
    private Tower tower;
    private final int type; // -1 for upgrades, otherwise tower type
    private final int width;
    private final int height;
    private final int offsetX; // x position relative to selected tower
    private final int offsetY; // y position relative to selected tower
    
    private final int[] widthList = {
        64,
        80
    };

    private final int[] heightList = {
        32,
        32
    };

    private final String[] textList = {
        "Upgrade",
        "Basic Tower"
    };

    private final Color[] colorList = {
        Color.YELLOW,
        Color.LIGHT_GRAY
    };

    // Costs to build each type of tower
    private final int[] buildCostList = {
        25,
        50
    };

    /**
     * Constructor.
     */
    public UpgradeButton(int type, int offsetX, int offsetY) {
        this.type = type;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = widthList[type + 1];
        this.height = heightList[type + 1];
    }
    
    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

    int getTowerIndex() {
        return towerIndex;
    }

    Tower getTower() {
        return tower;
    }

    int getType() {
        return type;
    }

    /**
     * Returns width of the button.
     * 0 is used to hide the button if a tower is deselected.
     */
    int getWidth() {
        if (towerIndex == -1) {
            return 0;
        } else {
            return width;
        }
    }

    /**
     * Returns height of the button.
     * 0 is used to hide the button if a tower is deselected.
     */
    int getHeight() {
        if (towerIndex == -1) {
            return 0;
        } else {
            return height;
        }
    }

    /**
     * Returns the x position of the button's image.
     * The position is clamped to always leave some space between the button and screen borders.
     */
    int getImageX() {
        int imageX = posX - width / 2;
        return Math.min(Math.max(imageX, 10), 790 - width);
    }

    /**
     * Returns the y position of the button's image.
     * The position is clamped to always leave some space between the button and screen borders.
     */
    int getImageY() {
        int imageY = posY - height / 2;
        return Math.min(Math.max(imageY, 10), 630 - height);
    }

    String getText() {
        return textList[type + 1]; // +1 because type -1 text is at index 0
    }

    Color getColor() {
        return colorList[type + 1];
    }

    int getBuildCost() {
        return buildCostList[type];
    }

    /**
     * Links the button to the selected tower.
     */
    void assign(ArrayList<Tower> towerList, int selectedTower) {
        towerIndex = selectedTower;

        if (selectedTower == -1) {
            tower = null;
            posX = 1000;
            posY = 1000;
            return;
        }
        
        tower = towerList.get(selectedTower);

        posX = tower.getPosX() + offsetX;
        posY = tower.getPosY() + offsetY;
    }

    /**
     * Checks if the button has been clicked.
     */
    boolean isClicked(int lastClickX, int lastClickY) {
        return lastClickX >= getImageX() && lastClickX <= getImageX() + width
            && lastClickY >= getImageY() && lastClickY <= getImageY() + height
            && shouldDraw();
    }

    /**
     * Checks if the button should be displayed.
     */
    boolean shouldDraw() {
        if (type == -1) {
            return tower.getMaxLevel() != 0 && tower.getLevel() < tower.getMaxLevel();
        } else {
            return tower.getMaxLevel() == 0;
        }
    }

    /**
     * Runs when the button is clicked.
     */
    void click(ArrayList<Tower> towerList, Player playerOne) {
        int money = playerOne.getMoney();

        // Upgrade tower
        if (type == -1) {
            if (tower.getLevel() < tower.getMaxLevel() && money >= tower.getUpgradeCost()) {
                playerOne.setMoney(money - tower.getUpgradeCost());
                tower.levelUp();
            }
        
        // Build basic tower
        } else if (type == 0) {
            if (tower.getMaxLevel() == 0 && money >= buildCostList[type]) {
                playerOne.setMoney(money - buildCostList[type]);
                tower.placeBasic(towerList);
            }
        }
    }
}
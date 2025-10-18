import java.util.*;

class UpgradeButton {
    private int towerIndex = -1;
    private Tower tower;

    /**
     * Links the button to the selected tower.
     */
    void assign(ArrayList<Tower> towerList, int selectedTower) {
        towerIndex = selectedTower;

        if (selectedTower == -1) {
            return;
        }
        
        tower = towerList.get(selectedTower);
    }
}
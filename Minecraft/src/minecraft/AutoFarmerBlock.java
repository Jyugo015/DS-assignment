/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author USER
 */
import java.util.*;

public class AutoFarmerBlock {
    private Inventory inventory;
    private Queue<String> queue;
    private static final int NUMCROP = 7;
    private static final int NUMINFO = 6;
    private String[][] cropInfo = new String[NUMCROP][NUMINFO];
    private String seed, toolNeeded;
    private int numGrowthStage, numYieldSeedMin, numYieldSeedMax, numYieldCropMin, numYieldCropMax;

    public AutoFarmerBlock() {

    }

    public String[][] setCropInfo() {
        String [][] cropInfo = {
                //cropName, rawPlant, toolNeededForHarvesting, growthStages, seedsYielded, cropsYielded
                {"Wheat", "WheatSeed", "hoe", "8", "1-3", "1"},
                {"Carrot", "Carrot", "hoe", "8", "1-3", "1"},
                {"Potato", "Potato", "hoe", "8", "1-4", "1"},
                {"Beetroot", "hoe", "4", "1-4", "1"},
                {"Melon", "hoe", "3", "1-4", "1"},
                {"Pumpkin", "PumpkinSeed","hoe", "3", "1-4", "1"},
                {"Nether Wart", "hoe", "4", "1-4", "1"},
        };
        return cropInfo;
    }

    public void getCropInfo(String cropName) {
        cropInfo = setCropInfo();
        String[] numSeed, numCrop;
        int row = 0, col = 0;
        while (!cropInfo[row][0].equals(cropName)) {
            row++;
        }
        seed = cropInfo[row][1];
        toolNeeded = cropInfo[row][2];
        numGrowthStage = Integer.parseInt(cropInfo[row][3]);
        numSeed = cropInfo[row][4].split("-");
        numYieldSeedMin = Integer.parseInt(numSeed[0]);
        numYieldSeedMax = Integer.parseInt(numSeed[1]);
        numCrop = cropInfo[row][5].split("-");
        numYieldCropMin = Integer.parseInt(numCrop[0]);
        numYieldCropMax = Integer.parseInt(numCrop[1]);
    }

    public void enTask(String cropName, int quantity) {
        getCropInfo(cropName);
        if (inventory.checkAvailability(seed, quantity) && inventory.checkAvailability(toolNeeded, 1)) {
            queue.add("Planting " + cropName);
            queue.add("Watering " + cropName);
            for (int i = 0; i < numGrowthStage; i++) {
                queue.add("Growth Stage " + (i+1) + " of " + cropName);
                try {
                    Thread.sleep(5000); // Sleep for 5000 milliseconds (5 seconds)
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Handle interruption if necessary
                }
            }
            queue.add("Harvesting " + cropName);
            if (numYieldSeedMin > 0 && numYieldSeedMax > 0) {
                int numSeed = (int)(Math.random()*numYieldSeedMax+numYieldSeedMin);
                inventory.updateInventory(seed, numSeed);
                System.out.println(numSeed + " harvested!");
            } else
                inventory.updateInventory(seed,0);
            if (numYieldCropMin > 0 && numYieldCropMax > 0) {
                inventory.updateInventory(cropName, (int)(Math.random()*numYieldCropMax+numYieldCropMin));
            } else
                inventory.updateInventory(cropName,0);
        }
    }

    public void deTask () {
        queue.remove();
    }

    public void endTask () {
        queue.clear();
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author USER
 */
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;


public class AutoFarmerBlock {
    private Inventory inventory;
    private Queue<String> taskManager;
    private Map<String, CropInfo> cropInfoMap;

    public AutoFarmerBlock() {
        inventory = new Inventory();
        taskManager = new LinkedList<>();
        cropInfoMap = initializeCropInfo();
    }
    
    //Crop Information using Map, HashMap
    //key: cropName
    //value: CropInfo class -> seed, toolNeeded, numGrowthStage, minSeedYield, maxSeedYield, minCropYield, maxCropYield
    private Map<String, CropInfo> initializeCropInfo() {
        Map<String, CropInfo> cropInfoMap = new HashMap<>();
        cropInfoMap.put("Wheat", new CropInfo("Wheat Seed", "none", 8, 1, 4, 1,1));
        cropInfoMap.put("Carrot", new CropInfo("Carrot", "none", 8, 0, 0, 1, 4));
        cropInfoMap.put("Potato", new CropInfo("Potato", "none", 8, 0, 0, 1, 4));
        cropInfoMap.put("Beetroot", new CropInfo("Beetroot Seed", "none", 4, 1, 4, 1,1));
        cropInfoMap.put("Melon", new CropInfo("Melon Seed", "axe", 8, 0, 0, 3, 7));
        cropInfoMap.put("Pumpkin", new CropInfo("Pumpkin Seed", "axe", 8, 0, 0, 1, 1));
        cropInfoMap.put("Sweet Berries", new CropInfo("Sweet Berry Bushes", "none", 4, 0, 0, 2, 3));
        return cropInfoMap;
    }

   
    //when addCrop button is clicked, add task into the queue.
    //if resources needed is insufficient, print out suitable message
    public void addCrop(String cropName) {
    CropInfo cropInfo = cropInfoMap.get(cropName);
    if (cropInfo != null && inventory.checkAvailability(cropInfo.getSeed())) {
        String toolNeeded = cropInfo.getToolNeeded();
        if (toolNeeded.equals("none") || inventory.checkAvailability(toolNeeded)) {
            inventory.useInventory(cropInfo.getSeed());
            taskManager.add("Planting " + cropName);
            taskManager.add("Watering " + cropName);
            for (int i = 1; i <= cropInfo.getNumGrowthStages(); i++) { //add each growth stage into the queue
                taskManager.add("Growth Stage " + i + " (" + cropName + ") ");
            }
            taskManager.add("Harvesting " + cropName);
//            runTask(cropName);
        } else { //tool not enough
            System.out.println("Require Tool: " + toolNeeded); 
        }
    } else { //insufficient seed or cannot find cropInfo
        System.out.println("Insufficient resources to plant"); 
    }
}

    //display the task list
    public void displayList() {
        System.out.println("Task List:");
        int index = 1;
        for (String task : taskManager) {
            System.out.println(index + ". " + task);
            index++;
        }
    }
    
    //run through all the task inside the queue
    //after the addCrop button is clicked and all tasks are added into the queue, starts running the task one by one
    //complete one task, dequeue -> poll()
    public void runTask(String cropName) {
        CropInfo cropInfo = cropInfoMap.get(cropName);
        
        if (cropInfo == null) {
            System.out.println("Crop not found in database.");
            return;
        }
        
        while(!taskManager.isEmpty()) { //keeps execute the tasks until all tasks completed
            String[] tokens = taskManager.peek().split(" ");
            String taskType = tokens[0];
            switch(taskType) {
                case "Planting" -> {
                    System.out.println("Planted " + cropName);
                    taskManager.poll();
                }
                case "Watering" -> {
                    System.out.println("Finished Watering " + cropName);
                    taskManager.poll();
                }
                case "Growth" -> {
                    System.out.println("Growth Stage " + tokens[2] + " for " + cropName);
                    try {
                        Thread.sleep((int)(Math.random()*6001+2001)); //growth stage duration 2-6 seconds each (random)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    taskManager.poll();
                }
                case "Harvesting" -> {
                    if (cropInfo.getMaxSeedYield() != 0) {
                        int numSeed = (int)(Math.random() * (cropInfo.getMaxSeedYield() - cropInfo.getMinSeedYield() + 1)) + cropInfo.getMinSeedYield();
                        System.out.println(numSeed + " " + cropInfo.getSeed() + " Harvested!");
                        inventory.addInventory(cropInfo.getSeed(), numSeed);
                    }

                    int numCrop = (int)(Math.random() * (cropInfo.getMaxCropYield() - cropInfo.getMinCropYield() + 1)) + cropInfo.getMinCropYield();
                    String yielded = (!cropName.equals("Melon") ? cropName : "Melon Slices");
                    System.out.println(numCrop + " " + yielded + " Harvested!");
                    inventory.addInventory(yielded, numCrop);

                    
                    taskManager.poll();
                }
            }
        }
    }

    
    private static class CropInfo {
        private String seed;
        private String toolNeeded;
        private int numGrowthStages;
        private int minSeedYield;
        private int maxSeedYield;
        private int minCropYield;
        private int maxCropYield;

        public CropInfo(String seed, String toolNeeded, int numGrowthStages, int minSeedYield,
                        int maxSeedYield, int minCropYield, int maxCropYield) {
            this.seed = seed;
            this.toolNeeded = toolNeeded;
            this.numGrowthStages = numGrowthStages;
            this.minSeedYield = minSeedYield;
            this.maxSeedYield = maxSeedYield;
            this.minCropYield = minCropYield;
            this.maxCropYield = maxCropYield;
        }

        public String getSeed() {
            return seed;
        }

        public String getToolNeeded() {
            return toolNeeded;
        }

        public int getNumGrowthStages() {
            return numGrowthStages;
        }

        public int getMinSeedYield() {
            return minSeedYield;
        }

        public int getMaxSeedYield() {
            return maxSeedYield;
        }

        public int getMinCropYield() {
            return minCropYield;
        }

        public int getMaxCropYield() {
            return maxCropYield;
        }
    }
}



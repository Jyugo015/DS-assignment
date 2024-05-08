/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package second;

/**
 *
 * @author elysi
 */


import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

// Define the Potion class
class Potion {
    String name;
    String effect;
    Potion nextPotion;

    // Constructor to initialize potion name and effect
    public Potion(String name, String effect) {
        this.name = name;
        this.effect = effect;
    }
}

// Define the PotionSatchel class
public class PotionSatchel {
    private Potion head;
    private int size;
    private Potion currentPotion;
    private Timer timer;

    // Constructor to initialize the satchel
    public PotionSatchel() {
        this.head = null;
        this.size = 0;
        this.currentPotion = null;
        this.timer = new Timer();
    }

    // Add potion to the satchel
    public void addPotion(Potion potion) {
        if (head == null) {
            head = potion;
            currentPotion = head;
        } else {
            Potion current = head;
            while (current.nextPotion != null) {
                current = current.nextPotion;
            }
            current.nextPotion = potion;
        }
        size++;
    }

    //SEQUENTIAL POTION ACCESS
    // Set the sequence of the potions based on user input
    public void setPotionSequence(Scanner scanner) {
        System.out.print("Enter the number of potions:");
        int numPotions = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the names of potions in the desired sequence: \n");
        for (int i = 0; i < numPotions; i++) {
            System.out.print("Potion " + (i + 1) + ":");
            String name = scanner.nextLine();
            // Simulating database lookup to get effect by name

            String effect = getEffectByName(name);
            if (effect != null) {
                addPotion(new Potion(name, effect));
            } else {
                System.out.println("Potion '" + name + "' not found.");
            }
        }
        currentPotion = head;
    }

    // Simulate database lookup to get effect by name
    private String getEffectByName(String name) {
        // Simulate database lookup
        return null;
    }

    // Use potion from the satchel
    public void usePotion() {
        if (currentPotion == null) {
            System.out.println("No potions available in the satchel.");
        } else {
            Potion usedPotion = currentPotion;
            currentPotion = currentPotion.nextPotion;
            size--;
            System.out.println("Using " + usedPotion.name + " potion. Effect: " + usedPotion.effect);
        }
    }

    //AUTOMATIC POTION SELECTION
    // Automatically use potions
    public void automaticUsePotion() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                usePotion();
            }
        }, 0, 15 * 1000); // Run every 15 seconds
    }

    // Stop automatic use of potions
    public void stopAutomaticUse() {
        timer.cancel(); //call this method to stop automatic u
    }
    public void setPotionSequence(String[] potionNames) {
        for (String name : potionNames) {
            String effect = getEffectByName(name);
            if (effect != null) {
                addPotion(new Potion(name, effect));
            } else {
                System.out.println("Potion '" + name + "' not found.");
            }
        }
        currentPotion = head;
    }

    // Set sequence of potions for specific adventures
    public void setPotionSequenceForAdventure(String adventure) {
        switch (adventure) {
            case "PvP":
                setPotionSequence(new String[]{"Strength Potion", "Mighty Strength Potion", "Speed Potion", "Health Potion - Instant Recover",
                                                "Invisibility Potion", "Resistance Potion", "Potion of Turtle Master"});
                break;
            case "BossFight":
                setPotionSequence(new String[]{"Strength Potion", "Resistance Potion", "Health Potion - Restore Over Time", "Health Potion - Instant Recover",
                                                 "Potion of Turtle Master", "Speed Potion", "Slow Falling Potion"});
                break;
            case "Hazardous Exploration Missions":
                setPotionSequence(new String[]{"Leaping Potion", "Water Breathing Potion", "Fire Resistance Potion", "Potion of Luck", "Night Vision Potion",
                                                "Speed potion", "Health Potion - Restore Over Time"});
                break;
            default:
                System.out.println("Adventure '" + adventure + "' not recognized.");
        }
    }


    //method to clear all potion in the satchel
    public void clearSatchel() {
        head = null; //set head to null to clear all potion
        size = 0; //reset the size to 0
    }

    //method to remove the first potion in satchel
    public void removePotion() {
        if (head == null) { //if there is no potion
            System.out.println("No potions available in the satchel.");
        } else {
            //else, remove the first potion and update the new head
            head = head.nextPotion;
            size--; //size of satchel decrease after remove potion
        }
    }

    //method to display all the potion in the satchel
    public void displayPotions() {
        if (head == null) { //if there is no potion in satchel
            System.out.println("No potions available in the satchel.");
        } else {
            //else, iterate through all potions available in the satchel and then print their names, function
            System.out.println("Potions in the satchel:");
            Potion current = head;
            while (current != null) {
                System.out.println(current.name + ": " + current.effect);
                current = current.nextPotion;
            }
        }

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PotionSatchel satchel = new PotionSatchel();

        satchel.setPotionSequence(scanner);
        satchel.automaticUsePotion();
        satchel.setPotionSequenceForAdventure("PvP");
        satchel.automaticUsePotion();
        satchel.setPotionSequenceForAdventure("Boss Fight");
        satchel.automaticUsePotion();
        satchel.setPotionSequenceForAdventure("Hazardous Exploration Missions");
        satchel.automaticUsePotion();


        //display potion (need or not?)
        System.out.println("Displaying potions in the satchel...");
        satchel.displayPotions();

        //use potion
        System.out.println("Using potions from the satchel...");
        satchel.usePotion();
        satchel.usePotion();

        //remove potion
        System.out.println("Removing a potion from the satchel...");
        satchel.removePotion();
        satchel.displayPotions();

        //clear all potion in satchel
        System.out.println("Clearing the satchel...");
        satchel.clearSatchel();
        satchel.displayPotions();

    }
}

/*
POTIONS

"Speed Potion", "Increases movement speed"
"Strength Tonic", "Boosts attack power"
"Mighty Strength Potion", "Stronger attack power"
"Invisible Draught", "Get invisible"
"Night Vision Elixir", "See clearly at night"
"Leaping Potion", "Increases jump height"
"Fire Resistance Potion", "Grants immunity to fire damage"
"Water Breathing Potion", "Allows breathing underwater"
"Potion of Luck", "Increases luck for finding rare items"
"Resistance Potion", "Provides temporary damage reduction"
"Health Potion - Restore Over Time", "Slowly restores health over time"
Health Potion - Instant Restore", "Instantly restores health"
"Potion of Turtle Master", "Provides temporary damage reduction"
"Slow Falling Potion", "Reduces fall speed and prevents fall damage"

 */


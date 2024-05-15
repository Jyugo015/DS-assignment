/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DS_Minecraft;

/**
 
 * @author elysi
 */
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

public class CreatureEncyclopedia {
    public Hashtable<String, Creature> encyclopedia; 

    // Method to search for creatures based on a search term
    public void searchCreatures(String searchTerm) {
        boolean found = false;
        for (String creatureName : encyclopedia.keySet()) {
            Creature creatureInfo = getCreatureInfo(creatureName);
            if (creatureName.toLowerCase().contains(searchTerm) ||
                creatureInfo.getType().toLowerCase().contains(searchTerm) ||
                creatureInfo.getSpecies().toLowerCase().contains(searchTerm) ||
                creatureInfo.getBehavior().toLowerCase().contains(searchTerm) ||
                creatureInfo.getHabitat().toLowerCase().contains(searchTerm) ||
                creatureInfo.getDrops().toLowerCase().contains(searchTerm) ||
                creatureInfo.getWeakness().toLowerCase().contains(searchTerm)) {

                System.out.println(creatureName + ":");
                System.out.println("Type: " + creatureInfo.getType());
                System.out.println("Species: " + creatureInfo.getSpecies());
                System.out.println("Behavior: " + creatureInfo.getBehavior());
                System.out.println("Habitat: " + creatureInfo.getHabitat());
                System.out.println("Drops: " + creatureInfo.getDrops());
                System.out.println("Weakness: " + creatureInfo.getWeakness());
                List<String> contributions = creatureInfo.getCommunityContributions();
                if (!contributions.isEmpty()) {
                    System.out.println("Community Contributions: ");
                    for (String contribution : contributions) {
                        System.out.println("- " + contribution);
                    }
                }
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No results found for '" + searchTerm + "'.");
        }
    }
    
     // Method to filter creatures by type
    public List<Creature> filterByType(String type) {
        List<Creature> filteredCreatures = new ArrayList<>();
        for (Creature creature : encyclopedia.values()) {
            if (creature.getType().equalsIgnoreCase(type)) {
                filteredCreatures.add(creature);
            }
        }
        return filteredCreatures;
    }

    // Method to sort creatures alphabetically by name (A-Z)
    public List<Creature> sortCreaturesAZ() {
        List<Creature> sortedCreatures = new ArrayList<>(encyclopedia.values());
        Collections.sort(sortedCreatures, Comparator.comparing(Creature::getSpecies));
        return sortedCreatures;
    }

    // Method to sort creatures alphabetically by name (Z-A)
    public List<Creature> sortCreaturesZA() {
        List<Creature> sortedCreatures = sortCreaturesAZ();
        Collections.reverse(sortedCreatures);
        return sortedCreatures;
    }
    
    public static boolean askToSearchAgain(Scanner scanner) {
        System.out.println("\nDo you want to search again? (yes/no)");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("yes");
}


    public CreatureEncyclopedia() {
        encyclopedia = new Hashtable<>();

        //undead mobs
        encyclopedia.put("Skeletons", new Creature("Undead mobs", "Skeletons", "Hostile, armed with bows, shoot arrows at players and other mobs.", "Spawn at night or in dark areas.", "Arrows, bones, and rarely, bows or enchanted bows.", "Vulnerable to close-range attacks due to lack of melee capabilities. They are also susceptible to sunlight, which causes them to burn during the day.", "Undead Mobs"));
        encyclopedia.put("Drowned", new Creature("Undead mobs", "Drowned", "Hostile underwater zombies, wield tridents.", "Spawn underwater in oceans, rivers, and underwater ruins.", "Rotten flesh, occasionally tridents, and fishing loot.", "Vulnerable to attacks from weapons, particularly those with sweeping or knockback effects. They are also slow on land compared to water.", "Undead Mobs"));
        encyclopedia.put("Husks", new Creature("Undead mobs", "Husks", "Hostile zombies found in desert biomes, immune to sunlight.", "Spawn in desert biomes, desert temples, and desert villages.", "Rotten flesh, occasionally iron ingots, carrots, or potatoes.", "Vulnerable to water and fire damage. Their melee attacks do not inflict any additional effects, making them relatively straightforward to handle.", "Undead Mobs"));
        encyclopedia.put("Skeleton horses", new Creature("Undead mobs", "Skeleton horses", "Passive mobs that spawn from lightning strikes, ridden by skeleton horsemen.", "Rarely spawn in the Overworld during thunderstorms.", "None, but can be tamed and ridden by players.", "Vulnerable to attacks, but they do not actively attack players unless ridden by a skeleton horseman.", "Undead Mobs"));
        encyclopedia.put("Withers", new Creature("Undead mobs", "Withers", "Powerful boss mobs, shoot explosive skulls, regenerate health over time.", "Created by players using soul sand and wither skeleton skulls.", "Nether star, used to craft beacons.", "Vulnerable to attacks from players using ranged or melee weapons. They can also be slowed down by the effects of potions such as slowness.", "Undead Mobs"));
        encyclopedia.put("Wither Skeletons", new Creature("Undead mobs", "Wither Skeletons", "Hostile skeletons found in Nether fortresses.", "Spawn in Nether fortresses, particularly in fortress corridors.", "Bones, coal, and rarely, wither skeleton skulls.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Undead Mobs"));
        encyclopedia.put("Zombie villagers", new Creature("Undead mobs", "Zombie villagers", "Hostile variants of villagers.", "Spawn when a villager is killed by a zombie.", "Rotten flesh, occasionally carrots, potatoes, or iron ingots.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Can be cured by using a potion of weakness and a golden apple.", "Undead Mobs"));
        encyclopedia.put("Phantom", new Creature("Flying mobs", "Phantom", "Hostile flying mobs that spawn if players haven't slept for several in-game days.", "Spawn in the Overworld, particularly in the sky.", "Phantom membrane, used to repair elytra.", "Vulnerable to ranged attacks, particularly arrows or projectiles. Takes increased damage from sweeping or knockback attacks.", "Undead Mobs"));
        encyclopedia.put("Strays", new Creature("Undead mobs", "Strays", "Hostile skeletons found in snowy biomes, shoot arrows of slowness.", "Spawn in snowy biomes, particularly during snowstorms.", "Bones, arrows, and occasionally, tipped arrows of slowness.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to fire and sunlight damage.", "Undead Mobs"));
        encyclopedia.put("Zombie Piglins", new Creature("Neutral mobs", "Zombie Piglins", "Neutral mobs found in the Nether, become aggressive if attacked.", "Spawn in the Nether, particularly in Nether wastes and Crimson forests.", "Gold nuggets, rotten flesh, and occasionally, gold ingots or enchanted gold items.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Can be distracted by gold items.", "Undead Mobs"));
        encyclopedia.put("Zoglins", new Creature("Undead mobs", "Zoglins", "Hostile mobs resulting from zombifying hoglins, aggressive towards players and other mobs.", "Spawn when a hoglin enters the Overworld or when a hoglin is zombified.", "Rotten flesh, occasionally leather.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to fire and sunlight damage.", "Undead Mobs"));

        //passive mobs: do not attack player
        encyclopedia.put("Chickens", new Creature("Birds", "Chickens", "Walk around and lay eggs.", "Found in grassy areas, particularly in villages and farms.", "Raw chicken, feathers, and occasionally eggs.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs"));
        encyclopedia.put("Cows", new Creature("Mammals", "Cows", "Graze and can be milked.", "Found in grassy areas, particularly in plains and villages.", "Raw beef, leather, and occasionally milk.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs"));
        encyclopedia.put("Horses", new Creature("Mammals", "Horses", "Can be tamed and ridden.", "Found in plains and savannas, often in herds.", "None.", "Vulnerable to attacks, particularly from hostile mobs or players. May buck off inexperienced riders.", "Passive Mobs"));
        encyclopedia.put("Mooshrooms", new Creature("Mammals", "Mooshrooms", "Similar to cows but with mushroom growth.", "Found in mushroom fields and mushroom biomes.", "Raw beef, leather, and occasionally mushrooms.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs"));
        encyclopedia.put("Rabbits", new Creature("Mammals", "Rabbits", "Hop around and can be bred with carrots.", "Found in grassy areas, particularly in forests and plains.", "Rabbit hide, raw rabbit, and occasionally rabbit's foot.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs"));
        encyclopedia.put("Sheep", new Creature("Mammals", "Sheep", "Graze and can be sheared for wool.", "Found in grassy areas, particularly in plains and mountains.", "Wool (when sheared), raw mutton, and occasionally lamb chops.", "Vulnerable to attacks, particularly from hostile mobs or players. Low health.", "Passive Mobs"));

        //neutral mobs: attack players if provoked or under certain conditions
        encyclopedia.put("Wolves", new Creature("Neutral Mobs", "Wolves", "Neutral, become hostile when attacked or when a nearby wolf is attacked.", "Found in forests and taiga biomes, often in packs.", "Bones and occasionally wolf pelts.", "Vulnerable to attacks, particularly from ranged weapons. Can be tamed with bones.", "Neutral Mobs"));
        encyclopedia.put("Bats", new Creature("Neutral Mobs", "Bats", "Passive mobs that fly around in dark areas.", "Spawn in dark areas, particularly in caves and underground structures.", "None.", "Vulnerable to attacks, but they rarely pose a threat to players due to their passive nature.", "Neutral Mobs"));

        //aquatic mobs
        encyclopedia.put("Axolotls", new Creature("Aquatic Mobs", "Axolotls", "Passive aquatic mobs found in underground water sources, can regenerate health.", "Spawn in lush caves in underground water sources.", "None.", "Vulnerable to attacks, particularly from hostile mobs. Cannot survive out of water for extended periods.", "Aquatic Mobs"));
        encyclopedia.put("Dolphins", new Creature("Aquatic Mobs", "Dolphins", "Passive aquatic mobs, swim in groups, can lead players to shipwrecks and ruins.", "Spawn in ocean biomes, particularly in groups near shorelines.", "Raw cod or raw salmon when killed.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs"));
        encyclopedia.put("Squids", new Creature("Aquatic Mobs", "Squids", "Passive aquatic mobs, swim in groups, ink clouds when attacked.", "Spawn in ocean biomes, particularly in deep ocean biomes.", "Ink sacs used for crafting dyes.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs"));
        encyclopedia.put("Glow squid", new Creature("Aquatic Mobs", "Glow squid", "Passive aquatic mobs that emit light underwater.", "Spawn in ocean biomes, particularly in deep ocean biomes.", "Glow ink sacs, which emit light when used to craft signs and item frames.", "Vulnerable to attacks, particularly from hostile mobs or players. Cannot survive out of water for extended periods.", "Aquatic Mobs"));
        encyclopedia.put("Guardians", new Creature("Aquatic Mobs", "Guardians", "Hostile aquatic mobs found in ocean monuments, shoot laser beams.", "Spawn in ocean monuments, particularly in water chambers.", "Prismarine shards, prismarine crystals, fish, and rarely, sponge blocks.", "Vulnerable to attacks, particularly from ranged weapons. Can be temporarily distracted by blocks or entities placed between them and their target.", "Aquatic Mobs"));
        encyclopedia.put("Elder guardians", new Creature("Aquatic Mobs", "Elder guardians", "Hostile aquatic mobs, more powerful variants of guardians.", "Spawn in ocean monuments, particularly near treasure rooms.", "Same as regular guardians.", "Vulnerable to attacks, particularly from ranged weapons. Can be temporarily distracted by blocks or entities placed between them and their target.", "Aquatic Mobs"));
        encyclopedia.put("Turtles", new Creature("Aquatic Mobs", "Turtles", "Passive aquatic mobs, lay eggs on beaches, can be bred with seagrass.", "Spawn on beaches, particularly in warm ocean biomes.", "Seagrass, scute (when baby turtles grow into adults).", "Vulnerable to attacks, particularly from hostile mobs or players. Slow movement on land makes them susceptible to predators.", "Aquatic Mobs"));
        encyclopedia.put("Cod", new Creature("Aquatic Mobs", "Cod", "Passive aquatic mobs, swim in schools, different varieties found in different biomes.", "Spawn in ocean biomes, particularly in specific biome types.", "Raw fish of their respective types when killed.", "Vulnerable to attacks, particularly from predators or hostile mobs. Limited mobility out of water makes them easy targets for land-based predators.", "Aquatic Mobs"));

        //arthropods
        encyclopedia.put("Bees", new Creature("Neutral Mobs", "Bees", "Passive mobs that pollinate flowers and attack when provoked.", "Found in flower-filled biomes, particularly near beehives.", "Honeycomb and honey bottles.", "Vulnerable to attacks when provoked, but they retaliate in groups. Smoke from a campfire or a beehive calms them, making them less aggressive.", "Arthropods"));
        encyclopedia.put("Cave spiders", new Creature("Hostile Mobs", "Cave spiders", "Hostile spiders found in cave systems, poison players with their attacks.", "Spawn in abandoned mineshafts and cave systems.", "Spider eyes and occasionally, spider webs.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods"));
        encyclopedia.put("Endermites", new Creature("Hostile Mobs", "Endermites", "Hostile mobs that spawn when an ender pearl is used.", "Spawn in the End, particularly near teleporting players or ender pearls.", "None.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Limited mobility makes them easy to defeat.", "Arthropods"));
        encyclopedia.put("Silverfish", new Creature("Hostile Mobs", "Silverfish", "Hostile mobs found in strongholds, hide in blocks and swarm when attacked.", "Spawn in strongholds, particularly in stone bricks.", "None.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods"));
        encyclopedia.put("Spiders", new Creature("Hostile Mobs", "Spiders", "Hostile mobs found in various biomes, capable of climbing walls.", "Spawn in dark areas, particularly in forests and caves.", "String and spider eyes.", "Vulnerable to attacks, particularly from weapons with sweeping or knockback effects. Susceptible to sunlight and fire damage.", "Arthropods"));

        //illagers -evil counterparts to the peaceful villagers found in villages
        encyclopedia.put("Pillagers", new Creature("Hostile Mobs", "Pillagers", "Hostile mobs that attack players and villagers, often found in patrols or pillager outposts.", "Spawn in patrols and pillager outposts in various biomes.", "Crossbows, arrows, emeralds, and occasionally, ominous banners.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers"));
        encyclopedia.put("Illusioners", new Creature("Hostile Mobs", "Illusioners", "Hostile illusion-casting mobs, rare and difficult to find.", "Rarely spawn in woodland mansions or through commands.", "None.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by focusing on the real illusioner rather than its illusions.", "Illagers"));
        encyclopedia.put("Ravagers", new Creature("Hostile Mobs", "Ravagers", "Hostile mobs that charge and attack players and villagers, often accompanied by pillagers.", "Found in patrols, pillager outposts, and raids.", "Saddle, and occasionally, emeralds or crossbows.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers"));
        encyclopedia.put("Evokers", new Creature("Hostile Mobs", "Evokers", "Hostile spell-casting mobs found in woodland mansions, summon vexes and attack players.", "Spawn in woodland mansions, particularly in evoker rooms.", "Totem of undying, emeralds, and occasionally, spell books.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by quickly defeating them to prevent them from summoning vexes.", "Illagers"));
        encyclopedia.put("Vindicators", new Creature("Hostile Mobs", "Vindicators", "Hostile mobs that wield axes and attack players and villagers.", "Spawn in raids, particularly during waves of illagers.", "Emeralds and occasionally, axes or enchanted books.", "Vulnerable to attacks, particularly from ranged weapons. Can be distracted by other mobs or players.", "Illagers"));
        encyclopedia.put("Villagers", new Creature("Passive Mobs", "Villagers", "Passive mobs found in villages, trade with players and perform various professions.", "Spawn in villages, particularly in houses and buildings.", "None.", "Vulnerable to attacks, particularly from hostile mobs. They have limited ability to defend themselves.", "Illagers"));
        encyclopedia.put("Wandering Traders", new Creature("Passive Mobs", "Wandering Traders", "Passive mobs that spawn randomly in the world, trade various items with players.", "Roam around the world, often found in open areas or near player-built structures.", "None.", "Vulnerable to attacks, particularly from hostile mobs. They have limited ability to defend themselves.", "Illagers"));
        encyclopedia.put("Iron Golems", new Creature("Neutral Mobs", "Iron Golems", "Neutral mobs that protect villagers and attack hostile mobs.", "Spawn in villages, particularly when a sufficient number of villagers are present.", "Iron ingots and poppies.", "Vulnerable to attacks, particularly from mobs with high damage output. Can be lured away from villages to weaken their defense.", "Illagers"));
        encyclopedia.put("Witches", new Creature("Hostile Mobs", "Witches", "Hostile spell-casting mobs that attack players with harmful potions.", "Spawn in witch huts and during raids.", "Redstone, glowstone dust, sticks, sugar, spider eyes, potions, and occasionally, glass bottles.", "Vulnerable to attacks, particularly from ranged weapons. Can be countered by avoiding their potion attacks and using cover to approach them safely.", "Illagers"));
        encyclopedia.put("Vexes", new Creature("Hostile Mobs", "Vexes", "Hostile flying mobs summoned by evokers during raids, attack players.", "Spawn during raids summoned by evokers.", "None.", "Vulnerable to attacks, particularly from ranged weapons. Can be difficult to hit due to their flying behavior.", "Illagers"));
    }

    public Creature getCreatureInfo(String creatureName) {
        return encyclopedia.getOrDefault(creatureName, new Creature("", "", "", "", "", "", ""));
    }

    public void addNoteToCreature(String creatureName, String note) {
        Creature creature = encyclopedia.get(creatureName);
        if (creature != null) {
            creature.addCommunityContributions(note);
            System.out.println("Note added successfully for " + creatureName);
        } else {
            System.out.println("Creature " + creatureName + " not found in the encyclopedia.");
        }

    }
    
    public static class Creature {
        private String type;
        private String species;
        private String behavior;
        private String habitat;
        private String drops;
        private String weakness;
        private String category;
        private List<String> communityContributions;

        public Creature(String type, String species, String behavior, String habitat, String drops, String weakness, String category) {
            this.type = type;
            this.species = species;
            this.behavior = behavior;
            this.habitat = habitat;
            this.drops = drops;
            this.weakness = weakness;
            this.category = category;
            this.communityContributions = new ArrayList<>();

        }

        // Getter methods
        public String getType() {
            return type;
        }

        public String getSpecies() {
            return species;
        }

        public String getBehavior() {
            return behavior;
        }

        public String getHabitat() {
            return habitat;
        }

        public String getDrops() {
            return drops;
        }

        public String getWeakness() {
            return weakness;
        }

        public String getCategory() {
            return category;
        }

        public List<String> getCommunityContributions() {
            return communityContributions;
        }

        public void addCommunityContributions(String contribution) {
            communityContributions.add(contribution);
        }
    }

    
    public static void main(String[] args) {
        CreatureEncyclopedia encyclopedia = new CreatureEncyclopedia();
        Scanner scanner = new Scanner(System.in);
            

        // Display information for all creatures
        System.out.println("Creature Encyclopedia:");
        String[] categories = {"Undead Mobs", "Passive Mobs", "Neutral Mobs", "Arthropods", "Illagers"};

        for (String category : categories) {
            System.out.println("\n" + category + ":");
            for (String creatureName : encyclopedia.encyclopedia.keySet()) {
                Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);

                if (creatureInfo.getCategory().equals(category)) {
                    System.out.println(creatureName + ":");
                    System.out.println("Type: " + creatureInfo.getType());
                    System.out.println("Species: " + creatureInfo.getSpecies());
                    System.out.println("Behavior: " + creatureInfo.getBehavior());
                    System.out.println("Habitat: " + creatureInfo.getHabitat());
                    System.out.println("Drops: " + creatureInfo.getDrops());
                    System.out.println("Weakness: " + creatureInfo.getWeakness());
                    List<String> contributions = creatureInfo.getCommunityContributions();
                    if (!contributions.isEmpty()) {
                        System.out.println("Community Contributions: ");
                        for (String contribution : contributions) {
                            System.out.println("- " + contribution);
                        }
                    }
                    System.out.println();
                }
            }
        }
        
        // Ask the user if they want to add notes for any creatures
        System.out.println("Do you want to add a note for any creatures? (yes/no)");
        String addNoteChoice = scanner.nextLine().toLowerCase();

        if (addNoteChoice.equals("yes")) {
        // Allow the user to add notes for multiple creatures
        while (true) {
            System.out.println("\nEnter the name of the creature you want to add a note to (or 'exit' to finish):");
            String creatureName = scanner.nextLine();
        if (creatureName.equalsIgnoreCase("exit")) {
            break;
        }

        // Check if the creature exists in the encyclopedia
        if (!encyclopedia.encyclopedia.containsKey(creatureName)) {
            System.out.println("Creature " + creatureName + " not found in the encyclopedia.");
            continue;
        }

        // Prompt the user to add notes for the creature
        System.out.println("Enter your note for " + creatureName + ":");
        while (true) {
            String note = scanner.nextLine();
            if (note.equalsIgnoreCase("exit")) {
                break;
            }
            encyclopedia.addNoteToCreature(creatureName, note);
        }
    }
        } else {
            System.out.println("No notes will be added.");
}


        // Print the information for all creatures
        System.out.println("\nCreature Encyclopedia After Adding Notes:");
        for (String category : categories) {
            System.out.println("\n" + category + ":");
            for (String creatureName : encyclopedia.encyclopedia.keySet()) {
                Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);

                if (creatureInfo.getCategory().equals(category)) {
                    System.out.println(creatureName + ":");
                    System.out.println("Type: " + creatureInfo.getType());
                    System.out.println("Species: " + creatureInfo.getSpecies());
                    System.out.println("Behavior: " + creatureInfo.getBehavior());
                    System.out.println("Habitat: " + creatureInfo.getHabitat());
                    System.out.println("Drops: " + creatureInfo.getDrops());
                    System.out.println("Weakness: " + creatureInfo.getWeakness());
                    List<String> contributions = creatureInfo.getCommunityContributions();
                    if (!contributions.isEmpty()) {
                        System.out.println("Community Contributions: ");
                        for (String contribution : contributions) {
                            System.out.println("- " + contribution);
                        }
                    }
                    System.out.println();
                }
            }
        }
             // Search functionality
            // Search functionality
        System.out.println("\n=== Search Tool ===");
    do {
        System.out.println("Enter your search term:");
        String searchTerm = scanner.nextLine().toLowerCase();

        System.out.println("\nSearch Results:");
        encyclopedia.searchCreatures(searchTerm);
    } while (askToSearchAgain(scanner));

        // Filter by type
        System.out.println("\n=== Filter by Type ===");
        System.out.println("Enter the type to filter (e.g., 'Mammals', 'Aquatic Mobs'):");
        String typeFilter = scanner.nextLine();
        List<Creature> filteredByType = encyclopedia.filterByType(typeFilter);
        printCreatures(filteredByType);

        // Sort alphabetically (A-Z)
        System.out.println("\n=== Sorted Alphabetically (A-Z) ===");
        List<Creature> sortedAZ = encyclopedia.sortCreaturesAZ();
        printCreatures(sortedAZ);

        // Sort alphabetically (Z-A)
        System.out.println("\n=== Sorted Alphabetically (Z-A) ===");
        List<Creature> sortedZA = encyclopedia.sortCreaturesZA();
        printCreatures(sortedZA);

        scanner.close();
    }

    // Method to print creatures
    private static void printCreatures(List<Creature> creatures) {
        if (creatures.isEmpty()) {
            System.out.println("No creatures found.");
        } else {
            for (Creature creature : creatures) {
                System.out.println(creature.getSpecies() + ":");
                System.out.println("Type: " + creature.getType());
                System.out.println("Behavior: " + creature.getBehavior());
                System.out.println("Habitat: " + creature.getHabitat());
                System.out.println("Drops: " + creature.getDrops());
                System.out.println("Weakness: " + creature.getWeakness());
                List<String> contributions = creature.getCommunityContributions();
                if (!contributions.isEmpty()) {
                    System.out.println("Community Contributions: ");
                    for (String contribution : contributions) {
                        System.out.println("- " + contribution);
                    }
                }
                System.out.println();
            }
        }
    }
}

    



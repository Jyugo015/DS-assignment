/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author elysi
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class AdventurerDiary {

    private Map<Integer, String> entries;
    private int entryCounter;

    public AdventurerDiary() {
        this.entries = new LinkedHashMap<>();
        this.entryCounter = 1;
    }

    public void logEvent(String eventDescription) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = formattedTimestamp + " - " + eventDescription;
        this.entries.put(this.entryCounter++, entry);
        System.out.println("Event logged: " + entry);

    }

    public void displayEntries() {
        System.out.println("Adventurer's Diary Entries:");
        for (Map.Entry<Integer, String> entry : this.entries.entrySet()) {
            System.out.println("[entry " + entry.getKey() + "]");
            System.out.println("timestamp: " + entry.getValue().split(" - ")[0]);
            System.out.println("description: " + entry.getValue().split(" - ")[1]);
        }
    }

    public Map<Integer, String> getEntries() {
        return entries;
    }

    public String shareEntry(int entryNumber) {
        return entries.getOrDefault(entryNumber, "Invalid entry number");
    }

    public void shareEvent() {
        Scanner scanner = new Scanner(System.in);
        boolean continueSharing = true;
        List<String> sharedEvents = new ArrayList<>();

        while (continueSharing) {
            System.out.println("\nDo you want to share an event? (Enter the number of the entry or 'exit' to stop)");
            String input = scanner.next();

            if (input.equals("exit")) {
                continueSharing = false;
                continue;
            }

            int entryToShare = Integer.parseInt(input);
            if (entryToShare != 0) {
                String sharedEvent = shareEntry(entryToShare);
                sharedEvents.add(sharedEvent);
                System.out.println("Shared Entry:");
                System.out.println(sharedEvent);
            }

            // Ask if the user wants to share another event
            System.out.println("\nDo you want to share another event? (yes/no)");
            String shareAgainChoice = scanner.next().toLowerCase();
            if (shareAgainChoice.equals("no")) {
                continueSharing = false;
            }
        }

        // Display all shared events
        System.out.println("\nShared Events:");
        for (String sharedEvent : sharedEvents) {
            System.out.println(sharedEvent);
        }
    }
    public void searchEvent() {
        Scanner scanner = new Scanner(System.in);
        boolean searchAgain = true;

        while (searchAgain) {
            System.out.println("\nDo you want to search an event? (Enter the keyword of the entry or 'exit' to stop)");
            String keyword = scanner.next().toLowerCase();

            if (keyword.equals("exit")) {
                break;
            }

            boolean found = false;
            System.out.println("Search result:");
            for (Map.Entry<Integer, String> entry : this.entries.entrySet()) {
                String description = entry.getValue().split(" - ")[1];
                if (description.toLowerCase().contains(keyword)) {
                    found = true;
                    System.out.println(entry.getValue());
                    break; // Exit loop after finding the first match
                }
            }

            if (!found) {
                System.out.println("No matching events found.");
            }

            // Ask if the user wants to search another event
            System.out.println("\nDo you want to search for another event? (yes/no)");
            String searchAgainChoice = scanner.next().toLowerCase();
            if (searchAgainChoice.equals("no")) {
                searchAgain = false;
            }
        }
    }

    public void verifyAllEvents() {
        System.out.println("Verifying all events:");
        for (String entry : this.entries.values()) {
            System.out.println(entry);
            // Extract event description
            String eventDescription = entry.split(" - ")[1];
            // Verify event
            verifyEvent(eventDescription);
            System.out.println(); // Print a blank line for separation
        }
    }

    public void verifyEvent(String eventDescription) {
        boolean found = false;
        for (String entry : this.entries.values()) {
            if (entry.contains(eventDescription)) {
                found = true;
                break;
            }
        }
        if (found) {
            System.out.println("Event verified: true");
        } else {
            System.out.println("Event verified: false");
        }
    }


    public static void main(String[] args) {

        AdventurerDiary diary = new AdventurerDiary();
        diary.logEvent("Player joined the game");
        diary.logEvent("Achievement earned: DIAMONDS!");
        diary.logEvent("Discovered a village");
        diary.logEvent("Completed the Ender Dragon challenge");
        diary.displayEntries();

        diary.shareEvent();
        diary.searchEvent();

        // Verify all events
        diary.verifyAllEvents();
    }
}

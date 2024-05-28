package minecraft;

import java.sql.SQLException;

public class BSTs<E extends EnderBackpackItem>{
    String username = "defaultUser";
    private BinarySearchTree<EnderBackpackItem> ALLTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> toolsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> foodTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> arrrorsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> decorationsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> mobEggsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> weaponsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> materialTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> potionsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> recordsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> armorTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> transportationsTree = new BinarySearchTree<>(username);
    private BinarySearchTree<EnderBackpackItem> dyesTree = new BinarySearchTree<>(username);
//    private static BinarySearchTree[] trees = {toolsTree,foodTree,armorTree,arrrorsTree,decorationsTree,mobEggsTree,weaponsTree,materialTree,potionsTree,recordsTree,transportationsTree,dyesTree,ALLTree};
    
    public void add(String EnderBackpackItemName, int quantity) throws SQLException {
        EnderBackpackItem item = database_itemBox.getEnderBackpackItem(username, EnderBackpackItemName);
        if (item.type == null) { // the item is not inside the itemBox
            System.out.println("Type is null");
            item = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        }
        System.out.println("name: " + item.name);
        System.out.println("type: " + item.type);
        System.out.println("quantity: " + item.quantity);
        ALLTree.add(EnderBackpackItemName,quantity);
        switch (item.type) {
            case "Tools" :toolsTree.add(EnderBackpackItemName, quantity); break;
            case "Food" :foodTree.add(EnderBackpackItemName, quantity); break;
            case "Arrows" :arrrorsTree.add(EnderBackpackItemName, quantity); break;
            case "Decorations" :decorationsTree.add(EnderBackpackItemName, quantity); break;
            case "MobEggs" :mobEggsTree.add(EnderBackpackItemName, quantity); break;
            case "Weapons" :weaponsTree.add(EnderBackpackItemName, quantity); break;
            case "Armor" :armorTree.add(EnderBackpackItemName, quantity); break;
            case "Materials" :materialTree.add(EnderBackpackItemName, quantity); break;
            case "Transportations" :transportationsTree.add(EnderBackpackItemName, quantity); break;
            case "Potions" :potionsTree.add(EnderBackpackItemName, quantity); break;
            case "Records" :recordsTree.add(EnderBackpackItemName, quantity); break;
            case "Dyes" :dyesTree.add(EnderBackpackItemName, quantity); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void remove(String EnderBackpackItemName, int quantity) throws SQLException {
        EnderBackpackItem EnderBackpackItem = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        System.out.println("name: " + EnderBackpackItem.name);
        System.out.println("type: " + EnderBackpackItem.type);
        System.out.println("quantity: " + EnderBackpackItem.quantity);
        ALLTree.remove(EnderBackpackItemName, quantity);
        System.out.println("removed here");
        switch (EnderBackpackItem.type) {
            case "Tools" :toolsTree.remove(EnderBackpackItemName, quantity);  break;
            case "Food" :foodTree.remove(EnderBackpackItemName, quantity); break;
            case "Arrows" :arrrorsTree.remove(EnderBackpackItemName, quantity); break;
            case "Decorations" :decorationsTree.remove(EnderBackpackItemName, quantity); break;
            case "MobEggs" :mobEggsTree.remove(EnderBackpackItemName, quantity); break;
            case "Weapons" :weaponsTree.remove(EnderBackpackItemName, quantity); break;
            case "Armor" :armorTree.remove(EnderBackpackItemName, quantity); break;
            case "Materials" :materialTree.remove(EnderBackpackItemName, quantity); break;
            case "Transportations" :transportationsTree.remove(EnderBackpackItemName, quantity); break;
            case "Potions" :potionsTree.remove(EnderBackpackItemName, quantity); break;
            case "Records" :recordsTree.remove(EnderBackpackItemName, quantity); break;
            case "Dyes" :dyesTree.remove(EnderBackpackItemName, quantity); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void removeAll(String EnderBackpackItemName) throws SQLException {
        EnderBackpackItem EnderBackpackItem = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
        System.out.println("name: " + EnderBackpackItem.name);
        System.out.println("type: " + EnderBackpackItem.type);
        System.out.println("quantity: " + EnderBackpackItem.quantity);        ALLTree.removeAll(EnderBackpackItemName);
        switch (EnderBackpackItem.getType()) {
            case "Tools" :toolsTree.removeAll(EnderBackpackItemName);  break;
            case "Food" :foodTree.removeAll(EnderBackpackItemName); break;
            case "Arrows" :arrrorsTree.removeAll(EnderBackpackItemName); break;
            case "Decorations" :decorationsTree.removeAll(EnderBackpackItemName); break;
            case "MobEggs" :mobEggsTree.removeAll(EnderBackpackItemName); break;
            case "Weapons" :weaponsTree.removeAll(EnderBackpackItemName); break;
            case "Armor" :armorTree.removeAll(EnderBackpackItemName); break;
            case "Materials" :materialTree.removeAll(EnderBackpackItemName); break;
            case "Transportations" :transportationsTree.removeAll(EnderBackpackItemName); break;
            case "Potions" :potionsTree.removeAll(EnderBackpackItemName); break;
            case "Records" :recordsTree.removeAll(EnderBackpackItemName); break;
            case "Dyes" :dyesTree.removeAll(EnderBackpackItemName); break;
            default:
                throw new AssertionError();
        }
    }
    
    public int getTotalQuantity(){
        return ALLTree.getQuantity();
    }
    
    public int getQuantityOfCateogory(String category){
        switch (category) {
            case "Tools" : return toolsTree.getQuantity();
            case "Food" : return foodTree.getQuantity();
            case "Arrows" : return arrrorsTree.getQuantity();
            case "Decorations" : return decorationsTree.getQuantity();
            case "MobEggs" : return mobEggsTree.getQuantity();
            case "Weapons" : return weaponsTree.getQuantity();
            case "Armor" : return armorTree.getQuantity();
            case "Materials" : return materialTree.getQuantity();
            case "Transportations" : return transportationsTree.getQuantity();
            case "Potions" : return potionsTree.getQuantity();
            case "Records" : return recordsTree.getQuantity();
            case "Dyes" : return dyesTree.getQuantity();
            case "(Include ALL)" : return ALLTree.getQuantity();
            default:
                throw new AssertionError();
        }
    }
    
    public BinarySearchTree<EnderBackpackItem> getParticularCategory(String category) {
        switch (category) {
            case "Tools" : return toolsTree;
            case "Food" : return foodTree;
            case "Arrows" : return arrrorsTree;
            case "Decorations" : return decorationsTree;
            case "MobEggs" : return mobEggsTree;
            case "Weapons" : return weaponsTree;
            case "Armor" : return armorTree;
            case "Materials" : return materialTree;
            case "Transportations" : return transportationsTree;
            case "Potions" : return potionsTree;
            case "Records" : return recordsTree;
            case "Dyes" : return dyesTree;
            case "(Include ALL)" : return ALLTree;
            default:
                throw new AssertionError();
        }
    }

}

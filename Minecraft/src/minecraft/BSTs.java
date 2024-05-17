package minecraft;

public class BSTs<E extends Item>{
    private static BinarySearchTree<Item> ALLTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> toolsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> foodTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> arrrorsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> decorationsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> mobEggsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> weaponsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> materialTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> potionsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> recordsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> armorTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> transportationsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Item> dyesTree = new BinarySearchTree<>();
//    private static BinarySearchTree[] trees = {toolsTree,foodTree,armorTree,arrrorsTree,decorationsTree,mobEggsTree,weaponsTree,materialTree,potionsTree,recordsTree,transportationsTree,dyesTree,ALLTree};
    
    public void add(Item item, int quantity) {
        ALLTree.add(item,quantity);
        switch (item.getCategory()) {
            case "Tools" :toolsTree.add(item, quantity);  break;
            case "Food" :foodTree.add(item, quantity); break;
            case "Arrows" :arrrorsTree.add(item, quantity); break;
            case "Decorations" :decorationsTree.add(item, quantity); break;
            case "MobEggs" :mobEggsTree.add(item, quantity); break;
            case "Weapons" :weaponsTree.add(item, quantity); break;
            case "Armor" :armorTree.add(item, quantity); break;
            case "Materials" :materialTree.add(item, quantity); break;
            case "Transportations" :transportationsTree.add(item, quantity); break;
            case "Potions" :potionsTree.add(item, quantity); break;
            case "Records" :recordsTree.add(item, quantity); break;
            case "Dyes" :dyesTree.add(item, quantity); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void add(Item item) {
        ALLTree.add(item);
        switch (item.getCategory()) {
            case "Tools" :toolsTree.add(item); break;
            case "Food" :foodTree.add(item); break;
            case "Arrows" :arrrorsTree.add(item); break;
            case "Decorations" :decorationsTree.add(item); break;
            case "MobEggs" :mobEggsTree.add(item); break;
            case "Weapons" :weaponsTree.add(item); break;
            case "Armor" :armorTree.add(item); break;
            case "Materials" :materialTree.add(item); break;
            case "Transportations" :transportationsTree.add(item); break;
            case "Potions" :potionsTree.add(item); break;
            case "Records" :recordsTree.add(item); break;
            case "Dyes" :dyesTree.add(item); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void remove(Item item, int quantity) {
        String itemName = item.getName();
        ALLTree.remove(itemName, quantity);
        System.out.println("removed here");
        switch (item.getCategory()) {
            case "Tools" :toolsTree.remove(itemName, quantity);  break;
            case "Food" :foodTree.remove(itemName, quantity); break;
            case "Arrows" :arrrorsTree.remove(itemName, quantity); break;
            case "Decorations" :decorationsTree.remove(itemName, quantity); break;
            case "MobEggs" :mobEggsTree.remove(itemName, quantity); break;
            case "Weapons" :weaponsTree.remove(itemName, quantity); break;
            case "Armor" :armorTree.remove(itemName, quantity); break;
            case "Materials" :materialTree.remove(itemName, quantity); break;
            case "Transportations" :transportationsTree.remove(itemName, quantity); break;
            case "Potions" :potionsTree.remove(itemName, quantity); break;
            case "Records" :recordsTree.remove(itemName, quantity); break;
            case "Dyes" :dyesTree.remove(itemName, quantity); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void remove(Item item) {
        String itemName = item.getName();
        ALLTree.remove(itemName);
        switch (item.getCategory()) {
            case "Tools" :toolsTree.remove(itemName); break;
            case "Food" :foodTree.remove(itemName); break;
            case "Arrows" :arrrorsTree.remove(itemName); break;
            case "Decorations" :decorationsTree.remove(itemName); break;
            case "MobEggs" :mobEggsTree.remove(itemName); break;
            case "Weapons" :weaponsTree.remove(itemName); break;
            case "Armor" :armorTree.remove(itemName); break;
            case "Materials" :materialTree.remove(itemName); break;
            case "Transportations" :transportationsTree.remove(itemName); break;
            case "Potions" :potionsTree.remove(itemName); break;
            case "Records" :recordsTree.remove(itemName); break;
            case "Dyes" :dyesTree.remove(itemName); break;
            default:
                throw new AssertionError();
        }
    }
    
    public void removeAll(Item item) {
        String itemName = item.getName();
        ALLTree.removeAll(itemName);
        switch (item.getCategory()) {
            case "Tools" :toolsTree.removeAll(itemName);  break;
            case "Food" :foodTree.removeAll(itemName); break;
            case "Arrows" :arrrorsTree.removeAll(itemName); break;
            case "Decorations" :decorationsTree.removeAll(itemName); break;
            case "MobEggs" :mobEggsTree.removeAll(itemName); break;
            case "Weapons" :weaponsTree.removeAll(itemName); break;
            case "Armor" :armorTree.removeAll(itemName); break;
            case "Materials" :materialTree.removeAll(itemName); break;
            case "Transportations" :transportationsTree.removeAll(itemName); break;
            case "Potions" :potionsTree.removeAll(itemName); break;
            case "Records" :recordsTree.removeAll(itemName); break;
            case "Dyes" :dyesTree.removeAll(itemName); break;
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
    
    public BinarySearchTree<Item> getParticularCategory(String category) {
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

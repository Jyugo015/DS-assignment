package minecraft;

public class BSTs<E extends Item>{
    
    private static BinarySearchTree<Items.Tools> toolsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Food> foodTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Arrows> arrrorsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Decorations> decorationsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.MobEggs> mobEggsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Weapons> weaponTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Materials> materialTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Potions> potionsTree = new BinarySearchTree<>();
    private static BinarySearchTree<Items.Records> recordsTree = new BinarySearchTree<>();
    
    public void add(Item item) {
        if (item instanceof Items.Tools){
            toolsTree.add(item, 10);
        } else if (item instanceof Items.Food){
            foodTree.add(item, 10);
        } else if (item instanceof Items.Arrows){
            arrrorsTree.add(item, 10);
        } else if (item instanceof Items.Weapons){
            weaponTree.add(item, 10);
        } else if (item instanceof Items.Materials){
            materialTree.add(item, 10);
        } else if (item instanceof Items.Decorations){
            decorationsTree.add(item, 10);
        } else if (item instanceof Items.Potions){
            potionsTree.add(item, 10);
        } else if (item instanceof Items.MobEggs){
            mobEggsTree.add(item, 10);
        } else if (item instanceof Items.Records){
            recordsTree.add(item, 10);
        } 
    }
    
    public BinarySearchTree<Items.Arrows> getArrrorsTree() {
        return arrrorsTree;
    }

    public BinarySearchTree<Items.Decorations> getDecorationsTree() {
        return decorationsTree;
    }

    public BinarySearchTree<Items.Food> getFoodTree() {
        return foodTree;
    }

    public BinarySearchTree<Items.Materials> getMaterialTree() {
        return materialTree;
    }

    public BinarySearchTree<Items.MobEggs> getMobEggsTree() {
        return mobEggsTree;
    }

    public BinarySearchTree<Items.Potions> getPotionsTree() {
        return potionsTree;
    }

    public BinarySearchTree<Items.Records> getRecordsTree() {
        return recordsTree;
    }

    public BinarySearchTree<Items.Tools> getToolsTree() {
        return toolsTree;
    }

    public BinarySearchTree<Items.Weapons> getWeaponTree() {
        return weaponTree;
    }

    public void setMobEggsTree(BinarySearchTree<Items.MobEggs> mobEggsTree) {
        BSTs.mobEggsTree = mobEggsTree;
    }

}
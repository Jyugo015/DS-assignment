package minecraft;

import java.util.ArrayList; 
import java.util.List;

public class BinarySearchTree<E extends Item> {
    private Node<Item> root;
    private int size = 0;
    private int quantity = 0;
    private ArrayList<Item> retrivingList = new ArrayList<>();

//    public Node<Item> getRoot() {
//        return root;
//    }

    public int getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void add(Item item) {
        add(item, 1);
    }
    
    public void add(Item item, int quantity) {
        if (root == null) {
            root = new Node<>(item);
            add(item, quantity);
        } else if (quantity >= 1){
            Node<Item> parent = null;
            Node<Item> current = root;
            while (current != null) {                
                if (item.compareTo(current.item) <0) {
                    parent = current;
                    current = current.left;
                } else if (item.compareTo(current.item) >0) {
                    parent = current;
                    current = current.right;
                } else {
                    current.addQuantity(quantity);
                    this.quantity += quantity;
                    System.out.println("ADDED AMOUNT " + item.getName() + ", now have " + current.getQuantity());
//                    System.out.println("Size of the tree: " + getSize());
                    return;
                }
            }
            System.out.println("Parent: " + parent.item.getName());
            // if the item is not found in the original tree, insert as a new node
            if (item.compareTo(parent.item) < 0) {
                parent.left = new Node<>(item);
                parent.left.parent  = parent;
                parent.left.addQuantity(quantity); 
                System.out.println("Inserted new item " + item + ", now have " + parent.left.getQuantity());
            } else {
                parent.right = new Node<>(item);
                parent.right.parent  = parent;
                parent.right.addQuantity(quantity); 
                System.out.println("Inserted new item " + item + ", now have " + parent.right.getQuantity());
            }
            this.quantity += quantity;
        }
        size++;
//        System.out.println("Size of the tree: " + getSize());
    }
    
    public String remove(String itemName) {
        return remove(itemName, 1);
    }
    
    // ----------------------------got bug----------------------------
    public String remove(String itemName, int quantity) {
        if (size ==0) return null;
        Node<Item> current = getNode(itemName);
        if (current != null) {
            // remove the node entirely
            if (current.getQuantity() <= quantity) {
                if (current.right != null) {
                    Node<Item>leftmostOfTheRight = current.right;
                    while (leftmostOfTheRight.left != null) {                        
                        leftmostOfTheRight = leftmostOfTheRight.left;
                    }
                    if (current.right != leftmostOfTheRight) {
                        leftmostOfTheRight.parent.left = null;
                        leftmostOfTheRight.right =current.right;
                        if (current.right != null) {
                            current.right.parent = leftmostOfTheRight;
                        }
                        leftmostOfTheRight.left =current.left;
                        if (current.left != null) {
                            current.left.parent = leftmostOfTheRight;
                        }
                        
                        if (current != root){
                            leftmostOfTheRight.parent = current.parent;
                            current.parent.right = leftmostOfTheRight;
                        } else {
                            root = leftmostOfTheRight;
                            root.parent = null;
                        }
                    } else { // the right is directly its replace
                        leftmostOfTheRight.left = current.left;
                        if (current.left != null) {
                            current.left.parent = leftmostOfTheRight;
                        }
                        if (current != root){
                            if (current.item.getName().compareTo(current.parent.item.getName()) < 0) {
                                leftmostOfTheRight.parent = current.parent;
                                current.parent.left = leftmostOfTheRight;
                                
//                                System.out.println("Current: " + current.item.getName());
//                                System.out.println("current.parent.left: " + current.parent.left.item.getName());
                            } else {
                                leftmostOfTheRight.parent = current.parent;
                                current.parent.right = leftmostOfTheRight;
//                                System.out.println("Current: " + current.item.getName());
//                                System.out.println("current.parent.left: " + current.parent.left.item.getName());
                            }
                                
                        } else {
                            root = leftmostOfTheRight;
                            root.parent = null;
//                            System.out.println("New root: " + root.item.getName());
//                            System.out.println("root.left: " + root.left.item.getName());
//                            System.out.println("root.right: " + root.right.item.getName());
                        }
                    }
                } else if (current == root) {
                    root = current.left;
                    if (root!= null) {
                        root.parent = null;
                    }
                } else if (current.item.getName().compareTo(current.parent.item.getName()) > 0){
                    current.parent.right = current.left;
                    if (current.left != null) {
                        current.parent.right.parent = current.parent;
//                        System.out.println("Current.parent.left: " + current.parent.left);
                    }
                } else {
                    current.parent.left = current.left;
                    if (current.left != null) {
                        current.parent.left.parent = current.parent;
//                        System.out.println("Current.parent.left: " + current.parent.left);
//                        System.out.println("Current.left.parent: " + current.left.parent);
                    }
                }
                this.quantity -= current.quantity;
                current.removeQuantity(current.quantity);
                size--;
                current.parent = null;
                // ------------------------------------Add back quantity---------------------------------------------
                
                System.out.printf("All %s is removed. Remain? %b%n", current.item.getName(), contains(itemName));
            } else {
                current.removeQuantity(quantity);
                this.quantity -= quantity;
                System.out.printf("%d of %s is removed. Remain %d%n", quantity, current.item.getName(), current.getQuantity());
            }
            current.increaseCountOfUse();
            return current.item.getName();
        }
        return null;
    }
    
    public String removeAll(String itemName) {
        Node<Item> current = getNode(itemName);
        return (current != null) ? remove(itemName, current.getQuantity()) : null;
    }
    
    public boolean contains(String item) {
        Node<Item> current = getNode(item);
        return current != null;
    }
    
    public List<Item> retrivePossibleItemsAfterwards(String searchedItem) {
        Node<Item> current  = root;
        retriveAllItems();
        while (current != null) {
            int compare = searchedItem.compareToIgnoreCase(current.item.getName());
//            System.out.println("Compare: " + compare);
            if (compare == 0) {
                break;
            } else if (compare < 0 ) {
                if (current.left != null) {
                    current = current.left; 
                } else {
                    break;
                } 
            } else {
                if (current.left == null && current.right == null) {
                    current = current.parent;
                    break;
                }
                if (current.right != null && current.right.item.getName().compareToIgnoreCase(searchedItem)<0) {
                    current = current.parent;
                    break;
                } else {
                    current = current.right;
                }
            }
        }
        // if the item not found
        if (current == null) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < retrivingList.size(); i++) {
            if (retrivingList.get(i) == (current.item)) {
                index = i;
//                System.out.println(retrivingList.get(i));
//                System.out.println((current.item));
//                System.out.println("index " + index);
                break;
            }
        }
        return retrivingList.subList(index, size);
    }
    
    public ArrayList<Item> retriveAllItems() {
        retrivingList = new ArrayList<>();
        return retriveAllItemssupplement(root);
    }
    
    public Node<Item> getNode(String name) {
        if (size ==0) return null;
        Node<Item> current = root;
        while (current != null) {            
            if (name.compareToIgnoreCase(current.item.getName()) < 0)
                current = current.left;
            else if (name.compareToIgnoreCase(current.item.getName()) > 0)
                current = current.right;
            else{
//                System.out.println("Current: " +current.item.getName());
                return current;
            }
                
        }
        return null;
    }
    
    public int getQuantity(String item) {
        Node<Item> current = getNode(item);
        if (current!= null) {
            return current.getQuantity();
        }
        return 0;
    }
    
    private ArrayList<Item> retriveAllItemssupplement(Node<Item> subroot) {
        if (subroot == null) {
            return null;
        }
        if (subroot.left == null) {
            retrivingList.add(subroot.item); 
        } else {
            retriveAllItemssupplement(subroot.left);
            retrivingList.add(subroot.item); 
        } if (subroot.right!= null) {
            retriveAllItemssupplement(subroot.right);
        } 
        return retrivingList;
    }
    
    public void clear() {
        retrivingList = new ArrayList<>();
        size = 0;
        root = null;
    }
    
    private ArrayList<Node<Item>> path(Node<Item> node){
        ArrayList<Node<Item>> path = new ArrayList<>();
        Node<Item> current = getNode(node.item.getName());
        while (current != null) {            
            path.add(current);
            current = current.parent;
        }
        return path;
    }
    
    private class Node<E extends Item> {
        Item item;
        Node<Item> left;
        Node<Item> right;
        Node<Item> parent;
        int quantity = 0;
        int countOfUse = 0;
        
        public Node(Item item) {
            this.item = item;
        }
        
        public int addQuantity(int quantity) {
            this.quantity += quantity;
            return this.quantity;
        }

        public int getQuantity() {
            return quantity;
        } 

        public int getCountOfUse() {
            return countOfUse;
        }

        public void setCountOfUse(int countOfUse) {
            this.countOfUse = countOfUse;
        }

        public void increaseCountOfUse() {
            this.countOfUse++;
        }

        public int removeQuantity(int quantity) {
            if (this.quantity >= quantity) {
                this.quantity -= quantity;
            } else this.quantity = 0;

            return this.quantity;
        }

        public void resetQuantity() {
            this.quantity =0;
        }

        @Override
        public String toString() {
            return item.getName();
        }
    }
}

package minecraft;

import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.List;

public class BinarySearchTree<E extends EnderBackpackItem> {
    private Node<EnderBackpackItem> root;
    private int size = 0;
    private int quantity = 0;
    private String username;
    private ArrayList<EnderBackpackItem> retrivingList = new ArrayList<>();

    public BinarySearchTree(String username) {
        this.username = username;
    }

    public int getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void add(String EnderBackpackItemName, int quantity) throws SQLException {
        EnderBackpackItem EnderBackpackItem = database_itemBox.getEnderBackpackItem(username,EnderBackpackItemName);
//        if (EnderBackpackItem.type == null) { // the item is not inside the itemBox
//            System.out.println("Type is null");
//            EnderBackpackItem = database_item4.getEnderBackpackItem(username, EnderBackpackItemName);
//        }
        System.out.println("quantity in binary tree search add: " + quantity + " of " + EnderBackpackItemName + " type " + EnderBackpackItem.type==null ? database_item4.getEnderBackpackItem(username, EnderBackpackItemName) : EnderBackpackItem.type);
        if (root == null) {
            root = new Node<>(EnderBackpackItem);
            add(EnderBackpackItemName, quantity);
        } else if (quantity >= 1){
            Node<EnderBackpackItem> parent = null;
            Node<EnderBackpackItem> current = root;
            while (current != null) {                
                if (EnderBackpackItem.compareTo(current.EnderBackpackItem) <0) {
                    parent = current;
                    current = current.left;
                } else if (EnderBackpackItem.compareTo(current.EnderBackpackItem) >0) {
                    parent = current;
                    current = current.right;
                } else {
                    current.addQuantity(quantity);
                    this.quantity += quantity;
                    System.out.println("ADDED AMOUNT " + EnderBackpackItem.getName() + ", now have " + current.getQuantity());
                    System.out.println("Size of the tree: " + getSize());
                    return;
                }
            }
            System.out.println("Parent: " + parent.EnderBackpackItem.getName());
            // if the EnderBackpackItem is not found in the original tree, insert as a new node
            if (EnderBackpackItem.compareTo(parent.EnderBackpackItem) < 0) {
                parent.left = new Node<>(EnderBackpackItem);
                parent.left.parent  = parent;
                parent.left.addQuantity(quantity);  
                this.quantity += quantity ;
                System.out.println("Inserted new EnderBackpackItem " + EnderBackpackItem + ", now have " + parent.left.getQuantity());
            } else {
                parent.right = new Node<>(EnderBackpackItem);
                parent.right.parent  = parent;
                parent.right.addQuantity(quantity);
                System.out.println("Inserted new EnderBackpackItem " + EnderBackpackItem + ", now have " + parent.right.getQuantity()); 
                this.quantity += quantity;
            }
            
        }
        size++;
        System.out.println("Size of the tree: " + getSize());
    }
    
    // ----------------------------got bug----------------------------
    public String remove(String EnderBackpackItemName, int quantity) throws SQLException {
        if (size ==0) return null;
        Node<EnderBackpackItem> current = getNode(EnderBackpackItemName);
        if (current != null) {
            // remove the node entirely
            if (current.getQuantity() <= quantity) {
                if (current.right != null) {
                    Node<EnderBackpackItem>leftmostOfTheRight = current.right;
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
                            if (current.EnderBackpackItem.getName().compareTo(current.parent.EnderBackpackItem.getName()) < 0) {
                                leftmostOfTheRight.parent = current.parent;
                                current.parent.left = leftmostOfTheRight;
                                
//                                System.out.println("Current: " + current.EnderBackpackItem.getName());
//                                System.out.println("current.parent.left: " + current.parent.left.EnderBackpackItem.getName());
                            } else {
                                leftmostOfTheRight.parent = current.parent;
                                current.parent.right = leftmostOfTheRight;
//                                System.out.println("Current: " + current.EnderBackpackItem.getName());
//                                System.out.println("current.parent.left: " + current.parent.left.EnderBackpackItem.getName());
                            }
                                
                        } else {
                            root = leftmostOfTheRight;
                            root.parent = null;
//                            System.out.println("New root: " + root.EnderBackpackItem.getName());
//                            System.out.println("root.left: " + root.left.EnderBackpackItem.getName());
//                            System.out.println("root.right: " + root.right.EnderBackpackItem.getName());
                        }
                    }
                } else if (current == root) {
                    root = current.left;
                    if (root!= null) {
                        root.parent = null;
                    }
                } else if (current.EnderBackpackItem.getName().compareTo(current.parent.EnderBackpackItem.getName()) > 0){
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
                System.out.printf("All %s is removed. Remain? %b%n", current.EnderBackpackItem.getName(), contains(EnderBackpackItemName));
            } else {
                current.removeQuantity(quantity);
                this.quantity -= quantity;
                System.out.printf("%d of %s is removed. Remain %d%n", quantity, EnderBackpackItemName, current.getQuantity());
            }
            current.increaseCountOfUse();
            return current.EnderBackpackItem.getName();
        }
        return null;
    }
    
    public String removeAll(String EnderBackpackItemName) throws SQLException {
        Node<EnderBackpackItem> current = getNode(EnderBackpackItemName);
        return (current != null) ? remove(EnderBackpackItemName, current.getQuantity()) : null;
    }
    
    public boolean contains(String EnderBackpackItem) {
        Node<EnderBackpackItem> current = getNode(EnderBackpackItem);
        return current != null;
    }
    
    public List<EnderBackpackItem> retrivePossibleEnderBackpackItemsAfterwards(String searchedEnderBackpackItem) {
        Node<EnderBackpackItem> current  = root;
        retriveAllItems();
        boolean needAdd1 = false;
        while (current != null) {
            int compare = searchedEnderBackpackItem.compareToIgnoreCase(current.EnderBackpackItem.getName());
            System.out.println("serched : " + searchedEnderBackpackItem);
            System.out.println("current: " + current.EnderBackpackItem.getName());
            System.out.println("current.left: " + current.left);
            System.out.println("current.right: " + current.right);
            if (compare == 0) {
                break;
            } else if (compare < 0 ) {
                if (current.left != null) {
                    current = current.left; 
                } else {
                    break;
                } 
            } else {
                if (current.right == null){
                    if (current.parent!=null && current.parent.EnderBackpackItem.getName().compareToIgnoreCase(current.EnderBackpackItem.name) > 0) {
                        current = current.parent;
                        break;
                    } else {
                        needAdd1 = true;
                        break;
                    }
                } else {
                    current = current.right;
                }
            }
        }
        // if the EnderBackpackItem not found
        if (current == null) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < retrivingList.size(); i++) {
            if (retrivingList.get(i) == (current.EnderBackpackItem)) {
                index = i;
//                System.out.println(retrivingList.get(i));
//                System.out.println((current.EnderBackpackItem));
//                System.out.println("index " + index);
                break;
            }
        }
        return retrivingList.subList((needAdd1 && index + 1 < size)? index +1 : index, size);
    }
    
    public ArrayList<EnderBackpackItem> retriveAllItems() {
        retrivingList = new ArrayList<>();
        return retriveAllItemssupplement(root);
    }
    
    public Node<EnderBackpackItem> getNode(String name) {
        if (size ==0) return null;
        Node<EnderBackpackItem> current = root;
        while (current != null) {            
            if (name.compareToIgnoreCase(current.EnderBackpackItem.getName()) < 0)
                current = current.left;
            else if (name.compareToIgnoreCase(current.EnderBackpackItem.getName()) > 0)
                current = current.right;
            else{
//                System.out.println("Current: " +current.EnderBackpackItem.getName());
                return current;
            }
                
        }
        return null;
    }
    
    public int getQuantity(String EnderBackpackItem) {
        Node<EnderBackpackItem> current = getNode(EnderBackpackItem);
        if (current!= null) {
            return current.getQuantity();
        }
        return 0;
    }
    
    private ArrayList<EnderBackpackItem> retriveAllItemssupplement(Node<EnderBackpackItem> subroot) {
        if (subroot == null) {
            return null;
        }
        if (subroot.left == null) {
            retrivingList.add(subroot.EnderBackpackItem); 
        } else {
            retriveAllItemssupplement(subroot.left);
            retrivingList.add(subroot.EnderBackpackItem); 
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
    
    private ArrayList<Node<EnderBackpackItem>> path(Node<EnderBackpackItem> node){
        ArrayList<Node<EnderBackpackItem>> path = new ArrayList<>();
        Node<EnderBackpackItem> current = getNode(node.EnderBackpackItem.getName());
        while (current != null) {            
            path.add(current);
            current = current.parent;
        }
        return path;
    }
    
    private class Node<E extends EnderBackpackItem> {
        EnderBackpackItem EnderBackpackItem;
        Node<EnderBackpackItem> left;
        Node<EnderBackpackItem> right;
        Node<EnderBackpackItem> parent;
        int quantity = 0;
        int countOfUse = 0;
        
        public Node(EnderBackpackItem EnderBackpackItem) {
            this.EnderBackpackItem = EnderBackpackItem;
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
            return EnderBackpackItem.getName();
        }
    }
}

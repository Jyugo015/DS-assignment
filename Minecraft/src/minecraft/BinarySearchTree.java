package minecraft;

import java.util.ArrayList; 
import java.util.List;

public class BinarySearchTree<E extends Item> {
    private Node<Item> root;
    private int size =0;
    private ArrayList<Item> retrivingList = new ArrayList<>();

//    public Node<Item> getRoot() {
//        return root;
//    }

    public int getSize() {
        return size;
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
                    System.out.println("ADDED AMOUNT " + item+ ", now have " + current.getQuantity());
                    System.out.println("Size of the tree: " + getSize());
                    return;
                }
            }
            // if the item is not found in the original tree, insert as a new node
            if (item.compareTo(parent.item) < 0) {
                parent.left = new Node<>(item);
                parent.left.parent  = parent;
                parent.left.addQuantity(quantity); 
            } else {
                parent.right = new Node<>(item);
                parent.right.parent  = parent;
                parent.right.addQuantity(quantity); 
            }
            System.out.println("Inserted new item " + item + ", now have " + parent.getQuantity());
        }
        size++;
        System.out.println("Size of the tree: " + getSize());
    }
    
    public String remove(String itemName) {
        return remove(itemName, 1);
    }
    
    public String remove(String itemName, int quantity) {
        if (size ==0) return null;
        Node<Item> current = getNode(itemName);
        if (current != null) {
            // remove the node entirely
            if (current.getQuantity() == quantity) {
                if (current.right != null) {
                    Node<Item>leftmostOfTheRight = current.right;
                    while (leftmostOfTheRight.left != null) {                        
                        leftmostOfTheRight = leftmostOfTheRight.left;
                    }
                    if (current.right != leftmostOfTheRight) {
                        leftmostOfTheRight.parent.left = null;
                        leftmostOfTheRight.right = current.right;
                        if (current != root) 
                            current.parent.right = leftmostOfTheRight;
                        else 
                            root = leftmostOfTheRight;
                    } else { // the right is directly its replace
                        leftmostOfTheRight.left = current.left;
                        if (current != root)
                            current.parent.right = leftmostOfTheRight;
                        else  
                            root = leftmostOfTheRight;
                    } 
                } else if(current != root){
                    Node<Item> parent  = current.parent;
                    parent.left = current.left; 
                } else if (current == root) {
                    root = root.left; 
                }
                
                current.removeQuantity(quantity);
                size--;
                System.out.printf("All %s is removed. Remain? %b%n", current.item.getName(), contains(itemName));
            } else {
                current.removeQuantity(quantity);
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
        Node<Item> parent  = null;
        Node<Item> current  = root;
        retriveAllItems();
        while (current != null) {            
            if (searchedItem.compareTo(current.item.getName()) < 0 && current.left != null) {
                parent = current;
                current = current.left;
            } else {
                current = parent;
                break;
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
                System.out.println(retrivingList.get(i));
                System.out.println((current.item));
                System.out.println("index " + index);
                break;
            }
        }
        return retrivingList.subList(index, size-1);
    }
    
    public ArrayList<Item> retriveAllItems() {
        retrivingList = new ArrayList<>();
        return retriveAllItemssupplement(root);
    }
    
    private Node<Item> getNode(String name) {
        if (size ==0) return null;
        Node<Item> current = root;
        while (current != null) {            
            if (name.compareTo(current.item.getName()) < 0)
                current = current.left;
            else if (name.compareTo(current.item.getName()) > 0)
                current = current.right;
            else 
                return current;
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

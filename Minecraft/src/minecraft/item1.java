package minecraft;

import java.util.ArrayList;
public class item1 {
    public final int initialCapacity = 5;//to be set by user in the early configuration stage
    public int capacity, currentStock;
    public ArrayList<Item> list = new ArrayList<Item>();
    
    public item1(){
        this.capacity = initialCapacity;
        this.currentStock = 0;
    }
    
    public void addItem(Item add){
        if (this.list.size() <= capacity){
            this.list.add(add);
            this.currentStock++;
        }
        else 
            System.out.println("The backpack is full.");
    }
    
    public void removeItem(Item remove){
        if (this.list.contains(remove)){
            this.list.remove(remove);
            this.currentStock--;
        }
        else 
            System.out.println("The item is not in the backpack.");
    }

    public void clearItem(){
        this.list.clear();
        this.currentStock = 0;
    }

    public ArrayList<String> getItemSpecification(Item get){
        if (this.list.contains(get)){
            ArrayList<String> specification = new ArrayList<String>();
            specification.add(get.name);
            specification.add(get.type);
            specification.add(get.function);
            specification.add(Integer.toString(get.quantity));
            return specification;
        }
        return null;
    }

    public void addCapacity(int amount){//amount = 5/10/20/30
        this.capacity += amount;
    }

    public int getCurrentStock(){
        return this.list.size();
    }

    public int getCapacity(){
        return this.capacity;
    }
}

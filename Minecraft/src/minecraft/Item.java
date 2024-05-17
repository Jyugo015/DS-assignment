
package minecraft;

public class Item implements Comparable<Item>{
    private String name;
    private String category;
    private int quantity = 0;
    private int countOfUse = 0;
    
    public Item(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public int addQuantity(int quantity) {
        this.quantity += quantity;
        return this.quantity;
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

    public int getCountOfUse() {
        return countOfUse;
    }

    public void setCountOfUse(int countOfUse) {
        this.countOfUse = countOfUse;
    }

    public void increaseCountOfUse() {
        this.countOfUse++;
    }

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
    
}


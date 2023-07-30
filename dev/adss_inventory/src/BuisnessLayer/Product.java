package dev.adss_inventory.src.BuisnessLayer;

public class Product {
    private String name;
    private String producer;
    private int minAmount;
    private int currentAmount;
    private int amountInStore;
    private int amountInInventory;
    private Category category;
    private int ID;
    static int productID = 0;
   // float discount= 0;


    public Product(String name, String producer, int minAmount, int currentAmount, int amountInStore, int amountInInventory, Category category) {
        this.name = name;
        this.producer = producer;
        this.minAmount = minAmount;
        this.currentAmount = currentAmount;
        this.amountInStore = amountInStore;
        this.amountInInventory = amountInInventory;
        this.category = category;
        this.ID = productID;
        productID++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getAmountInStore() {
        return amountInStore;
    }

    public void setAmountInStore(int amountInStore) {
        this.amountInStore = amountInStore;
    }

    public int getAmountInInventory() {
        return amountInInventory;
    }

    public void setAmountInInventory(int amountInInventory) {
        this.amountInInventory = amountInInventory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    //this method is used to reduce items from inventory and
    // raise a warning if the amount is below the minimum amount
    public void reduceItems(int amount){
        if(amount <= this.currentAmount)
            this.currentAmount -= amount;
        if(minAmount >= currentAmount)
            System.out.println("Warning: The amount of " + name + " is below the minimum amount");
    }

}

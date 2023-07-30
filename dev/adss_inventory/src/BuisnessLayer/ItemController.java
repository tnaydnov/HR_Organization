package dev.adss_inventory.src.BuisnessLayer;

import java.util.*;

public class ItemController {
    private Dictionary<Integer, List<Item>> soldItems; //sold items by category ID
    private Dictionary<Integer, List<Item>> storageItems; //storage items by category ID
    private Dictionary<Integer, List<Item>> inStoreItems; //in store items by category ID
    private Dictionary<Integer, List<Item>> defectiveItems; //defective items by category ID
    private ProductController productController;


    public ItemController() {
    }
    //what do we get as location? string or enum?
     public void addItem(String place, String manufacturer , Integer barcode, String name, Date expirationDate, Integer costPrice ,int category) {
        Item.location locate= null;
        if(place.equals("store")) {
             locate = Item.location.STORE;
         } else if (place.equals("storage")) {
            locate = Item.location.INVENTORY;
        }
         Item item = new Item(manufacturer, name, locate, expirationDate, costPrice, -1);
         List<Item> items = new LinkedList<Item>();
         items.add(item);
         if (locate == Item.location.STORE) {
             //check if the item is already in the dictionary
             if (inStoreItems.get(category) == null) {
                 inStoreItems.put(category,items);
             } else {
                 inStoreItems.get(category).add(item);
             }
         } else if (locate == Item.location.INVENTORY) {
             if (storageItems.get(category) == null) {
                 storageItems.put(category,items);
             } else {
                 storageItems.get(category).add(item);
             }
         }
     }
     //sold item
        public void itemSold(int CategoryID, int ItemID) {
            //get item from storage
            //add to sold items
            //remove from storage
            if (inStoreItems.get(CategoryID).contains(ItemID)){
                soldItems.get(CategoryID).add(inStoreItems.get(CategoryID).get(ItemID));
                inStoreItems.get(CategoryID).remove(ItemID);
                //remove from product amount
                productController.setProductAmountById(ItemID,1);
            }
        }
        //get item
        public Item getItem(int CategoryID, int ItemID) {
            //get item from storage
            if (inStoreItems.get(CategoryID).contains(ItemID)){
                return inStoreItems.get(CategoryID).get(ItemID);
            }
            return null;
        }
        //+ItemsInStock()
        public List<Item> itemsInStock(int CategoryID) {
                //get all items from storage
             List<Item> items = new LinkedList<Item>();
             items.addAll(storageItems.get(CategoryID));
             items.addAll(inStoreItems.get(CategoryID));
                return items;
        }






}

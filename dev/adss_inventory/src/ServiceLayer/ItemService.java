package dev.adss_inventory.src.ServiceLayer;

import dev.adss_inventory.src.BuisnessLayer.Item;
import dev.adss_inventory.src.BuisnessLayer.ItemController;

import java.util.Date;
import java.util.List;

public class ItemService {
    //connect to item controller
    ItemController itemController;

    public ItemService(ItemController itemController) {
        this.itemController = itemController;
    }
    // AddItem(Place:string,Manufacturer:string ,Integer c )
    public void addItem(String place, String manufacturer , Integer barcode, String name, Date expirationDate, Integer costPrice , int category) {
        itemController.addItem(place, manufacturer, barcode, name, expirationDate, costPrice, category);
    }
    // SetMinimum-product- check where urs better to put it
    public void setMinimum(int itemID, String deliveryTime, String demand) {
        //itemController.setMinimum(itemID, deliveryTime, demand);
    }
    // getItem(ID: int ,barcode: int)
    public void getItem(int CategoryID,int ItemID) {
        itemController.getItem(CategoryID,ItemID);
    }

    //update Item has been sold
    public void ItemSold(int CategoryID,int ItemID){
        itemController.itemSold(CategoryID,ItemID);
        //need to update product amount
    }
    //return a list of items in stock (both at store and storage)
     public List<Item> getItemsInStock(int categoryID) {
         return itemController.itemsInStock(categoryID);
     }


}

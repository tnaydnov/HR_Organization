package dev.adss_inventory.src.BuisnessLayer;

import java.util.Date;
import java.util.Dictionary;

public class Category {
    //dictionary contains the product by category
    Dictionary<Integer,Product> productByCategory;
    String name;
    int id;
    Date startDiscount;
    Date endDiscount;



}

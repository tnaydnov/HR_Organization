package dev.adss_inventory.src.BuisnessLayer;

import java.util.Dictionary;
import java.util.List;

public class ProductController {
    private Dictionary<Integer, List<Product>> productByCategory;
    private Dictionary<Integer,List<Category>> categoryByProduct;
    private Dictionary<Integer, Product> ProductById;

    public ProductController() {
    }

    public void setMinimum(int productID, int minAmount) {
        ProductById.get(productID).setMinAmount(minAmount);
    }

    public void setProductAmountById(int productID, int amount) {
        ProductById.get(productID).setCurrentAmount( ProductById.get(productID).getCurrentAmount()-amount);
    }
}
